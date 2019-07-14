package com.north25.prometheus.aggregator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Iterator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.north25.prometheus.MetricGroup;
import com.north25.prometheus.MutableMetricGroup;
import com.north25.prometheus.TextFormatParser;

public class MetricAggregatorServlet extends HttpServlet {

	private static final long serialVersionUID = -7491782822048213134L;
	
	private MetricAggregator metricAggregator;
	private Gson gson;
	

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		this.metricAggregator = new MetricAggregator();
		this.gson = new Gson();
	}
	
	private void writeJsonResponse(JsonObject json, HttpServletResponse resp) throws UnsupportedEncodingException, IOException {
		resp.setContentType("test/json");
		JsonWriter jsonWriter = new JsonWriter(resp.getWriter());
		this.gson.toJson(json, jsonWriter);
		jsonWriter.flush();
	}

	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			super.service(req, resp);
		}
		catch (ServiceException se) {
			JsonObject respObject = new JsonObject();
			respObject.addProperty("error", se.getMessage() + ": " + se.getCause().getMessage());
			resp.setStatus(se.getHttpStatus());
			this.writeJsonResponse(respObject, resp);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain; version=0.0.4");
		String sortedParam = req.getParameter("sorted");
		String ageParam = req.getParameter("age");
		
		
		boolean printAge = "1".equals(ageParam);
		boolean isSorted = "1".equals(sortedParam); 

		OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), "UTF-8");
		
		Iterator<MutableMetricGroup> iter;
		if (isSorted) {
			iter = this.metricAggregator.sortedIterator();
		}
		else {
			iter = this.metricAggregator.iterator();
		}
		
		MetricGroupRenderer renderer = new MetricGroupRenderer(printAge, System.currentTimeMillis());
		while (iter.hasNext()) {
			MutableMetricGroup metricGroup = iter.next();
			renderer.writeMetricGroup(metricGroup, writer);
		}
		writer.flush();

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		InputStream ins = req.getInputStream();
		InputStreamReader reader = new InputStreamReader(ins);
		TextFormatParser parser = new TextFormatParser(reader);
		try {
			JsonObject respObject = new JsonObject();
			int metricCount = 0;
			while (true) {
				MetricGroup metricGroup = parser.nextMetricGroup();
				if (metricGroup == null) {
					break;
				}
				this.metricAggregator.observeMetricGroup(metricGroup);
				metricCount += 1;
			}
			respObject.addProperty("metric-count", metricCount);
			this.writeJsonResponse(respObject, resp);
		}
		catch (ParseException pe) {
			throw new ServiceException("Invalid metric format", 422, pe);
		}
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String maxAgeStr = req.getParameter("maxage");
		JsonObject json = new JsonObject();
		MutableSampleFilter sampleFilter;
		if (maxAgeStr == null) {
			sampleFilter = AnySampleFilter.sharedInstance;
		}
		else {
			int maxAge;
			try {
				maxAge = Integer.parseInt(maxAgeStr);
			}
			catch (NumberFormatException ne) {
				throw new ServiceException("Invalid maxAge", 422, ne);
			}
			sampleFilter = new ExpiredSampleFilter(System.currentTimeMillis(), maxAge);
		}
		
		int purgedCount = this.metricAggregator.purgeSamples(sampleFilter);
		json.addProperty("purged-sample-count", purgedCount);
		this.writeJsonResponse(json, resp);
	}
	

}
