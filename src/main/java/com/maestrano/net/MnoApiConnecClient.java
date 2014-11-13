package com.maestrano.net;

import java.util.Date;
import java.util.TimeZone;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maestrano.Maestrano;
import com.maestrano.helpers.MnoStringHelper;
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
	
	/**
	 * Return the entity name as expected by Maestrano
	 * @param clazz
	 * @return entity name
	 */
	public static String getEntityName(Class<?> clazz) {
		return MnoStringHelper.toSnakeCase(clazz.getSimpleName()).replaceFirst("^cnc_", "");
	}
	
	/**
	 * Collection version of the entity name
	 * @param entity class
	 * @return pluralized version of entity name
	 */
	public static String getEntitiesName(Class<?> clazz) {
		String name = getEntityName(clazz);
		
		if (name.equals("person")) {
			return "people";
		} else {
			return name + "s";
		}
	}
	
	/**
	 * Return the path to the entity collection endpoint
	 * @param entity class
	 * @return collection endpoint
	 */
	public static String getCollectionEndpoint(Class<?> clazz) {
		return Maestrano.apiService().getConnecBase() + "/" + getEntitiesName(clazz);
	}
	
	/**
	 * Return the url to the collection endpoint
	 * @param entity class
	 * @return collection url
	 */
	public static String getCollectionUrl(Class<?> clazz) {
		return Maestrano.apiService().getConnecHost() + getCollectionEndpoint(clazz);
	}
	
	/**
	 * Return the path to the instance endpoint
	 * @param entity class
	 * @param entity id
	 * @return instance path
	 */
	public static String getInstanceEndpoint(Class<?> clazz, String id) {
		return getCollectionEndpoint(clazz) + "/" + id;
	}
	
	/**
	 * Return the url to the instance endpoint
	 * @param entity class
	 * @param entity id
	 * @return instance url
	 */
	public static String getInstanceUrl(Class<?> clazz, String id) {
		return Maestrano.apiService().getConnecHost() + getInstanceEndpoint(clazz,id);
	}
}
