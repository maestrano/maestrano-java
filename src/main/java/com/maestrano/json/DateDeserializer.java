package com.maestrano.json;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.maestrano.helpers.MnoDateHelper;

public class DateDeserializer implements JsonDeserializer<Date>{

	public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		try {
			return json == null ? null : MnoDateHelper.fromIso8601(json.getAsString());
		} catch (ParseException e) {}
		return null;
	}

}
