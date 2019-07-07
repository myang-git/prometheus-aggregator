package com.north25.prometheus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MetricGroup {
	
	protected final String metricName;
	protected final MetricType metricType;
	protected final CommentElement typeComment;
	protected final CommentElement helpComment;
	protected final List<SampleElement> samples;

	public static Builder newBuilder() {
		return new Builder();
	}
	
	public static class Builder {
		
		private String metricName;
		private MetricType metricType;
		private CommentElement typeComment;
		private CommentElement helpComment;
		private List<SampleElement> samples;
		
		private Builder() {
			this.metricName = null;
			this.typeComment = null;
			this.helpComment = null;
			this.samples = null;
		}
		
		void setMetricName(String name) {
			this.metricName = name;
		}
		
		String getMetricName() {
			return this.metricName;
		}
		
		CommentElement getType() {
			return this.typeComment;
		}
		
		Builder setType(CommentElement type) {
			if (this.metricName != null && !this.metricName.equals(type.getMetricName())) {
				throw new IllegalStateException("current metric name is " + this.metricName + ", but comment element has " + type.getMetricName());
			}
			else if (this.metricName == null) {
				this.metricName = type.getMetricName();
			}
			this.typeComment = type;
			this.metricType = type.getMetricType();
			return this;
		}
		
		CommentElement getHelp() {
			return this.helpComment;
		}
		
		Builder setHelp(CommentElement help) {
			if (this.metricName != null && !this.metricName.equals(help.getMetricName())) {
				throw new IllegalStateException("current metric name is " + this.metricName + ", but help element has " + help.getMetricName());
			}
			else if (this.metricName == null) {
				this.metricName = help.getMetricName();
			}
			
			this.helpComment = help;
			return this;
		}
		
		Builder addSample(SampleElement sample) {
			if (this.metricName != null && !this.metricName.equals(sample.getMetricName())) {
				throw new IllegalStateException("current metric name is " + this.metricName + ", but sample element has " + sample.getMetricName());
			}
			else if (this.metricName == null) {
				this.metricName = sample.getMetricName();
			}
			
			if (this.metricType == MetricType.HISTOGRAM) {
				if (sample.getSubcategory() != MetricSubcategory.BUCKET &&
					sample.getSubcategory() != MetricSubcategory.SUM &&
					sample.getSubcategory() != MetricSubcategory.COUNT) {
					throw new IllegalArgumentException("invalid subcategory for histogram, expected bucket, sum, count: " + sample.getSubcategory());
				}
			}
			
			if (this.metricType == MetricType.SUMMARY && sample.getSubcategory() != null) {
				if (sample.getSubcategory() != MetricSubcategory.SUM &&
					sample.getSubcategory() != MetricSubcategory.COUNT) {
					throw new IllegalArgumentException("invalid subcategory for summary, expected sum, count: " + sample.getSubcategory());
				}				
			}
			
			if (this.samples == null) {
				this.samples = new ArrayList<SampleElement>();
			}
			this.samples.add(sample);
			return this;
		}
		
		void reset() {
			this.typeComment = null;
			this.helpComment = null;
			if (this.samples != null) {
				this.samples.clear();
				this.samples = null;
			}
			this.metricName = null;
			this.metricType = null;
		}
		
		boolean canBuild() {
			if (this.metricName == null) {
				return false;
			}
			return true;
		}
		
		MetricGroup build() {
			if (!this.canBuild()) {
				throw new IllegalStateException("builder is empty");
			}
			MetricGroup metricGroup = new MetricGroup(this.metricName, this.metricType, this.typeComment, this.helpComment);
			if (this.samples != null && !this.samples.isEmpty()) {
				metricGroup.samples.addAll(this.samples);
			}
			return metricGroup;
		}
	}
	
	protected MetricGroup(String metricName, MetricType metricType, CommentElement type, CommentElement help) {
		this.metricName = metricName;
		this.metricType = metricType;
		this.typeComment = type;
		this.helpComment = help;
		this.samples = new ArrayList<SampleElement>();
	}
	
	public String getMetricName() {
		return this.metricName;
	}
	
	public MetricType getMetricType() {
		return this.metricType;
	}
	
	public CommentElement getTypeComment() {
		return this.typeComment;
	}
	
	public CommentElement getHelpComment() {
		return this.helpComment;
	}
	
	public List<SampleElement> getSamples() {
		return Collections.unmodifiableList(this.samples);
	}
	
	public String toString() {
		StringBuilder sbldr = new StringBuilder();
		int tokens = 0;
		if (this.typeComment != null) {
			sbldr.append(this.typeComment.toString());
			tokens++;
		}
		if (this.helpComment != null) {
			if (tokens > 0) {
				sbldr.append("\n");
			}
			sbldr.append(this.helpComment.toString());
			tokens++;
		}
		if (this.samples != null) {
			for (SampleElement sample : this.samples) {
				if (tokens > 0) {
					sbldr.append("\n");
				}
				sbldr.append(sample.toString());
				tokens++;
			}
		}
		return sbldr.toString();
	}
}
