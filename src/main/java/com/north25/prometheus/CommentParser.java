package com.north25.prometheus;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CommentParser {
		
	private final Pattern helpPattern;
	private final Pattern typePattern;
	private final Pattern genericPattern;
	
	public CommentParser() {
		this.helpPattern = Pattern.compile(Patterns.HELP_COMMENT_PATTERN);
		this.typePattern = Pattern.compile(Patterns.TYPE_COMMENT_PATTERN);
		this.genericPattern = Pattern.compile(Patterns.GENERIC_COMMENT_PATTERH);
	}
	
	
	public CommentElement parse(String text) throws ParseException {
		Matcher genericCommentMatcher = this.genericPattern.matcher(text);
		boolean isComment = genericCommentMatcher.matches();
		if (!isComment) {
			return null;
		}
		
		CommentType type = null;
		String metricName = null;
		MetricType metricType = null;
		String body = null;
		
		Matcher helpMatcher = this.helpPattern.matcher(text);
		if (helpMatcher.matches()) {
			type = CommentType.HELP;
			metricName = helpMatcher.group("metricname");
			body = helpMatcher.group("body");
		}
		if (type == null) {
			Matcher typeMatcher = this.typePattern.matcher(text);
			if (typeMatcher.matches()) {
				type = CommentType.TYPE;
				metricName = typeMatcher.group("metricname");
				metricType = MetricType.valueOf(typeMatcher.group("metrictype").toUpperCase());
			}
		}
		if (type == null) {
			type = CommentType.TEXT;
			body = genericCommentMatcher.group("body");
		}
		CommentElement comment = 
			CommentElement
				.newBuilder(metricName, metricType)
				.type(type)
				.body(body)
				.build();
		return comment;
		
	}

}
