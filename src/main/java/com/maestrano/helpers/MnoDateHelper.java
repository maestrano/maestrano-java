package com.maestrano.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MnoDateHelper {
	private static final SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	
	public static Date fromIso8601(String dateStr) throws ParseException {
		return DF.parse(dateStr.replaceAll("Z$", "+0000"));
	}
	
	public static String toIso8601(Date dateObj) {
		DF.setTimeZone(TimeZone.getTimeZone("UTC"));
		return DF.format(dateObj).replaceAll("\\+0000$","Z");
	}
}
