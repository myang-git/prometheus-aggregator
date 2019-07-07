package com.north25.prometheus;

public class CommentElement extends MetricGroupElement {
	
	private CommentType type;
	private String body;

	public static Builder newBuilder(String metricName, MetricType metricType) {
		return new Builder(metricName, metricType);
	}
	
	public static class Builder {
		private CommentElement instance;
		
		private Builder(String metricName, MetricType metricType) {
			this.instance = new CommentElement(metricName, metricType);
		}
		
		public Builder type(CommentType type) {
			this.instance.type = type;
			return this;
		}
		
		public Builder body(String desc) {
			this.instance.body = desc;
			return this;
		}
		
		public CommentElement build() {
			if (this.instance.type == null) {
				throw new IllegalArgumentException("type is not set");
			}
			if (this.instance.type != CommentType.TEXT && this.instance.getMetricName() == null) {
				throw new NullPointerException("metricName is null");
			}

			if (this.instance.type == CommentType.TYPE && instance.metricType == null) {
				throw new IllegalArgumentException("metric type cannot be null for TYPE comment");
			}
			return this.instance;
		}
	}
	
	private CommentElement(String metricName, MetricType metricType) {
		super(metricName, metricType);
	}
	
	public CommentType getCommentType() {
		return this.type;
	}
	
	public String getDescription() {
		return this.body;
	}

	public String toString() {
		StringBuilder sbldr = new StringBuilder("#");
		sbldr.append(" ").append(this.type.name());
		if (this.metricName != null) {
			sbldr.append(" ").append(this.metricName);
		}
		if (this.metricType != null) {
			sbldr.append(" ").append(this.metricType.name().toLowerCase());
		}
		if (this.body != null) {
			sbldr.append(" ").append(this.body);			
		}
		return sbldr.toString();
	}
}
