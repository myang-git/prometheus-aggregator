/**
 * FormatUtils.java 
 * Created at 12:20:29 PM, Apr 30, 2018
 */

package com.north25.prometheus;

public class FormatUtils {
	
	public static String escapeString(String string, char escapeChar) {
		String regex = "(?<=[^\\\\])\\" + escapeChar;
		String replaceString = "\\\\" + escapeChar;
		return string.replaceAll(regex, replaceString);
	}
	
	private static void test_escape_string() throws Throwable {
		String s = "error = \"file not found\"";
		String escaped = escapeString(s, '"');
		System.out.println(escaped);
	}

	public static void main(String[] args) throws Throwable {
		test_escape_string();
	}
	
}
