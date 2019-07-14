package com.north25.util;

public class TimeDuration {
	
	private final String text;
	
	public static final long HOURS_PER_DAY = 24;
	public static final long MINUTES_PER_HOUR = 60;
	public static final long SECONDS_PER_MINUTE = 60;
	public static final long MILLIS_PER_SECOND = 1000;
	
	public TimeDuration(long millis) {
		if (millis < MILLIS_PER_SECOND) {
			this.text = String.valueOf(millis) + "ms";
		}
		else {
			this.text = this.createText(millis);
		}
	}
	
	private String createText(long millis) {
		StringBuilder sb = new StringBuilder();
		long millisPerDay = HOURS_PER_DAY * MINUTES_PER_HOUR * SECONDS_PER_MINUTE * MILLIS_PER_SECOND;
		long days = millis / millisPerDay;
		if (days > 0) {
			sb.append(days).append("d");
		}
		
		millis = millis % millisPerDay;
		long millisPerHour = millisPerDay / HOURS_PER_DAY;
		long hours = millis / millisPerHour;
		if (hours > 0) {
			sb.append(hours).append("h");
		}
		
		millis = millis % millisPerHour;
		long millisPerMinute = millisPerHour / MINUTES_PER_HOUR;
		long minutes = millis / millisPerMinute;
		if (minutes > 0) {
			sb.append(minutes).append("m");
		}
		
		millis = millis % millisPerMinute;
		long seconds = millis / MILLIS_PER_SECOND;
		if (seconds > 0) {
			sb.append(seconds).append("s");
		}
		
		millis = millis % MILLIS_PER_SECOND;
		if (millis > 0) {
			sb.append(millis).append("ms");
		}
		return sb.toString();
		
	}
	
	public String toString() {
		return this.text;
	}
	
	public static void main(String[] args) {
		TimeDuration dur = new TimeDuration(System.currentTimeMillis());
		System.out.println(dur);
	}

}
