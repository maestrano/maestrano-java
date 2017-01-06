package com.maestrano.net;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maestrano.Maestrano;
import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.exception.InvalidRequestException;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.helpers.MnoMapHelper;
import com.maestrano.helpers.MnoStringHelper;
import com.maestrano.json.DateDeserializer;
import com.maestrano.json.DateSerializer;
import com.maestrano.reflect.ListParameterizedType;
import com.maestrano.reflect.MnoAccountResponseParameterizedType;

/**
 * service to retrieve Maestrano Business Objects using maestrano API
 */
public class MnoAccountClient<T> {

	public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).registerTypeAdapter(Date.class, new DateSerializer())
			.registerTypeAdapter(Date.class, new DateDeserializer()).create();

	public final static String CTYPE = "application/json";
	private final Maestrano maestrano;
	private final Class<T> entitityClass;

	/**
	 * @deprecated
	 * @param entitityClass
	 */
	protected MnoAccountClient(Class<T> entitityClass) {
		this.entitityClass = entitityClass;
		this.maestrano = Maestrano.getDefault();
	}

	protected MnoAccountClient(Class<T> entitityClass, Maestrano maestrano){
		this.entitityClass = entitityClass;
		this.maestrano = maestrano;
	}

	/**
	 * Return the entity name as expected by maestrano
	 * 
	 * @param entitityClass
	 * @return entity name
	 */
	public String getEntityName() {
		return MnoStringHelper.toSnakeCase(entitityClass.getSimpleName()).replaceFirst("^mno_", "");
	}

	/**
	 * Collection version of the entity name
	 * 
	 * @param entity
	 *            class
	 * @return pluralized version of entity name
	 */
	public String getEntitiesName() {
		return getEntityName() + "s";
	}

	/**
	 * Return the path to the entity collection endpoint
	 * 
	 * @param entity
	 *            class
	 * @return collection endpoint
	 */
	public String getCollectionEndpoint() {
		return maestrano.apiService().getBase() + "account/" + getEntitiesName();
	}

	/**
	 * Return the url to the collection endpoint
	 * 
	 * @param entity
	 *            class
	 * @return collection url
	 */
	public String getCollectionUrl() {
		return maestrano.apiService().getHost() + getCollectionEndpoint();
	}

	/**
	 * Return the path to the instance endpoint
	 * 
	 * @param entity
	 *            class
	 * @param entity
	 *            id
	 * @return instance path
	 */
	public String getInstanceEndpoint(String id) {
		return getCollectionEndpoint() + "/" + id;
	}

	/**
	 * Return the url to the instance endpoint
	 * 
	 * @param entity
	 *            class
	 * @param entity
	 *            id
	 * @return instance url
	 */
	public String getInstanceUrl(String id) {
		return maestrano.apiService().getHost() + getInstanceEndpoint(id);
	}

	/**
	 * Return all the entities
	 * 
	 * @param entity
	 *            class
	 * @return list of entities
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public List<T> all() throws AuthenticationException, ApiException, InvalidRequestException {
		return all(null, getAuthenticatedClient());
	}

	/**
	 * Return all the entities matching the parameters
	 * 
	 * @param <V>
	 * @param entity
	 *            class
	 * @param params
	 *            criteria
	 * @return list of entities
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public List<T> all(Map<String, ?> params) throws AuthenticationException, ApiException, InvalidRequestException {
		return all(params, getAuthenticatedClient());
	}

	/**
	 * Return all the entities matching the parameters and using the provided client
	 * 
	 * @param <V>
	 * @param entity
	 *            class
	 * @param params
	 *            criteria
	 * @param httpClient
	 *            MnoHttpClient to use
	 * @return list of entities
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public List<T> all(Map<String, ?> params, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
		String jsonBody = httpClient.get(getCollectionUrl(), MnoMapHelper.toUnderscoreHash(params));

		Type parsingType = new MnoAccountResponseParameterizedType(new ListParameterizedType(entitityClass));
		MnoAccountResponse<List<T>> resp = GSON.fromJson(jsonBody, parsingType);
		resp.validate();

		return resp.getData();
	}

	/**
	 * Create an entity remotely
	 * 
	 * @param entity
	 *            class
	 * @param entity
	 *            attributes
	 * @return created entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public T create(Map<String, Object> hash) throws AuthenticationException, ApiException, InvalidRequestException {
		return create(hash, getAuthenticatedClient());
	}

	/**
	 * Create an entity remotely
	 * 
	 * @param entity
	 *            class
	 * @param entity
	 *            attributes
	 * @param httpClient
	 * @return created entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public T create(Map<String, Object> hash, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
		String jsonBody = httpClient.post(getCollectionUrl(), GSON.toJson(MnoMapHelper.toUnderscoreHash(hash)));

		Type parsingType = new MnoAccountResponseParameterizedType(entitityClass);
		MnoAccountResponse<T> resp = GSON.fromJson(jsonBody, parsingType);
		resp.validate();

		return resp.getData();
	}

	/**
	 * Fetch an entity by id
	 * 
	 * @param entity
	 *            class
	 * @param entity
	 *            id
	 * @return entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public T retrieve(String entityId) throws AuthenticationException, ApiException, InvalidRequestException {
		return retrieve(entityId, getAuthenticatedClient());
	}

	/**
	 * Fetch an entity by id
	 * 
	 * @param entity
	 *            class
	 * @param entity
	 *            id
	 * @param httpClient
	 * @return entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public T retrieve(String entityId, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
		String jsonBody = httpClient.get(getInstanceUrl(entityId));

		Type parsingType = new MnoAccountResponseParameterizedType(entitityClass);
		MnoAccountResponse<T> resp = GSON.fromJson(jsonBody, parsingType);
		resp.validate();

		return resp.getData();
	}

	/**
	 * Update an entity remotely
	 * 
	 * @param entity
	 *            class
	 * @param entity
	 *            id
	 * @param entity
	 *            attributes to update
	 * @return updated entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public T update(String entityId, Map<String, Object> hash) throws AuthenticationException, ApiException, InvalidRequestException {
		return update(entityId, hash, getAuthenticatedClient());
	}

	/**
	 * Update an entity remotely
	 * 
	 * @param entity
	 *            class
	 * @param entity
	 *            id
	 * @param entity
	 *            attributes to update
	 * @param httpClient
	 * @return updated entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public T update(String entityId, Map<String, Object> hash, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
		String jsonBody = httpClient.put(getInstanceUrl(entityId), GSON.toJson(MnoMapHelper.toUnderscoreHash(hash)));

		Type parsingType = new MnoAccountResponseParameterizedType(entitityClass);
		MnoAccountResponse<T> resp = GSON.fromJson(jsonBody, parsingType);
		resp.validate();

		return resp.getData();
	}

	/**
	 * Delete or cancel an entity remotely
	 * 
	 * @param entity
	 *            class
	 * @param entity
	 *            id
	 * @return deleted/cancelled entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 */
	public T delete(String entityId) throws AuthenticationException, ApiException {
		return delete(entityId, getAuthenticatedClient());
	}

	/**
	 * Delete or cancel an entity remotely
	 * 
	 * @param entity
	 *            class
	 * @param entity
	 *            id
	 * @param httpClient
	 * @return deleted/cancelled entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 */
	public T delete(String entityId, MnoHttpClient httpClient) throws AuthenticationException, ApiException {
		String jsonBody = httpClient.delete(getInstanceUrl(entityId));

		Type parsingType = new MnoAccountResponseParameterizedType(entitityClass);
		MnoAccountResponse<T> resp = GSON.fromJson(jsonBody, parsingType);

		return resp.getData();
	}

	private MnoHttpClient getAuthenticatedClient() {
		return MnoHttpClient.getAuthenticatedClient(maestrano.apiService(), CTYPE);
	}
}
