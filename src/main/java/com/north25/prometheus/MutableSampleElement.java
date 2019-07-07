package com.north25.prometheus;

import java.util.List;

public class MutableSampleElement extends SampleElement {

	public MutableSampleElement(String name, MetricType metricType, MetricSubcategory subcategory, List<Label> labels) {
		super(name, metricType, subcategory, labels);
	}

	public MutableSampleElement(SampleElement sample) {
		super(sample.metricName, sample.metricType, sample.subcategory, sample.labels);
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
