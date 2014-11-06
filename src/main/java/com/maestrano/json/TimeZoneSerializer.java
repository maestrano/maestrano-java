package com.maestrano.json;

import java.lang.reflect.Type;
import java.util.TimeZone;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class TimeZoneSerializer implements JsonSerializer<TimeZone>{
	public JsonElement serialize(TimeZone src, Type typeOfSrc, JsonSerializationContext context) {
		return src == null ? null : new JsonPrimitive(src.getID());
	}
}
