package com.maestrano.connec;

import java.util.List;
import java.util.Map;

import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.exception.InvalidRequestException;
import com.maestrano.helpers.MnoMapHelper;
import com.maestrano.net.ConnecClient;

public class CnTaxRate extends ConnecResource {
	private String name;
	private double rate;
	
	/**
	 * Instantiate a new TaxRate from a JSON string
	 * @param jsonStr
	 * @return new tax code object
	 */
	public static CnTaxRate fromJson(String jsonStr) {
		CnTaxRate obj = ConnecClient.GSON.fromJson(jsonStr, CnTaxRate.class);
		return obj;
	}
	
	/**
	 * Instantiate a new TaxRate entity from a map of attributes
	 * @param map of attributes
	 * @return new tax code object
	 */
	public static CnTaxRate fromMap(Map<String,Object> map) {
		String jsonStr = ConnecClient.GSON.toJson(MnoMapHelper.toUnderscoreHash(map));
		CnTaxRate obj = fromJson(jsonStr);
		return obj;
	}
	
	/**
	 * Return all tax rates
	 * @param groupId the groupId for which to retrieve organizations
	 * @return list of organizations
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 * @throws InvalidRequestException 
	 */
	public static List<CnTaxRate> all(String groupId) throws AuthenticationException, ApiException, InvalidRequestException {
		return ConnecClient.all(CnTaxRate.class,groupId);
	}
	
	/**
	 * Retrieve the tax rates corresponding to the provided group and id
	 * @param groupId customer group id
	 * @param id group id
	 * @return a company entity if found, null otherwise
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 * @throws InvalidRequestException 
	 */
	public static CnTaxRate retrieve(String groupId, String id) throws AuthenticationException, ApiException, InvalidRequestException {
		return ConnecClient.retrieve(CnTaxRate.class, groupId,id);
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
	public static CnTaxRate create(String groupId, Map<String,Object> params) throws AuthenticationException, ApiException, InvalidRequestException {
		return ConnecClient.create(CnTaxRate.class,groupId,params);
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
	public boolean save(String groupId) throws AuthenticationException, ApiException, InvalidRequestException {
		if (groupId == null) return false;
		this.groupId = groupId;
		
		CnTaxRate obj;
		if (this.id == null) {
			obj = ConnecClient.create(CnTaxRate.class,this.groupId,this);
		} else {
			obj = ConnecClient.update(CnTaxRate.class,this.groupId,this.id,this);
		}
		
		this.merge(obj);
		
		return true;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
}
