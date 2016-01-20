package com.maestrano.account;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.exception.InvalidRequestException;

public class MnoGroup extends MnoObject {
	private String id;
	private Date createdAt;
	private Date updatedAt;
	private Boolean hasCreditCard;
	private String status;
	
	/**
	 * Return all groups using the application
	 * @return list of groups
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 * @throws InvalidRequestException 
	 */
	public static List<MnoGroup> all() throws AuthenticationException, ApiException, InvalidRequestException {
		return getDefaultClient().all(MnoGroup.class);
	}
	
	/**
	 * Return all groups using the application and matching the criteria
	 * @param <V>
	 * @param params
	 * @return list of groups
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 * @throws InvalidRequestException 
	 */
	public static <V> List<MnoGroup> all(Map<String,V> params) throws AuthenticationException, ApiException, InvalidRequestException {
		return getDefaultClient().all(MnoGroup.class, params);
	}
	
	/**
	 * Retrieve a single group by id
	 * @param entityId
	 * @return a group if found, null otherwise
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 * @throws InvalidRequestException 
	 */
	public static MnoGroup retrieve(String entityId) throws AuthenticationException, ApiException, InvalidRequestException {
		return getDefaultClient().retrieve(MnoGroup.class, entityId);
	}
	
	public String getId() {
		return id;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public Boolean getHasCreditCard() {
		return hasCreditCard;
	}
	public String getStatus() {
		return status;
	}
}
