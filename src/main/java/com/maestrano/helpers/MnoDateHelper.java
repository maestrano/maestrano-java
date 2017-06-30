package com.maestrano.helpers;

import java.text.ParseException;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class MnoDateHelper {

	public static Date fromIso8601(String dateStr) throws ParseException {
		return DateTime.parse(dateStr).toDate();
	}

	public static String toIso8601(Date dateObj) {
		// Joda Time toString is by default Iso 8601
		// http://joda-time.sourceforge.net/apidocs/org/joda/time/base/AbstractInstant.html#toString%28%29
		return new DateTime(dateObj, DateTimeZone.UTC).toString();
	}
}
