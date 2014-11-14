package com.maestrano.net;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maestrano.Maestrano;
import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.exception.InvalidRequestException;
import com.maestrano.helpers.MnoMapHelper;
import com.maestrano.helpers.MnoStringHelper;
import com.maestrano.json.DateDeserializer;
import com.maestrano.json.DateSerializer;
import com.maestrano.json.TimeZoneDeserializer;
import com.maestrano.json.TimeZoneSerializer;
import com.maestrano.reflect.ConnecResponseParameterizedType;

public class ConnecClient {
	public static final Gson GSON = new GsonBuilder()
	.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
	.registerTypeAdapter(Date.class, new DateSerializer())
	.registerTypeAdapter(Date.class, new DateDeserializer())
	.registerTypeAdapter(TimeZone.class, new TimeZoneSerializer())
	.registerTypeAdapter(TimeZone.class, new TimeZoneDeserializer())
	.create();

	public ConnecClient() {}
	
	/**
	 * Return the entity name as expected by Maestrano
	 * @param clazz
	 * @return entity name
	 */
	public static String getEntityName(Class<?> clazz) {
		return MnoStringHelper.toSnakeCase(clazz.getSimpleName()).replaceFirst("^cn_", "");
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
		} else if (name.equals("company")) {
			return "company";
		} else {
			return name + "s";
		}
	}
	
	/**
	 * Return the path to the entity collection endpoint
	 * @param entity class
	 * @param customer group id
	 * @return collection endpoint
	 */
	public static String getCollectionEndpoint(Class<?> clazz, String groupId) {
		return Maestrano.apiService().getConnecBase() + "/" + groupId + "/" + getEntitiesName(clazz);
	}
	
	/**
	 * Return the url to the collection endpoint
	 * @param entity class
	 * @param customer group id
	 * @return collection url
	 */
	public static String getCollectionUrl(Class<?> clazz, String groupId) {
		return Maestrano.apiService().getConnecHost() + getCollectionEndpoint(clazz,groupId);
	}
	
	/**
	 * Return the path to the instance endpoint
	 * @param entity class
	 * @param customer group id
	 * @param entity id
	 * @return instance path
	 */
	public static String getInstanceEndpoint(Class<?> clazz, String groupId, String id) {
		String edp = getCollectionEndpoint(clazz,groupId);
		
		if (id != null && !id.isEmpty()) {
			edp += "/" + id;
		}
		
		return edp;
	}
	
	/**
	 * Return the url to the instance endpoint
	 * @param entity class
	 * @param customer group id
	 * @param entity id
	 * @return instance url
	 */
	public static String getInstanceUrl(Class<?> clazz, String groupId, String id) {
		return Maestrano.apiService().getConnecHost() + getInstanceEndpoint(clazz,groupId,id);
	}
	
	/**
	 * Return all the entities 
	 * @param clazz entity class
	 * @param groupId customer group id
	 * @return list of entities
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> List<T> all(Class<T> clazz, String groupId) throws AuthenticationException, ApiException, InvalidRequestException {
		return all(clazz,groupId,null,MnoHttpClient.getAuthenticatedClient());
	}
	
	/**
	 * Return all the entities matching the parameters
	 * @param <V>
	 * @param clazz entity class
	 * @param groupId customer group id
	 * @param params criteria
	 * @return list of entities
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T, V> List<T> all(Class<T> clazz, String groupId, Map<String,V> params) throws AuthenticationException, ApiException, InvalidRequestException {
		return all(clazz,groupId,params,MnoHttpClient.getAuthenticatedClient());
	}
	
	/**
	 * Return all the entities matching the parameters and using the provided client
	 * @param <V>
	 * @param clazz entity class
	 * @param groupId customer group id
	 * @param params criteria
	 * @param httpClient MnoHttpClient to use
	 * @return list of entities
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T, V> List<T> all(Class<T> clazz, String groupId, Map<String,V> params, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
		String jsonBody = httpClient.get(getCollectionUrl(clazz,groupId), MnoMapHelper.toUnderscoreHash(params));
		
		Type parsingType = new ConnecResponseParameterizedType(clazz);
		ConnecResponse<T> resp = GSON.fromJson(jsonBody, parsingType);
		
		return resp.getEntities();
	}
	
	/**
	 * Create an entity remotely
	 * @param clazz entity class
	 * @param groupId customer group id
	 * @param hash entity attributes
	 * @return created entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> T create(Class<T> clazz, String groupId, Map<String,Object> hash) throws AuthenticationException, ApiException, InvalidRequestException {
		return create(clazz,groupId,hash,MnoHttpClient.getAuthenticatedClient());
	}
	
	/**
	 * Create an entity remotely
	 * @param clazz entity class
	 * @param groupId customer group id
	 * @param hash entity attributes
	 * @param httpClient
	 * @return created entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> T create(Class<T> clazz, String groupId, Map<String,Object> hash, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
		Map<String,Map<String,Object>> envelope = new HashMap<String,Map<String,Object>>();
		envelope.put("entity",MnoMapHelper.toUnderscoreHash(hash));
		String payload = GSON.toJson(envelope);
		
		String jsonBody = httpClient.post(getCollectionUrl(clazz,groupId), payload);
		
		Type parsingType = new ConnecResponseParameterizedType(clazz);
		ConnecResponse<T> resp = GSON.fromJson(jsonBody, parsingType);
		
		return resp.getEntity();
	}
	
	/**
	 * Fetch an entity by id
	 * @param clazz entity class
	 * @param groupId customer group id
	 * @param entityId id of the entity to retrieve
	 * @return entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> T retrieve(Class<T> clazz, String groupId, String entityId) throws AuthenticationException, ApiException, InvalidRequestException {
		return retrieve(clazz,groupId,entityId,MnoHttpClient.getAuthenticatedClient());
	}
	
	/**
	 * Fetch an entity by id
	 * @param clazz entity class
	 * @param groupId customer group id
	 * @param entityId id of the entity to retrieve
	 * @param httpClient
	 * @return entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> T retrieve(Class<T> clazz, String groupId, String entityId, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
		String jsonBody = httpClient.get(getInstanceUrl(clazz,groupId,entityId));
		
		Type parsingType = new ConnecResponseParameterizedType(clazz);
		ConnecResponse<T> resp = GSON.fromJson(jsonBody, parsingType);
		
		return resp.getEntity();
	}
	
	/**
	 * Update an entity remotely
	 * @param clazz entity class
	 * @param groupId customer group id
	 * @param entityId id of the entity to retrieve
	 * @param hash entity attributes to update 
	 * @return updated entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> T update(Class<T> clazz, String groupId, String entityId, T obj) throws AuthenticationException, ApiException, InvalidRequestException {
		Map<String,T> envelope = new HashMap<String,T>();
		envelope.put("entity",obj);
		String payload = GSON.toJson(envelope);
		
		return update(clazz,groupId,entityId,payload,MnoHttpClient.getAuthenticatedClient());
	}
	
	/**
	 * Update an entity remotely
	 * @param clazz entity class
	 * @param groupId customer group id
	 * @param entityId id of the entity to retrieve
	 * @param hash entity attributes to update 
	 * @return updated entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> T update(Class<T> clazz, String groupId, String entityId, Map<String,Object> hash) throws AuthenticationException, ApiException, InvalidRequestException {
		return update(clazz,groupId,entityId,hash,MnoHttpClient.getAuthenticatedClient());
	}
	
	/**
	 * Update an entity remotely
	 * @param clazz entity class
	 * @param groupId customer group id
	 * @param entityId id of the entity to retrieve
	 * @param hash entity attributes to update 
	 * @param httpClient
	 * @return updated entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> T update(Class<T> clazz, String groupId, String entityId, Map<String,Object> hash, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
		Map<String,Map<String,Object>> envelope = new HashMap<String,Map<String,Object>>();
		envelope.put("entity",MnoMapHelper.toUnderscoreHash(hash));
		String payload = GSON.toJson(envelope);
		
		return update(clazz,groupId,entityId,payload,httpClient);
	}
	
	/**
	 * Update an entity remotely
	 * @param clazz entity class
	 * @param groupId customer group id
	 * @param entityId id of the entity to retrieve
	 * @param jsonStr entity attributes to update 
	 * @param httpClient
	 * @return updated entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> T update(Class<T> clazz, String groupId, String entityId, String jsonStr, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
		String jsonBody = httpClient.put(getInstanceUrl(clazz,groupId,entityId),jsonStr);
		
		Type parsingType = new ConnecResponseParameterizedType(clazz);
		ConnecResponse<T> resp = GSON.fromJson(jsonBody, parsingType);
		
		return resp.getEntity();
	}
	
	
	/**
	 * Delete or cancel an entity remotely 
	 * @param clazz entity class
	 * @param groupId customer group id
	 * @param entityId id of the entity to delete 
	 * @return deleted/cancelled entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 */
	public static <T> T delete(Class<T> clazz, String groupId, String entityId) throws AuthenticationException, ApiException {
		return delete(clazz,groupId,entityId,MnoHttpClient.getAuthenticatedClient());
	}
	
	/**
	 * Delete or cancel an entity remotely 
	 * @param clazz entity class
	 * @param groupId customer group id
	 * @param entityId id of the entity to delete 
	 * @param httpClient
	 * @return deleted/cancelled entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 */
	public static <T> T delete(Class<T> clazz, String groupId, String entityId, MnoHttpClient httpClient) throws AuthenticationException, ApiException {
		String jsonBody = httpClient.delete(getInstanceUrl(clazz,groupId,entityId));
		
		Type parsingType = new ConnecResponseParameterizedType(clazz);
		ConnecResponse<T> resp = GSON.fromJson(jsonBody, parsingType);
		
		return resp.getEntity();
	}
}
