package com.north25.prometheus.aggregator;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.north25.prometheus.MutableMetricGroup;


public class MetricAggregatorServlet extends HttpServlet {

	private static final long serialVersionUID = -7491782822048213134L;
	private MetricAggregator aggregator;
	
	public void init(ServletConfig config) throws ServletException {
		this.aggregator = new MetricAggregator();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain; version=0.0.4");
		OutputStream os = resp.getOutputStream();
		String sortedParam = req.getParameter("sorted");
		String ageParam = req.getParameter("age");
		
		boolean printAge = "1".equals(ageParam);
		boolean sorted = "1".equals(sortedParam); 

		OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
		Iterator<MutableMetricGroup> metricGroupIter = null;
		if (sorted) {
			metricGroupIter = this.aggregator.sortedIterator();
		}
		else {
			metricGroupIter = this.aggregator.iterator();
		}

		MetricGroupRenderer renderer = new MetricGroupRenderer(printAge, System.currentTimeMillis());
		while (metricGroupIter.hasNext()) {
			MutableMetricGroup metrigGroup = metricGroupIter.next();
			renderer.writeMetricGroup(metrigGroup, writer);
		}

		writer.flush();
	}
	
}
