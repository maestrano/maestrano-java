package com.maestrano.helpers;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import junit.framework.Assert;

public class MnoDateHelperTest {
	
	Map<Long, String> EXPECTED_CONVERSIONS = ImmutableMap.<Long, String>builder()
			.put(1470832830000L, "2016-08-10T12:40:30Z")
			.put(784023330000L, "1994-11-05T08:15:30Z")
			.put(784023325000L, "1994-11-05T08:15:25Z")
			.put(784023541000L, "1994-11-05T08:19:01Z")
			.put(26641060000L, "1970-11-05T08:17:40Z")
			.put(-1261928200000L, "1930-01-05T08:23:20Z")
			.put(495835680000L, "1985-09-17T20:08:00Z")
			.build();

	@Test
	public void fromIso8601_itReturnsTheRightDate() throws ParseException {
		for (Entry<Long, String> entry : EXPECTED_CONVERSIONS.entrySet()) {
			Date date = MnoDateHelper.fromIso8601(entry.getValue());
			Assert.assertEquals(entry.getKey().longValue(), date.getTime());
		}

	}

	@Test
	public void toIso8601_itReturnsTheRightDate() {
		for (Entry<Long, String> entry : EXPECTED_CONVERSIONS.entrySet()) {
			String iso8601 = MnoDateHelper.toIso8601(new Date(entry.getKey()));
			Assert.assertEquals(entry.getValue(), iso8601);
		}
	}
}
