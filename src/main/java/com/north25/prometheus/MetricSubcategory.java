package com.north25.prometheus;

public enum MetricSubcategory {

	BUCKET("bucket"),
	SUM("sum"),
	COUNT("count");
	
	public final String string;
	
	private MetricSubcategory(String string) {
		this.string = string;
	}
	
}
