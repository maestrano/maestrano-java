package com.maestrano.net;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.maestrano.Maestrano;
import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.exception.InvalidRequestException;
import com.maestrano.helpers.MnoMapHelper;
import com.maestrano.helpers.MnoStringHelper;
import com.maestrano.json.DateDeserializer;
import com.maestrano.json.DateSerializer;
import com.maestrano.reflect.ListParameterizedType;
import com.maestrano.reflect.MnoResponseParameterizedType;

public class MnoApiAccountClient {
	public static final Gson GSON = new GsonBuilder()
		.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
		.registerTypeAdapter(Date.class, new DateSerializer())
		.registerTypeAdapter(Date.class, new DateDeserializer())
		.create();
	
	public MnoApiAccountClient() {}
	
	/**
	 * Return the entity name as expected by Maestrano
	 * @param clazz
	 * @return entity name
	 */
	public static String getEntityName(Class<?> clazz) {
		return MnoStringHelper.toSnakeCase(clazz.getSimpleName()).replaceFirst("^mno_", "");
	}
	
	/**
	 * Collection version of the entity name
	 * @param entity class
	 * @return pluralized version of entity name
	 */
	public static String getEntitiesName(Class<?> clazz) {
		return getEntityName(clazz) + "s";
	}
	
	/**
	 * Return the path to the entity collection endpoint
	 * @param entity class
	 * @return collection endpoint
	 */
	public static String getCollectionEndpoint(Class<?> clazz) {
		return Maestrano.apiService().getAccountBase() + "/" + getEntitiesName(clazz);
	}
	
	/**
	 * Return the url to the collection endpoint
	 * @param entity class
	 * @return collection url
	 */
	public static String getCollectionUrl(Class<?> clazz) {
		return Maestrano.apiService().getHost() + getCollectionEndpoint(clazz);
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
		return Maestrano.apiService().getHost() + getInstanceEndpoint(clazz,id);
	}
	
	/**
	 * Return all the entities 
	 * @param entity class
	 * @return list of entities
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> List<T> all(Class<T> clazz) throws AuthenticationException, ApiException, InvalidRequestException {
		return all(clazz,null,MnoHttpClient.getAuthenticatedClient());
	}
	
	/**
	 * Return all the entities matching the parameters
	 * @param entity class
	 * @param params criteria
	 * @return list of entities
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> List<T> all(Class<T> clazz, Map<String,String> params) throws AuthenticationException, ApiException, InvalidRequestException {
		return all(clazz,params,MnoHttpClient.getAuthenticatedClient());
	}
	
	/**
	 * Return all the entities matching the parameters and using the provided client
	 * @param entity class
	 * @param params criteria
	 * @param httpClient MnoHttpClient to use
	 * @return list of entities
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> List<T> all(Class<T> clazz, Map<String,String> params, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
		String jsonBody = httpClient.get(getCollectionUrl(clazz), params);
		
		Type parsingType = new MnoResponseParameterizedType(new ListParameterizedType(clazz));
		MnoApiAccountResponse<List<T>> resp = GSON.fromJson(jsonBody, parsingType);
		resp.validate();
		
		return resp.getData();
	}
	
	/**
	 * Create an entity remotely
	 * @param entity class
	 * @param entity attributes
	 * @return created entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> T create(Class<T> clazz, Map<String,Object> hash) throws AuthenticationException, ApiException, InvalidRequestException {
		return create(clazz,hash,MnoHttpClient.getAuthenticatedClient());
	}
	
	/**
	 * Create an entity remotely
	 * @param entity class
	 * @param entity attributes
	 * @param httpClient
	 * @return created entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> T create(Class<T> clazz, Map<String,Object> hash, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
		String jsonBody = httpClient.post(getCollectionUrl(clazz), GSON.toJson(MnoMapHelper.toUnderscoreHash(hash)));
		
		Type parsingType = new MnoResponseParameterizedType(clazz);
		MnoApiAccountResponse<T> resp = GSON.fromJson(jsonBody, parsingType);
		resp.validate();
		
		return resp.getData();
	}
	
	/**
	 * Fetch an entity by id
	 * @param entity class
	 * @param entity id
	 * @return entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> T retrieve(Class<T> clazz, String entityId) throws AuthenticationException, ApiException, InvalidRequestException {
		return retrieve(clazz,entityId,MnoHttpClient.getAuthenticatedClient());
	}
	
	/**
	 * Fetch an entity by id
	 * @param entity class
	 * @param entity id
	 * @param httpClient
	 * @return entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> T retrieve(Class<T> clazz, String entityId, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
		String jsonBody = httpClient.get(getInstanceUrl(clazz,entityId));
		
		Type parsingType = new MnoResponseParameterizedType(clazz);
		MnoApiAccountResponse<T> resp = GSON.fromJson(jsonBody, parsingType);
		resp.validate();
		
		return resp.getData();
	}
	
	/**
	 * Update an entity remotely
	 * @param entity class
	 * @param entity id
	 * @param entity attributes to update 
	 * @return updated entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> T update(Class<T> clazz, String entityId, Map<String,Object> hash) throws AuthenticationException, ApiException, InvalidRequestException {
		return update(clazz,entityId,hash,MnoHttpClient.getAuthenticatedClient());
	}
	
	/**
	 * Update an entity remotely
	 * @param entity class
	 * @param entity id
	 * @param entity attributes to update
	 * @param httpClient
	 * @return updated entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> T update(Class<T> clazz, String entityId, Map<String,Object> hash, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
		String jsonBody = httpClient.put(getInstanceUrl(clazz,entityId),GSON.toJson(MnoMapHelper.toUnderscoreHash(hash)));
		
		Type parsingType = new MnoResponseParameterizedType(clazz);
		MnoApiAccountResponse<T> resp = GSON.fromJson(jsonBody, parsingType);
		resp.validate();
		
		return resp.getData();
	}
	
	/**
	 * Delete or cancel an entity remotely 
	 * @param entity class
	 * @param entity id
	 * @return deleted/cancelled entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 */
	public static <T> T delete(Class<T> clazz, String entityId) throws AuthenticationException, ApiException {
		return delete(clazz,entityId,MnoHttpClient.getAuthenticatedClient());
	}
	
	/**
	 * Delete or cancel an entity remotely 
	 * @param entity class
	 * @param entity id
	 * @param httpClient
	 * @return deleted/cancelled entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 */
	public static <T> T delete(Class<T> clazz, String entityId, MnoHttpClient httpClient) throws AuthenticationException, ApiException {
		String jsonBody = httpClient.delete(getInstanceUrl(clazz,entityId));
		
		Type parsingType = new MnoResponseParameterizedType(clazz);
		MnoApiAccountResponse<T> resp = GSON.fromJson(jsonBody, parsingType);
		
		return resp.getData();
	}
}
