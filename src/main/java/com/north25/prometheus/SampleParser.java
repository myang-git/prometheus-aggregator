package com.north25.prometheus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SampleParser {

	private final Pattern samplePattern;
	private final Pattern subcategoryPattern;
	private final Pattern labelPattern;
	
	public SampleParser() {
		this.samplePattern = Pattern.compile(Patterns.SAMPLE_PATTERN);
		this.subcategoryPattern = Pattern.compile(Patterns.METRIC_SUBCATEGORY_PATTERN);
		this.labelPattern = Pattern.compile(Patterns.LABEL_VALUE_PATTERN);		
	}
	
	private MetricSubcategory findSubCategory(String metricName) {
		Matcher subcatMatcher = this.subcategoryPattern.matcher(metricName);
		if (!subcatMatcher.matches()) {
			return null;
		}
		return MetricSubcategory.valueOf(subcatMatcher.group("subcategory").toUpperCase());
	}
	
	void handleLabels(String labels, SampleElement.Builder sampleBuilder) {
		if (labels != null) {
			Matcher labelMatcher = this.labelPattern.matcher(labels);
			while(labelMatcher.find()) {
				String key = labelMatcher.group("key");
				String value = labelMatcher.group("value");
				sampleBuilder.addLabel(key, value);
			}
		}
	}	
	
	public SampleElement parse(String text) {
		Matcher sampleMatcher = this.samplePattern.matcher(text);
		if (!sampleMatcher.matches()) {
			return null;
		}
		String metricName = sampleMatcher.group("metricname");
		MetricSubcategory subcategory = this.findSubCategory(metricName);
		if (subcategory != null) {
			int metricNameLength = metricName.length();
			metricName = metricName.substring(0, metricNameLength - subcategory.string.length() - 1);			
		}
		SampleElement.Builder sampleBuilder = SampleElement.newBuilder(metricName);
		sampleBuilder.setSubcategory(subcategory);

		String value = sampleMatcher.group("value");
		sampleBuilder.setValue(value);
		
		String timestampStr = sampleMatcher.group("timestamp");
		if (timestampStr != null) {
			sampleBuilder.setTimestamp(Long.parseLong(timestampStr));
		}
		
		this.handleLabels(sampleMatcher.group("labels"), sampleBuilder);

		SampleElement sample = sampleBuilder.build();
		return sample;
	}
	
	
}
