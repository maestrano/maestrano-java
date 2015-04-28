package com.maestrano.net;

import java.lang.reflect.Type;
import java.util.ArrayList;
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

	private ConnecClient() {}
	
	/**
	 * Return the path to the entity collection endpoint
	 * @param entity name
	 * @param customer group id
	 * @return collection endpoint
	 */
	public static String getCollectionEndpoint(String entityName, String groupId) {
		return Maestrano.apiService().getConnecBase() + "/" + groupId + "/" + entityName;
	}
	
	/**
	 * Return the url to the collection endpoint
	 * @param entity name
	 * @param customer group id
	 * @return collection url
	 */
	public static String getCollectionUrl(String entityName, String groupId) {
		return Maestrano.apiService().getConnecHost() + getCollectionEndpoint(entityName,groupId);
	}
	
	/**
	 * Return the path to the instance endpoint
	 * @param entity name
	 * @param customer group id
	 * @param entity id
	 * @return instance path
	 */
	public static String getInstanceEndpoint(String entityName, String groupId, String id) {
		String edp = getCollectionEndpoint(entityName,groupId);
		
		if (id != null && !id.isEmpty()) {
			edp += "/" + id;
		}
		
		return edp;
	}
	
	/**
	 * Return the url to the instance endpoint
	 * @param entity name
	 * @param customer group id
	 * @param entity id
	 * @return instance url
	 */
	public static String getInstanceUrl(String entityName, String groupId, String id) {
		return Maestrano.apiService().getConnecHost() + getInstanceEndpoint(entityName,groupId,id);
	}
	
	/**
	 * Return all the entities 
	 * @param entity name
	 * @param groupId customer group id
	 * @return list of entity hashes
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static Map<String, Object> all(String entityName, String groupId) throws AuthenticationException, ApiException, InvalidRequestException {
		return all(entityName,groupId,null,MnoHttpClient.getAuthenticatedClient());
	}
	
	/**
     * Return all the entities 
     * @param entity name
     * @param groupId customer group id
     * @param Connec serializer class
     * @return list of entity hashes
     * @throws AuthenticationException
     * @throws ApiException
     * @throws InvalidRequestException
     */
    public static <T> List<T> all(String entityName, String groupId, Class<T> clazz) throws AuthenticationException, ApiException, InvalidRequestException {
        return all(entityName,groupId,null,MnoHttpClient.getAuthenticatedClient(),clazz);
    }
	
	/**
	 * Return all the entities matching the parameters
	 * @param <V>
	 * @param entity name
	 * @param groupId customer group id
	 * @param params criteria
	 * @return list of entities
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static Map<String, Object> all(String entityName, String groupId, Map<String,?> params) throws AuthenticationException, ApiException, InvalidRequestException {
		return all(entityName,groupId,params,MnoHttpClient.getAuthenticatedClient());
	}
	
	/**
     * Return all the entities matching the parameters and using the provided client
     * @param entity name
     * @param groupId customer group id
     * @param params criteria
     * @param httpClient MnoHttpClient to use
     * @return list of entities
     * @throws AuthenticationException
     * @throws ApiException
     * @throws InvalidRequestException
     */
    public static Map<String, Object> all(String entityName, String groupId, Map<String,?> params, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
        String jsonBody = httpClient.get(getCollectionUrl(entityName,groupId), MnoMapHelper.toUnderscoreHash(params));
        Type typeOfHashMap = HashMap.class;
        return GSON.fromJson(jsonBody, typeOfHashMap);
    }
	
	/**
	 * Return all the entities matching the parameters and using the provided client
	 * @param <T>
	 * @param entity name
	 * @param groupId customer group id
	 * @param params criteria
	 * @param httpClient MnoHttpClient to use
	 * @param Connec! serializer class
	 * @return list of entities
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> List<T> all(String entityName, String groupId, Map<String,?> params, MnoHttpClient httpClient, Class<T> clazz) throws AuthenticationException, ApiException, InvalidRequestException {
		String jsonBody = httpClient.get(getCollectionUrl(entityName,groupId), MnoMapHelper.toUnderscoreHash(params));

		return deserializeEntities(entityName, jsonBody, clazz);
	}
	
	/**
	 * Create an entity remotely from an object
	 * @param entity name
	 * @param groupId customer group id
	 * @param Connec resource
	 * @return
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> T create(String entityName, String groupId, T obj) throws AuthenticationException, ApiException, InvalidRequestException {
		Map<String,Object> envelope = new HashMap<String,Object>();
		envelope.put(entityName,obj);
		envelope.put("resource",entityName);
		String payload = GSON.toJson(envelope);
		
		return (T) create(entityName,groupId,payload,MnoHttpClient.getAuthenticatedClient(),obj.getClass());
	}
	
	/**
     * Create an entity remotely
     * @param entity name
     * @param groupId customer group id
     * @param hash entity attributes
     * @return created entity
     * @throws AuthenticationException
     * @throws ApiException
     * @throws InvalidRequestException
     */
    public static Map<String,Object> create(String entityName, String groupId, Map<String,Object> hash) throws AuthenticationException, ApiException, InvalidRequestException {     
        return create(entityName,groupId,hash,MnoHttpClient.getAuthenticatedClient());
    }
	
	/**
	 * Create an entity remotely
	 * @param entity name
	 * @param groupId customer group id
	 * @param hash entity attributes
	 * @param Connec serializer class
	 * @return created entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> T create(String entityName, String groupId, Map<String,Object> hash, Class<T> clazz) throws AuthenticationException, ApiException, InvalidRequestException {		
		return create(entityName,groupId,hash,MnoHttpClient.getAuthenticatedClient(),clazz);
	}
	
	/**
	 * Create an entity remotely
	 * @param entity name
	 * @param groupId customer group id
	 * @param hash entity attributes
	 * @param httpClient
	 * @return created entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static Map<String,Object> create(String entityName, String groupId, Map<String,Object> hash, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
		Map<String, Object> envelope = new HashMap<String,Object>();
		envelope.put(entityName,MnoMapHelper.toUnderscoreHash(hash));
		envelope.put("resource",entityName);
		String payload = GSON.toJson(envelope);
		
		return create(entityName,groupId,payload,httpClient);
	}
	
	/**
     * Create an entity remotely
     * @param entity name
     * @param groupId customer group id
     * @param hash entity attributes
     * @param httpClient
     * @param Connec serializer class
     * @return created entity
     * @throws AuthenticationException
     * @throws ApiException
     * @throws InvalidRequestException
     */
    public static <T> T create(String entityName, String groupId, Map<String,Object> hash, MnoHttpClient httpClient, Class<T> clazz) throws AuthenticationException, ApiException, InvalidRequestException {
        Map<String,Object> envelope = new HashMap<String,Object>();
        envelope.put(entityName,MnoMapHelper.toUnderscoreHash(hash));
        envelope.put("resource",entityName);
        String payload = GSON.toJson(envelope);
        return create(entityName,groupId,payload,httpClient,clazz);
    }
	
	/**
     * Create an entity remotely
     * @param entity name
     * @param groupId customer group id
     * @param jsonStr attributes as json string
     * @param httpClient
     * @return created entity
     * @throws AuthenticationException
     * @throws ApiException
     * @throws InvalidRequestException
     */
    public static Map<String,Object> create(String entityName, String groupId, String jsonStr, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {        
        String jsonBody = httpClient.post(getCollectionUrl(entityName,groupId), jsonStr);
        Type typeOfHashMap = HashMap.class;
        return GSON.fromJson(jsonBody, typeOfHashMap);
    }
    
    /**
     * Create an entity remotely
     * @param entity name
     * @param groupId customer group id
     * @param jsonStr attributes as json string
     * @param httpClient
     * @param Connec serializer class
     * @return created entity
     * @throws AuthenticationException
     * @throws ApiException
     * @throws InvalidRequestException
     */
    public static <T> T create(String entityName, String groupId, String jsonStr, MnoHttpClient httpClient, Class<T> clazz) throws AuthenticationException, ApiException, InvalidRequestException {        
        String jsonBody = httpClient.post(getCollectionUrl(entityName,groupId), jsonStr);

        return deserializeEntity(entityName,jsonBody,clazz);
    }
	
    /**
     * Fetch an entity by id
     * @param entity name
     * @param groupId customer group id
     * @param entityId id of the entity to retrieve
     * @return entity
     * @throws AuthenticationException
     * @throws ApiException
     * @throws InvalidRequestException
     */
    public static Map<String,Object> retrieve(String entityName, String groupId, String entityId) throws AuthenticationException, ApiException, InvalidRequestException {
        return retrieve(entityName,groupId,entityId,MnoHttpClient.getAuthenticatedClient());
    }
    
    /**
     * Fetch an entity by id
     * @param entity name
     * @param groupId customer group id
     * @param entityId id of the entity to retrieve
     * @param Connec serializer class
     * @return entity
     * @throws AuthenticationException
     * @throws ApiException
     * @throws InvalidRequestException
     */
    public static <T> T retrieve(String entityName, String groupId, String entityId, Class<T> clazz) throws AuthenticationException, ApiException, InvalidRequestException {
        return retrieve(entityName,groupId,entityId,MnoHttpClient.getAuthenticatedClient(),clazz);
    }
	
    /**
     * Fetch an entity by id
     * @param entity name
     * @param groupId customer group id
     * @param entityId id of the entity to retrieve
     * @param httpClient
     * @return entity
     * @throws AuthenticationException
     * @throws ApiException
     * @throws InvalidRequestException
     */
    public static Map<String,Object> retrieve(String entityName, String groupId, String entityId, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
        String jsonBody = httpClient.get(getInstanceUrl(entityName,groupId,entityId));
        Type typeOfHashMap = HashMap.class;
        return GSON.fromJson(jsonBody, typeOfHashMap);
    }
    
    /**
     * Fetch an entity by id
     * @param entity name
     * @param groupId customer group id
     * @param entityId id of the entity to retrieve
     * @param httpClient
     * @param Connec serializer class
     * @return entity
     * @throws AuthenticationException
     * @throws ApiException
     * @throws InvalidRequestException
     */
    public static <T> T retrieve(String entityName, String groupId, String entityId, MnoHttpClient httpClient, Class<T> clazz) throws AuthenticationException, ApiException, InvalidRequestException {
        String jsonBody = httpClient.get(getInstanceUrl(entityName,groupId,entityId));
        
        return deserializeEntity(entityName,jsonBody,clazz);
    }

	
	/**
	 * Update an entity remotely
	 * @param entity name
	 * @param groupId customer group id
	 * @param entityId id of the entity to retrieve
	 * @param hash entity attributes to update 
	 * @return updated entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static <T> T update(String entityName, String groupId, String entityId, T obj) throws AuthenticationException, ApiException, InvalidRequestException {
		Map<String,Object> envelope = new HashMap<String,Object>();
		envelope.put(entityName,obj);
		envelope.put("resource",entityName);
		String payload = GSON.toJson(envelope);
		
		return (T) update(entityName,groupId,entityId,payload,MnoHttpClient.getAuthenticatedClient(),obj.getClass());
	}
	
	/**
	 * Update an entity remotely
	 * @param entity name
	 * @param groupId customer group id
	 * @param entityId id of the entity to retrieve
	 * @param hash entity attributes to update 
	 * @return updated entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static Map<String,Object> update(String entityName, String groupId, String entityId, Map<String,Object> hash) throws AuthenticationException, ApiException, InvalidRequestException {
		return update(entityName,groupId,entityId,hash,MnoHttpClient.getAuthenticatedClient());
	}
	
	/**
	 * Update an entity remotely
	 * @param entity name
	 * @param groupId customer group id
	 * @param entityId id of the entity to retrieve
	 * @param hash entity attributes to update 
	 * @param httpClient
	 * @return updated entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static Map<String,Object> update(String entityName, String groupId, String entityId, Map<String,Object> hash, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
		Map<String,Object> envelope = new HashMap<String,Object>();
		envelope.put(entityName,MnoMapHelper.toUnderscoreHash(hash));
		envelope.put("resource",entityName);
		String payload = GSON.toJson(envelope);
		
		return update(entityName,groupId,entityId,payload,httpClient);
	}
	
	/**
     * Update an entity remotely
     * @param entity name
     * @param groupId customer group id
     * @param entityId id of the entity to retrieve
     * @param hash entity attributes to update 
     * @param httpClient
     * @param Connec serializer class
     * @return updated entity
     * @throws AuthenticationException
     * @throws ApiException
     * @throws InvalidRequestException
     */
    public static <T> T update(String entityName, String groupId, String entityId, Map<String,Object> hash, MnoHttpClient httpClient, Class<T> clazz) throws AuthenticationException, ApiException, InvalidRequestException {
        Map<String,Object> envelope = new HashMap<String,Object>();
        envelope.put(entityName,MnoMapHelper.toUnderscoreHash(hash));
        envelope.put("resource",entityName);
        String payload = GSON.toJson(envelope);
        
        return update(entityName,groupId,entityId,payload,httpClient,clazz);
    }
	
	/**
	 * Update an entity remotely
	 * @param entity name
	 * @param groupId customer group id
	 * @param entityId id of the entity to retrieve
	 * @param jsonStr entity attributes to update 
	 * @param httpClient
	 * @return updated entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 * @throws InvalidRequestException
	 */
	public static Map<String,Object> update(String entityName, String groupId, String entityId, String jsonStr, MnoHttpClient httpClient) throws AuthenticationException, ApiException, InvalidRequestException {
		String jsonBody = httpClient.put(getInstanceUrl(entityName,groupId,entityId),jsonStr);
		Type typeOfHashMap = HashMap.class;
        return GSON.fromJson(jsonBody, typeOfHashMap);
	}
	
	/**
     * Update an entity remotely
     * @param entity name
     * @param groupId customer group id
     * @param entityId id of the entity to retrieve
     * @param jsonStr entity attributes to update 
     * @param httpClient
     * @param Connec serializer class
     * @return updated entity
     * @throws AuthenticationException
     * @throws ApiException
     * @throws InvalidRequestException
     */
    public static <T> T update(String entityName, String groupId, String entityId, String jsonStr, MnoHttpClient httpClient, Class<T> clazz) throws AuthenticationException, ApiException, InvalidRequestException {
        String jsonBody = httpClient.put(getInstanceUrl(entityName,groupId,entityId),jsonStr);

        return deserializeEntity(entityName,jsonBody,clazz);
    }
	
	
	/**
	 * Delete or cancel an entity remotely 
	 * @param entity name
	 * @param groupId customer group id
	 * @param entityId id of the entity to delete 
	 * @return deleted/cancelled entity
	 * @throws AuthenticationException
	 * @throws ApiException
	 */
	public static Map<String, Object> delete(String entityName, String groupId, String entityId) throws AuthenticationException, ApiException {
		return delete(entityName,groupId,entityId,MnoHttpClient.getAuthenticatedClient());
	}
	
	/**
     * Delete or cancel an entity remotely 
     * @param entity name
     * @param groupId customer group id
     * @param entityId id of the entity to delete 
     * @param httpClient
     * @return deleted/cancelled entity
     * @throws AuthenticationException
     * @throws ApiException
     */
    public static Map<String, Object> delete(String entityName, String groupId, String entityId, MnoHttpClient httpClient) throws AuthenticationException, ApiException {
        String jsonBody = httpClient.delete(getInstanceUrl(entityName,groupId,entityId));
        Type typeOfHashMap = HashMap.class;
        return GSON.fromJson(jsonBody, typeOfHashMap);
    }
    
    /**
     * Delete or cancel an entity remotely 
     * @param entity name
     * @param groupId customer group id
     * @param entityId id of the entity to delete 
     * @param httpClient
     * @param Conenc serializer class
     * @return deleted/cancelled entity
     * @throws AuthenticationException
     * @throws ApiException
     */
    public static <T> T delete(String entityName, String groupId, String entityId, MnoHttpClient httpClient, Class<T> clazz) throws AuthenticationException, ApiException {
        String jsonBody = httpClient.delete(getInstanceUrl(entityName,groupId,entityId));
        
        return deserializeEntity(entityName,jsonBody,clazz);
    }
    
    private static <T> T deserializeEntity(String entityName, String jsonBody, Class<T> clazz) {
        Type typeOfHashMap = HashMap.class;
        Map<String, Object> newMap = GSON.fromJson(jsonBody, typeOfHashMap);
        String entityJson = GSON.toJson(newMap.get(entityName));

        return GSON.fromJson(entityJson, clazz);
    }
    
    private static <T> List<T> deserializeEntities(String entityName, String jsonBody, Class<T> clazz) {
        Type typeOfHashMap = HashMap.class;
        Map<String, Object> newMap = GSON.fromJson(jsonBody, typeOfHashMap);
        List<T> entities = new ArrayList<T>();
        List<Object> entitiesHashes = (List<Object>) newMap.get(entityName);
        for (Object entityhash : entitiesHashes) {
            String entityJson = GSON.toJson(entityhash);
            entities.add(GSON.fromJson(entityJson, clazz));
        }
        return entities;
    }
}
