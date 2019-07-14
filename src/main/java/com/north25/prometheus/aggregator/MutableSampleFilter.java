package com.north25.prometheus.aggregator;

import com.north25.prometheus.MutableSampleElement;

public interface MutableSampleFilter {
	
	public boolean accept(MutableSampleElement sample);

}
