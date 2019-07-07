package com.north25.prometheus;

public abstract class MetricGroupElement {
	
	protected final String metricName;
	protected final MetricType metricType;
	
	public MetricGroupElement(String metricName, MetricType metricType) {
		this.metricName = metricName;
		this.metricType = metricType;
	}
	
	public String getMetricName() {
		return this.metricName;
	}
	
	public MetricType getMetricType() {
		return this.metricType;
	}

}
