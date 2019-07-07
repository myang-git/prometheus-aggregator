package com.north25.prometheus;

public class Comment {
	
	private final String type;
	private final String name;
	private final String desc;
	private final String text;
	
	public static class Builder {

		private String type;
		private String name;
		private String desc;
		private String text;

		private Builder() {
		}
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder type(String type) {
			this.type = type;
			return this;
		}
		
		public Builder desc(String desc) {
			this.desc = desc;
			return this;
		}
		
		public Builder text(String text) {
			this.text = text;
			return this;
		}

		public Comment build() {
			if (this.type == null) {
				throw new NullPointerException("type is null");
			}
			if (this.name == null) {
				throw new NullPointerException("name is null");
			}
			if (this.desc == null) {
				throw new NullPointerException("desc is null");
			}
			if (this.text == null) {
				this.text = String.format("#%s %s %s", this.type, this.name, this.desc);
			}
			return new Comment(this.type, this.name, this.desc, this.text);
		}
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	private Comment(String type, String name, String desc, String text) {
		this.type = type;
		this.name = name;
		this.desc = desc;
		this.text = text;
	}
	
	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	public String getDesc() {
		return this.desc;
	}

	public String getText() {
		return this.text;
	}
	
	public String toString() {
		return this.text;
	}
		
}