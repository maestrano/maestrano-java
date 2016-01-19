package com.maestrano.net;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
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
import com.maestrano.json.DateDeserializer;
import com.maestrano.json.DateSerializer;
import com.maestrano.json.TimeZoneDeserializer;
import com.maestrano.json.TimeZoneSerializer;

public class ConnecClient {

	public final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).registerTypeAdapter(Date.class, new DateSerializer())
			.registerTypeAdapter(Date.class, new DateDeserializer())
			.registerTypeAdapter(TimeZone.class, new TimeZoneSerializer())
			.registerTypeAdapter(TimeZone.class, new TimeZoneDeserializer())
			.create();
	private final String preset;

	private ConnecClient(String preset) {
		this.preset = preset;
	}

	/**
	 * Instantiate a ConnecClient with the given Preset
	 * 
	 * @param preset
	 * @return
	 */
	public static ConnecClient withPreset(String preset) {
		return new ConnecClient(preset);
	}

	/**
	 * Instantiate a ConnecClient with the default preset: default
	 */
	public static ConnecClient defaultClient() {
		return withPreset("default");
	}

	/**
	 * Return the path to the entity collection endpoint
	 * 
	 * @param entity
	 *            name
	 * @param customer
	 *            group id
	 * @return collection endpoint
	 */
	public String getCollectionEndpoint(String entityName, String groupId) {
		return Maestrano.apiService().getConnecBase() + "/" + groupId + "/" + entityName;
	}

	/**
	 * Return the url to the collection endpoint
	 * 
	 * @param entity
	 *            name
	 * @param customer
	 *            group id
	 * @return collection url
	 */
	public String getCollectionUrl(String entityName, String groupId) {
		return Maestrano.apiService().getConnecHost(preset) + getCollectionEndpoint(entityName, groupId);
	}

	/**
	 * Return the path to the instance endpoint
	 * 
	 * @param entity
	 *            name
	 * @param customer
	 *            group id
	 * @param entity
	 *            id
	 * @return instance path
	 */
	public String getInstanceEndpoint(String entityName, String groupId, String id) {
		String edp = getCollectionEndpoint(entityName, groupId);

		if (id != null && !id.isEmpty()) {
			edp += "/" + id;
		}

		return edp;
	}

	/**
	 * Return the url to the instance endpoint
	 * 
	 * @param entity
	 *            name
	 * @param customer
	 *            group id
	 * @param entity
	 *            id
	 * @return instance url
	 */
	public String getInstanceUrl(String entityName, String groupId, String id) {
		return Maestrano.apiService().getConnecHost(preset) + getInstanceEndpoint(entityName, groupId, id);
	}

	/**
	 * Return all the entities
	 * 
	 * @param entity
	 *            name
	 * @param groupId
	 *            customer group id
	 * @return list of entity hashes
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public Map<String, Object> all(String entityName, String groupId) throws AuthenticationException, ApiException, InvalidRequestException {
		return all(entityName, groupId, null, MnoHttpClient.getAuthenticatedClient(preset));
	}

	/**
	 * Return all the entities matching the parameters
	 * 
	 * @param <V>
	 * @param entity
	 *            name
	 * @param groupId
	 *            customer group id
	 * @param params
	 *            criteria
	 * @return list of entities
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public Map<String, Object> all(String entityName, String groupId, Map<String, ?> params) throws AuthenticationException, ApiException, InvalidRequestException {
		return all(entityName, groupId, params, MnoHttpClient.getAuthenticatedClient(preset));
	}

	/**
	 * Return all the entities matching the parameters and using the provided client
	 * 
	 * @param entity
	 *            name
	 * @param groupId
	 *            customer group id
	 * @param params
	 *            criteria
	 * @param httpClient
	 *            MnoHttpClient to use
	 * @return list of entities
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public Map<String, Object> all(String entityName, String groupId, Map<String, ?> params, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
		String jsonBody = httpClient.get(getCollectionUrl(entityName, groupId), MnoMapHelper.toUnderscoreHash(params));
		Type typeOfHashMap = HashMap.class;
		return GSON.fromJson(jsonBody, typeOfHashMap);
	}

	/**
	 * Create an entity remotely
	 * 
	 * @param entity
	 *            name
	 * @param groupId
	 *            customer group id
	 * @param hash
	 *            entity attributes
	 * @return created entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public Map<String, Object> create(String entityName, String groupId, Map<String, Object> hash) throws AuthenticationException, ApiException, InvalidRequestException {
		return create(entityName, groupId, hash, MnoHttpClient.getAuthenticatedClient(preset));
	}

	/**
	 * Create an entity remotely
	 * 
	 * @param entity
	 *            name
	 * @param groupId
	 *            customer group id
	 * @param hash
	 *            entity attributes
	 * @param httpClient
	 * @return created entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public Map<String, Object> create(String entityName, String groupId, Map<String, Object> hash, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
		Map<String, Object> envelope = new HashMap<String, Object>();
		envelope.put(entityName, MnoMapHelper.toUnderscoreHash(hash));
		envelope.put("resource", entityName);
		String payload = GSON.toJson(envelope);

		return create(entityName, groupId, payload, httpClient);
	}

	/**
	 * Create an entity remotely
	 * 
	 * @param entity
	 *            name
	 * @param groupId
	 *            customer group id
	 * @param jsonStr
	 *            attributes as json string
	 * @param httpClient
	 * @return created entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public Map<String, Object> create(String entityName, String groupId, String jsonStr, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
		String jsonBody = httpClient.post(getCollectionUrl(entityName, groupId), jsonStr);
		Type typeOfHashMap = HashMap.class;
		return GSON.fromJson(jsonBody, typeOfHashMap);
	}

	/**
	 * Fetch an entity by id
	 * 
	 * @param entity
	 *            name
	 * @param groupId
	 *            customer group id
	 * @param entityId
	 *            id of the entity to retrieve
	 * @return entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public Map<String, Object> retrieve(String entityName, String groupId, String entityId) throws AuthenticationException, ApiException, InvalidRequestException {
		return retrieve(entityName, groupId, entityId, MnoHttpClient.getAuthenticatedClient(preset));
	}

	/**
	 * Fetch an entity by id
	 * 
	 * @param entity
	 *            name
	 * @param groupId
	 *            customer group id
	 * @param entityId
	 *            id of the entity to retrieve
	 * @param httpClient
	 * @return entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public Map<String, Object> retrieve(String entityName, String groupId, String entityId, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
		String jsonBody = httpClient.get(getInstanceUrl(entityName, groupId, entityId));
		Type typeOfHashMap = HashMap.class;
		return GSON.fromJson(jsonBody, typeOfHashMap);
	}

	/**
	 * Update an entity remotely
	 * 
	 * @param entity
	 *            name
	 * @param groupId
	 *            customer group id
	 * @param entityId
	 *            id of the entity to retrieve
	 * @param hash
	 *            entity attributes to update
	 * @return updated entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public Map<String, Object> update(String entityName, String groupId, String entityId, Map<String, Object> hash) throws AuthenticationException, ApiException, InvalidRequestException {
		return update(entityName, groupId, entityId, hash, MnoHttpClient.getAuthenticatedClient(preset));
	}

	/**
	 * Update an entity remotely
	 * 
	 * @param entity
	 *            name
	 * @param groupId
	 *            customer group id
	 * @param entityId
	 *            id of the entity to retrieve
	 * @param hash
	 *            entity attributes to update
	 * @param httpClient
	 * @return updated entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public Map<String, Object> update(String entityName, String groupId, String entityId, Map<String, Object> hash, MnoHttpClient httpClient)
			throws AuthenticationException, ApiException, InvalidRequestException {
		Map<String, Object> envelope = new HashMap<String, Object>();
		envelope.put(entityName, MnoMapHelper.toUnderscoreHash(hash));
		envelope.put("resource", entityName);
		String payload = GSON.toJson(envelope);

		return update(entityName, groupId, entityId, payload, httpClient);
	}

	/**
	 * Update an entity remotely
	 * 
	 * @param entity
	 *            name
	 * @param groupId
	 *            customer group id
	 * @param entityId
	 *            id of the entity to retrieve
	 * @param jsonStr
	 *            entity attributes to update
	 * @param httpClient
	 * @return updated entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public Map<String, Object> update(String entityName, String groupId, String entityId, String jsonStr, MnoHttpClient httpClient)
			throws AuthenticationException, ApiException, InvalidRequestException {
		String jsonBody = httpClient.put(getInstanceUrl(entityName, groupId, entityId), jsonStr);
		Type typeOfHashMap = HashMap.class;
		return GSON.fromJson(jsonBody, typeOfHashMap);
	}

	/**
	 * Delete or cancel an entity remotely
	 * 
	 * @param entity
	 *            name
	 * @param groupId
	 *            customer group id
	 * @param entityId
	 *            id of the entity to delete
	 * @return deleted/cancelled entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 */
	public Map<String, Object> delete(String entityName, String groupId, String entityId) throws AuthenticationException, ApiException {
		return delete(entityName, groupId, entityId, MnoHttpClient.getAuthenticatedClient(preset));
	}

	/**
	 * Delete or cancel an entity remotely
	 * 
	 * @param entity
	 *            name
	 * @param groupId
	 *            customer group id
	 * @param entityId
	 *            id of the entity to delete
	 * @param httpClient
	 * @return deleted/cancelled entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 */
	public Map<String, Object> delete(String entityName, String groupId, String entityId, MnoHttpClient httpClient) throws AuthenticationException, ApiException {
		String jsonBody = httpClient.delete(getInstanceUrl(entityName, groupId, entityId));
		Type typeOfHashMap = HashMap.class;
		return GSON.fromJson(jsonBody, typeOfHashMap);
	}
}
