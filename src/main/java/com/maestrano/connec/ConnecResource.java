package com.maestrano.connec;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.exception.InvalidRequestException;
import com.maestrano.net.ConnecClient;

class ConnecResource extends ConnecObject {
	protected String id;
	protected Date createdAt;
	protected Date updatedAt;
	protected String groupId;
	
	public ConnecResource() {
		super();
	}
	
    public static <T extends ConnecResource> String entityName(Class<T> clazz) {
        return getInstance(clazz).getEntityName();
    }

    public static <T extends ConnecResource> T getInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getEntityName() {
        return "resources";
    }
	
	/**
	 * Return a JSON representation of the entity
	 * @return
	 */
	public String toJson() {
		return ConnecClient.GSON.toJson(this);
	}
	
	/**
	 * Return a String representation of the entity (JSON representation)
	 */
	public String toString() {
		return this.toJson();
	}
	
	/**
     * Return all entities
     * @param groupId the groupId for which to retrieve entities
     * @return list of entities
     * @throws ApiException 
     * @throws AuthenticationException 
     * @throws InvalidRequestException 
     */
    public static <T extends ConnecResource> List<T> all(String groupId, Class<T> clazz) throws AuthenticationException, ApiException, InvalidRequestException {
        return ConnecClient.all(entityName(clazz),groupId,clazz);
    }
    
    /**
     * Retrieve the entity corresponding to the provided group and id
     * @param groupId customer group id
     * @param id group id
     * @return an entity if found, null otherwise
     * @throws ApiException 
     * @throws AuthenticationException 
     * @throws InvalidRequestException 
     */
    public static <T extends ConnecResource> T retrieve(String groupId, String id, Class<T> clazz) throws AuthenticationException, ApiException, InvalidRequestException {
        return ConnecClient.retrieve(entityName(clazz),groupId,id,clazz);
    }
    
    /**
     * Create a new entity
     * @param groupId customer group id
     * @param params map of attributes
     * @return created entity
     * @throws ApiException 
     * @throws AuthenticationException 
     * @throws InvalidRequestException 
     */
    public static <T extends ConnecResource> T create(String groupId, Map<String,Object> params, Class<T> clazz) throws AuthenticationException, ApiException, InvalidRequestException {
        return ConnecClient.create(entityName(clazz),groupId,params,clazz);
    }
    
    /**
     * Save the entity
     * @return true if the resource was saved
     * @throws AuthenticationException
     * @throws ApiException
     * @throws InvalidRequestException
     */
    public boolean save() throws AuthenticationException, ApiException, InvalidRequestException {
        return this.save(this.groupId);
    }
    
    /**
     * Save the entity
     * @param groupId customer group id
     * @return true if the resource was saved
     * @throws AuthenticationException
     * @throws ApiException
     * @throws InvalidRequestException
     */
    public <T extends ConnecResource> boolean save(String groupId) throws AuthenticationException, ApiException, InvalidRequestException {
        if (groupId == null) return false;
        this.groupId = groupId;
        
        ConnecResource obj;
        if (this.id == null) {
            obj = ConnecClient.create(entityName(this.getClass()),this.groupId,this);
        } else {
            obj = ConnecClient.update(entityName(this.getClass()),this.groupId,this.id,this);
        }
        
        this.merge(obj);
        
        return true;
    }
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}
