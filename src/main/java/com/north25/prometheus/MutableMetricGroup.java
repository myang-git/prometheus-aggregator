package com.north25.prometheus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.north25.prometheus.aggregator.MutableSampleFilter;

public class MutableMetricGroup extends MetricGroup {
	
	private Map<String, MutableSampleElement> sampleIndex;
	private long lastUpdateTime;

	public MutableMetricGroup(String metricName, MetricType metricType, CommentElement type, CommentElement help) {
		super(metricName, metricType, type, help);
		this.sampleIndex = new HashMap<String, MutableSampleElement>();
		this.lastUpdateTime = System.currentTimeMillis();
	}
	
	public MutableMetricGroup(MetricGroup metricGroup) {
		this(metricGroup.metricName, metricGroup.metricType, metricGroup.typeComment, metricGroup.helpComment);
		if (metricGroup.samples != null) {
			for (SampleElement sample : metricGroup.samples) {
				MutableSampleElement mutableSample = new MutableSampleElement(sample);
				super.samples.add(mutableSample);
			}
		}
	}
	
	public void addSample(SampleElement sample) {
		String uniqueId = sample.getUniqueId();
		MutableSampleElement existing = this.sampleIndex.get(uniqueId);
		if (existing != null) {
			existing.setValue(sample.value);
			if (sample.timestamp != null) {
				existing.setTimestamp(sample.timestamp);
			}
			existing.setReportTimestamp(System.currentTimeMillis());
		}
		else {
			MutableSampleElement mutable = new MutableSampleElement(sample);
			this.sampleIndex.put(uniqueId, mutable);
			super.samples.add(mutable);
		}
		this.setLastUpdateTime(System.currentTimeMillis());
	}
	
	public void setLastUpdateTime(long timestamp) {
		this.lastUpdateTime = timestamp;
	}
	
	public long getLastUpdateTime() {
		return this.lastUpdateTime;
	}
	
	public int purgeSamples(MutableSampleFilter filter) {
		Iterator<SampleElement> iter = this.samples.iterator();
		int removedSampleCount = 0;
		while (iter.hasNext()) {
			MutableSampleElement sample = (MutableSampleElement)iter.next();
			if (filter.accept(sample)) {
				iter.remove();
				removedSampleCount += 1;
			}
		}
		return removedSampleCount;
}
}
