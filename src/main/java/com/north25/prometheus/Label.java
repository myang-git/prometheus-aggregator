package com.north25.prometheus;

public class Label implements Comparable<Label> {
	public static final char ESCAPED_CHAR = '"';
	
	private final String name;
	private final String value;
	
	private String string;
	private final int hashCode;
	
	public Label(String name, String value) {
		this.name = name;
		this.value = FormatUtils.escapeString(value, ESCAPED_CHAR);
		this.hashCode = this.name.hashCode() * 17 + this.value.hashCode();
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getValue() {
		return this.value;
	}

	public int compareTo(Label o) {
		if (o == null) {
			return 1;
		}
		return this.name.compareTo(o.name);
	}	

	public String toString() {
		if (this.string == null) {
			this.string = this.name + "=\"" + this.value + "\"";
		}
		return this.string;
	}
	
	public boolean equals(Object that) {
		if (!(that instanceof Label)) {
			return false;
		}
		
		if (this == that) {
			return true;
		}
		
		Label thatLabel = (Label)that;
		return this.name.equals(thatLabel.name) && this.value.equals(thatLabel.value);
	}
	
	public int hashCode() {
		return this.hashCode;
	}
}
