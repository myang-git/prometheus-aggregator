package com.north25.prometheus;

public enum MetricType {
	
	COUNTER("counter"),
	GAUGE("gauge"),
	HISTOGRAM("histogram"),
	SUMMARY("summary"),
	UNTYPED("untyped");
	
	public final String string;
	
	MetricType(String value) {
		this.string = value;
	}
	

}
