package com.maestrano.json;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.maestrano.helpers.MnoDateHelper;

public class DateSerializer implements JsonSerializer<Date> {

	public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
		return src == null ? null : new JsonPrimitive(MnoDateHelper.toIso8601(src));
	}

}
