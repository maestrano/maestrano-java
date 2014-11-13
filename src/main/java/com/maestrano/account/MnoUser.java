package com.maestrano.account;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.exception.InvalidRequestException;
import com.maestrano.net.MnoAccountClient;

public class MnoUser extends MnoObject {
	private String id;
	private String name;
	private String surname;
	private String email;
	private String companyName;
	private String country;
	private String ssoSession;
	private Date createdAt;
	private Date updatedAt;
	
	/**
	 * Return all users using the application
	 * @return list of users
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 * @throws InvalidRequestException 
	 */
	public static List<MnoUser> all() throws AuthenticationException, ApiException, InvalidRequestException {
		return MnoAccountClient.all(MnoUser.class);
	}
	
	/**
	 * Return all users using the application and matching the criteria
	 * @param <V>
	 * @param params
	 * @return list of users
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 * @throws InvalidRequestException 
	 */
	public static <V> List<MnoUser> all(Map<String,V> params) throws AuthenticationException, ApiException, InvalidRequestException {
		return MnoAccountClient.all(MnoUser.class, params);
	}
	
	/**
	 * Retrieve a single user by id
	 * @param entityId
	 * @return a user if found, null otherwise
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 * @throws InvalidRequestException 
	 */
	public static MnoUser retrieve(String entityId) throws AuthenticationException, ApiException, InvalidRequestException {
		return MnoAccountClient.retrieve(MnoUser.class, entityId);
	}
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getSurname() {
		return surname;
	}
	public String getEmail() {
		return email;
	}
	public String getCompanyName() {
		return companyName;
	}
	public String getCountry() {
		return country;
	}
	public String getSsoSession() {
		return ssoSession;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	
	
}
