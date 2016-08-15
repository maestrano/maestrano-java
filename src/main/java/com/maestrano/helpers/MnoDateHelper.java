package com.maestrano.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MnoDateHelper {
	private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			return dateFormat;
		}
	};

	public static Date fromIso8601(String dateStr) throws ParseException {
		return DATE_FORMAT.get().parse(dateStr.replaceAll("Z$", "+0000"));
	}

	public static String toIso8601(Date dateObj) {
		return DATE_FORMAT.get().format(dateObj).replaceAll("\\+0000$","Z");

	}
}
