package com.north25.prometheus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SampleElement extends MetricGroupElement {

	private Integer hashCode;
	private String uniqueId;
	protected final MetricSubcategory subcategory;
	protected final List<Label> labels;
	protected String value;
	protected Long timestamp;
	
	public static class Builder {

		private String metricName;
		private MetricType metricType;
		private MetricSubcategory subcategory;
		private List<Label> labels;
		private String value;
		private Long timestamp;
		
		private Builder(String metricName) {
			this.metricName = metricName;
			this.labels = new ArrayList<Label>();
		}
		
		public Builder setMetricType(MetricType metricType) {
			this.metricType = metricType;
			return this;
		}
		
		public Builder setSubcategory(MetricSubcategory subcategory) {
			this.subcategory = subcategory;
			return this;
		}
		
		public Builder setValue(String value) {
			this.value = value;
			return this;
		}
		
		public Builder addLabel(String name, String value) {
			value = FormatUtils.escapeString(value, '"');
			this.labels.add(new Label(name, value));
			return this;
		}
		
		public Builder addLabel(Label label) {
			this.labels.add(label);
			return this;
		}
		
		public Builder setTimestamp(long timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public SampleElement build() {
			if (this.metricName == null) {
				throw new NullPointerException("name is null");
			}
			if (this.value == null) {
				throw new IllegalArgumentException("value cannot be null");
			}
			if (this.labels.size() == 0) {
				this.labels = null;
			}
			else {
				Collections.sort(this.labels);
				this.labels = Collections.unmodifiableList(this.labels);
			}
			
			SampleElement sample = new SampleElement(this.metricName, this.metricType, this.subcategory, this.labels);
			sample.value = this.value;
			if (this.timestamp != null) {
				sample.timestamp = this.timestamp;
			}
			return sample;
		}

	}

	public static Builder newBuilder(String metricName) {
		return new Builder(metricName);
	}


	protected SampleElement(String name, MetricType metricType, MetricSubcategory subcategory, List<Label> labels) {
		super(name, metricType);
		this.subcategory = subcategory;
		this.labels = labels;
		this.hashCode = null;
		this.uniqueId = null;
	}
	
	public MetricSubcategory getSubcategory() {
		return this.subcategory;
	}

	public List<Label> getLabels() {
		return this.labels;
	}
	
	public long getTimestamp() {
		return this.timestamp;
	}


	public boolean equals(Object that) {
		if (!(that instanceof SampleElement)) {
			return false;
		}
		if (this == that) {
			return true;
		}
		
		SampleElement thatSample = (SampleElement) that;
		boolean namesEqual = this.metricName.equals(thatSample.metricName);
		boolean subcatEqual;
		if (this.getSubcategory() == null && thatSample.getSubcategory() == null) {
			subcatEqual = true;
		}
		else if (this.getSubcategory() != null && thatSample.getSubcategory() != null) {
			subcatEqual = this.getSubcategory().equals(thatSample.getSubcategory());
		}
		else {
			subcatEqual = false;
		}
		if (namesEqual && !subcatEqual) {
			return false;
		}
		
		
		if (this.labels == null && thatSample.labels == null) {
			return namesEqual && subcatEqual;
		}
		if (this.labels != null && thatSample.labels != null) {
			if (this.labels.size() != thatSample.labels.size()) {
				return false;
			}
			Iterator<Label> thisLabelIter = this.labels.iterator();
			Iterator<Label> thatLabelITer = thatSample.labels.iterator();
			int labelCount = this.labels.size();
			boolean labelsEqual = true;
			for (int i = 0; i < labelCount; i++) {
				Label thisLabel = thisLabelIter.next();
				Label thatLabel = thatLabelITer.next();
				if (!thisLabel.equals(thatLabel)) {
					return false;
				}
			}
			return namesEqual && labelsEqual;
		}
		
		// only one of the label lists is null. return false
		return false;
	}
	
	public String getUniqueId() {
		if (this.uniqueId == null) {
			StringBuilder builder = new StringBuilder(this.metricName);
			if (this.subcategory != null) {
				builder.append("_").append(this.subcategory.string);
			}
			if (this.labels != null && this.labels.size() > 0) {
				Iterator<Label> iter = this.labels.iterator();
				builder.append(" {");
				builder.append(iter.next().toString());
				while (iter.hasNext()) {
					builder.append(", ");
					builder.append(iter.next().toString());
				}
				builder.append("}");
			}
			this.uniqueId = builder.toString();
		}
		return this.uniqueId;
	}

	public int hashCode() {
		if (this.hashCode != null) {
			return this.hashCode.intValue();
		}
		int code = this.metricName.hashCode();
		if (this.labels != null) {
			for (Label lbl : this.labels) {
				code = code * 17 + lbl.hashCode();
			}
		}
		this.hashCode = code;
		return this.hashCode;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder(this.getUniqueId());
		builder.append(" ").append(this.value);
		if (this.timestamp != null) {
			builder.append(" ").append(this.timestamp);
		}
		return builder.toString();
	}
}
