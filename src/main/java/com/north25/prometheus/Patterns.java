package com.north25.prometheus;

final class Patterns {

	public static final String METRIC_NAME_PATTERN = "(?<metricname>[a-zA-Z_:][a-zA-Z0-9_:]*)";
	public static final String METRIC_SUBCATEGORY_PATTERN = "\\s*" + METRIC_NAME_PATTERN + "\\_(?<subcategory>bucket|sum|count)";
	public static final String LABELS_PATTERN = "(?<labels>(?:[a-zA-Z_:][a-zA-Z0-9_:]*?\\s*=\\s*\\\".+?\\\")(\\s*,\\s*([a-zA-Z_:][a-zA-Z0-9_:]*?\\s*=\\s*\\\".?\\\"))*?)";
	public static final String SAMPLE_PATTERN = "^" + METRIC_NAME_PATTERN + "\\s*(?:\\{\\s*" + LABELS_PATTERN + "\\s*\\})?\\s+(?<value>\\S+)\\s*(?<timestamp>[\\+\\-]?[1-9][0-9]*)??\\s*$"; 
	public static final String LABEL_VALUE_PATTERN = "(?<key>[a-zA-Z_:][a-zA-Z0-9_:]*)\\s*=\\s*\"(?<value>(?:[^\"\\\\]|\\\\.)*)\"";
	
	// counter, gauge, histogram, summary, or untyped
	
	public static final String TYPE_COMMENT_PATTERN = "\\s*\\#\\s*(?:(?:(?:TYPE))\\s+" + METRIC_NAME_PATTERN + ")(?:\\s+(?<metrictype>counter|gauge|histogram|summary|untyped))\\s*";
	public static final String HELP_COMMENT_PATTERN = "\\s*\\#\\s*(?:(?:(?:HELP))\\s+" + METRIC_NAME_PATTERN + ")(?:\\s+(?<body>.+))\\s*";
	public static final String GENERIC_COMMENT_PATTERH = "\\s*#\\s*(?<body>.*)";

	
	public static void main(String[] args) {
		System.out.println(TYPE_COMMENT_PATTERN);
	}
}
