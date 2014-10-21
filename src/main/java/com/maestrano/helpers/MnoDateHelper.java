package com.maestrano.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MnoDateHelper {
	
	public static Date fromIso8601(String dateStr) throws ParseException {
		SimpleDateFormat simpleDf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		return simpleDf.parse(dateStr.replaceAll("Z$", "+0000"));
	}
	
	public static String toIso8601(Date dateObj) {
		SimpleDateFormat simpleDf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		simpleDf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return simpleDf.format(dateObj).replaceAll("'+0000$","Z");
	}
}
