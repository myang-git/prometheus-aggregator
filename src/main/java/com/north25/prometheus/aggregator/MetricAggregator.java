package com.north25.prometheus.aggregator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.north25.prometheus.MetricGroup;
import com.north25.prometheus.MutableMetricGroup;
import com.north25.prometheus.SampleElement;

public class MetricAggregator {
	
	private final Map<String, MutableMetricGroup> metricMap;
	
	public MetricAggregator() {
		this.metricMap = new HashMap<String, MutableMetricGroup>();
	}
	
	private class MetricGroupIterator implements Iterator<MutableMetricGroup> {
		
		private final Iterator<Map.Entry<String, MutableMetricGroup>> delegate;
		
		public MetricGroupIterator(boolean sorted) {
			ArrayList<Map.Entry<String, MutableMetricGroup>> entries = new ArrayList<Map.Entry<String, MutableMetricGroup>>(metricMap.entrySet());
			if (sorted) {
				Comparator<Map.Entry<String, MutableMetricGroup>> cmp = new Comparator<Map.Entry<String,MutableMetricGroup>>() {
					public int compare(Entry<String, MutableMetricGroup> o1, Entry<String, MutableMetricGroup> o2) {
						return o1.getKey().compareTo(o2.getKey());
					}
				};
				Collections.sort(entries, cmp);
			}
			this.delegate = entries.iterator();
		}

		public void remove() {
			
		}
		
		public boolean hasNext() {
			return this.delegate.hasNext();
		}

		public MutableMetricGroup next() {
			return this.delegate.next().getValue();
		}		
	}
	
	public Iterator<MutableMetricGroup> sortedIterator() {
		return new MetricGroupIterator(true);
	}
	
	public Iterator<MutableMetricGroup> iterator() {
		return new MetricGroupIterator(false);
	}
	
	public MetricGroup remove(String metricName) {
		return this.metricMap.remove(metricName);
	}
	
	public int metricCount() {
		return this.metricMap.size();
	}
	
	public void observeMetricGroup(MetricGroup metricGroup) {
		MutableMetricGroup existing = this.metricMap.get(metricGroup.getMetricName());
		if (existing == null) {
			existing = new MutableMetricGroup(metricGroup);
			this.metricMap.put(metricGroup.getMetricName(), existing);
		}
		else {
			List<SampleElement> samples = metricGroup.getSamples();
			if (samples != null) {
				for (SampleElement sample : samples) {
					existing.addSample(sample);
				}
			}
		}
	}

}
