package com.north25.prometheus.aggregator;

import com.north25.prometheus.MutableSampleElement;

public class ExpiredSampleFilter implements MutableSampleFilter {
	
	private final long refTime;
	private final long maxAgeMillis;
	
	public ExpiredSampleFilter(long refTime, long maxAgeMillis) {
		this.refTime = refTime;
		this.maxAgeMillis = maxAgeMillis;
	}

	public boolean accept(MutableSampleElement sample) {
		long reportTime = sample.getReportTimestamp();
		long age = refTime - reportTime;
		return age > maxAgeMillis;
	}

}
