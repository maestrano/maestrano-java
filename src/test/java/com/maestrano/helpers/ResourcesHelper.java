package com.maestrano.helpers;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.maestrano.account.RecurringBillTest;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class ResourcesHelper {

	public static String getResource(String path) {
		try {
			return Resources.toString(RecurringBillTest.class.getResource(path), Charsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Could not get resource " + path, e);
		}
	}

	public static Date parseDate(String string) { // TODO Auto-generated method stub
		try {
			return MnoDateHelper.fromIso8601(string);
		} catch (ParseException e) {
			throw new RuntimeException("Could not parse " + string, e);
		}
	}
}
