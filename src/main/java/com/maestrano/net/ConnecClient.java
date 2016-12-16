package com.maestrano.net;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maestrano.ApiService;
import com.maestrano.ConnecService;
import com.maestrano.Maestrano;
import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.exception.InvalidRequestException;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.exception.MnoException;
import com.maestrano.helpers.MnoMapHelper;
import com.maestrano.json.DateDeserializer;
import com.maestrano.json.DateSerializer;
import com.maestrano.json.TimeZoneDeserializer;
import com.maestrano.json.TimeZoneSerializer;

public class ConnecClient {

	public final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).registerTypeAdapter(Date.class, new DateSerializer())
			.registerTypeAdapter(Date.class, new DateDeserializer()).registerTypeAdapter(TimeZone.class, new TimeZoneSerializer()).registerTypeAdapter(TimeZone.class, new TimeZoneDeserializer())
			.create();
	private final ApiService apiService;
	private final ConnecService connecService;

	/**
	 * Instantiate a ConnecClient for the given marketplace
	 * 
	 * @param marketplace
	 * @throws MnoConfigurationException
	 */
	public ConnecClient(String marketplace) throws MnoConfigurationException {
		this(Maestrano.get(marketplace));
	}

	/**
	 * Instantiate a ConnecClient for the given Maestrano configuration
	 * 
	 * @param marketplace
	 * @throws MnoConfigurationException
	 */
	public ConnecClient(Maestrano maestrano) {
		this.connecService = maestrano.connecService();
		this.apiService = maestrano.apiService();
	}

	/**
	 * Instantiate a ConnecClient with the given Preset
	 * 
	 * @deprecated use {@link #ConnecClient(String)} constructor directly
	 * @param marketplace
	 * @return
	 * @throws MnoException
	 */
	public static ConnecClient withPreset(String marketplace) throws MnoConfigurationException {
		return new ConnecClient(marketplace);
	}

	/**
	 * Instantiate a ConnecClient with the default preset
	 * 
	 * @deprecated use {@link #ConnecClient(Maestrano)} constructor directly
	 * @deprecated use
	 * @throws MnoException
	 */
	public static ConnecClient defaultClient() {
		return new ConnecClient(Maestrano.getDefault());
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
		return connecService.getBasePath() + "/" + groupId + "/" + entityName;
	}

	/**
	 * Return the url to the collection endpoint
	 * 
	 * @param entity
	 *            name
	 * @param customerW
	 *            group id
	 * @return collection url
	 */
	public String getCollectionUrl(String entityName, String groupId) {
		return connecService.getHost() + getCollectionEndpoint(entityName, groupId);
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
		return connecService.getHost() + getInstanceEndpoint(entityName, groupId, id);
	}

	/**
	 * Return all the entities
	 * 
	 * @param entity
	 *            name
	 * @param groupId
	 *            customer group id
	 * @return list of entity hashes
	 * @throws MnoException
	 */
	public Map<String, Object> all(String entityName, String groupId) throws MnoException {
		return all(entityName, groupId, null, getAuthenticatedClient());
	}

	/**
	 * Return the entities in a typed class. The typed class should contain a field "entityName" with the list of entity class. For example:
	 * 
	 * <pre>
	* {@code
	* class Organizations{
	*   private List<Organization> organizations;
	*   public List<Organization> getOrganizations(){
	*       return organizations;
	*  }
	* }
	* class Organization{
	* 	public String getId(); 
	*	//etc...
	* }
	 * </pre>
	 * 
	 * @param entityName
	 * @param groupId
	 * @param clazz
	 * @return
	 * @throws MnoException
	 */
	public <T> T all(String entityName, String groupId, Class<T> clazz) throws MnoException {
		return all(entityName, groupId, null, getAuthenticatedClient(), clazz);
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
	 * @throws MnoException
	 */
	public Map<String, Object> all(String entityName, String groupId, Map<String, ?> params) throws MnoException {
		return all(entityName, groupId, params, getAuthenticatedClient());
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
	public <T> T all(String entityName, String groupId, Map<String, ?> params, MnoHttpClient httpClient, Class<T> clazz) throws AuthenticationException, ApiException, InvalidRequestException {
		String jsonBody = httpClient.get(getCollectionUrl(entityName, groupId), MnoMapHelper.toUnderscoreHash(params));
		return GSON.fromJson(jsonBody, clazz);
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
	 * @throws MnoException
	 */
	public Map<String, Object> create(String entityName, String groupId, Map<String, Object> hash) throws MnoException {
		return create(entityName, groupId, hash, getAuthenticatedClient());
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
	 * @throws MnoException
	 */
	public Map<String, Object> retrieve(String entityName, String groupId, String entityId) throws MnoException {
		return retrieve(entityName, groupId, entityId, getAuthenticatedClient());
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
		return update(entityName, groupId, entityId, hash, getAuthenticatedClient());
	}

	/**
	 * apiService Update an entity remotely
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
		return delete(entityName, groupId, entityId, getAuthenticatedClient());
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

	private MnoHttpClient getAuthenticatedClient() {
		return MnoHttpClient.getAuthenticatedClient(apiService);
	}
}
