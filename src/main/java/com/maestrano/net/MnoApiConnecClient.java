package com.maestrano.net;

import java.util.Date;
import java.util.TimeZone;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maestrano.json.DateDeserializer;
import com.maestrano.json.DateSerializer;
import com.maestrano.json.TimeZoneDeserializer;
import com.maestrano.json.TimeZoneSerializer;

public class MnoApiConnecClient {
	public static final Gson GSON = new GsonBuilder()
	.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
	.registerTypeAdapter(Date.class, new DateSerializer())
	.registerTypeAdapter(Date.class, new DateDeserializer())
	.registerTypeAdapter(TimeZone.class, new TimeZoneSerializer())
	.registerTypeAdapter(TimeZone.class, new TimeZoneDeserializer())
	.create();

	public MnoApiConnecClient() {}
}
