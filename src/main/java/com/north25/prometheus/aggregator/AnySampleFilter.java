package com.north25.prometheus.aggregator;

import com.north25.prometheus.MutableSampleElement;

public class AnySampleFilter implements MutableSampleFilter {
	
	public final static AnySampleFilter sharedInstance = new AnySampleFilter();
	
	private AnySampleFilter() {
		
	}

	public boolean accept(MutableSampleElement sample) {
		return true;
	}

}
