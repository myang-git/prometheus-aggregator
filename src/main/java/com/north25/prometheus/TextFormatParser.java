package com.north25.prometheus;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;


public class TextFormatParser {

	private final BufferedReader reader;
	private MetricGroup.Builder metricGroupBuilder;
	private SampleParser sampleParser;
	private CommentParser commentParser;
	
	public TextFormatParser(Reader reader) {
		this.reader = new BufferedReader(reader);
		this.metricGroupBuilder = MetricGroup.newBuilder();
		this.sampleParser = new SampleParser();
		this.commentParser = new CommentParser();
	}
	
	private MetricGroup handleNewElement(MetricGroupElement newElement) {
		String currentMetricName = this.metricGroupBuilder.getMetricName();
		MetricGroup metricGroup = null;
		if (!newElement.getMetricName().equals(currentMetricName)) {
			if (this.metricGroupBuilder.canBuild()) {
				metricGroup = this.metricGroupBuilder.build();
			}
			this.metricGroupBuilder.reset();
		}
		return metricGroup;
	}
	
	public MetricGroup nextMetricGroup() throws IOException, ParseException {
		if (this.metricGroupBuilder == null) {
			this.metricGroupBuilder = MetricGroup.newBuilder();
		}

		MetricGroup metricGroup = null;
		while (metricGroup == null) {
			String line = this.reader.readLine();
			if (line == null) {
				if (this.metricGroupBuilder.canBuild()) {
					metricGroup = this.metricGroupBuilder.build();
				}
				this.metricGroupBuilder.reset();
				break;
			}
			
			MetricGroupElement element = null;
			
			// try parsing comment
			element = this.commentParser.parse(line);
			if (element != null) {
				CommentElement comment = (CommentElement)element;
				if (comment.getCommentType() == CommentType.TYPE) {
					metricGroup = this.handleNewElement(comment);
					this.metricGroupBuilder.setType(comment);
				}
				else if (comment.getCommentType() == CommentType.HELP) {
					metricGroup = this.handleNewElement(comment);
					this.metricGroupBuilder.setHelp(comment);
				}
				continue;
			}
			
			// try parsing sample
			element = this.sampleParser.parse(line);
			if (element != null) {				
				metricGroup = this.handleNewElement(element);
				this.metricGroupBuilder.addSample((SampleElement)element);
				continue;
			}

			if (element == null) {
				throw new ParseException(line, 0);
			}
		}
		return metricGroup;
	}
	
	private static void test0_parse_simple() throws Throwable {
		String line = "http_requests_total{method=\"post\",code=\"400\"}    3\n" + 
					  "metric_without_timestamp_and_labels 12.47";
		
		StringReader reader = new StringReader(line);
		TextFormatParser parser = new TextFormatParser(reader);
		while (true) {
			MetricGroup mg = parser.nextMetricGroup();
			if (mg == null) {
				break;
			}
			System.out.println(mg);
		}
	}
	
	private static void test1_parse_multiple() throws Throwable {
		FileReader fr = new FileReader("./test/sample.txt");
		TextFormatParser parser = new TextFormatParser(fr);
		while (true) {
			MetricGroup mg = parser.nextMetricGroup();
			if (mg == null) {
				break;
			}
			System.out.println(mg);
		}
	}
	
	public static void main(String[] args) throws Throwable {
		test1_parse_multiple();
	}
}
