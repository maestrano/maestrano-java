package com.maestrano.connec;

import java.util.List;
import java.util.Map;

import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.exception.InvalidRequestException;
import com.maestrano.helpers.MnoMapHelper;
import com.maestrano.net.ConnecClient;

public class CnTaxCode extends ConnecResource {
	private String name;
	private String description;
	private double saleTaxRate;
	private double purchaseTaxRate;
	private List<CnTaxRate> saleTaxes;
	private List<CnTaxRate> purchaseTaxes;
	
	/**
	 * Instantiate a new TaxRate from a JSON string
	 * @param jsonStr
	 * @return new tax code object
	 */
	public static CnTaxCode fromJson(String jsonStr) {
		CnTaxCode obj = ConnecClient.GSON.fromJson(jsonStr, CnTaxCode.class);
		return obj;
	}
	
	/**
	 * Instantiate a new TaxRate entity from a map of attributes
	 * @param map of attributes
	 * @return new tax code object
	 */
	public static CnTaxCode fromMap(Map<String,Object> map) {
		String jsonStr = ConnecClient.GSON.toJson(MnoMapHelper.toUnderscoreHash(map));
		CnTaxCode obj = fromJson(jsonStr);
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
	public static List<CnTaxCode> all(String groupId) throws AuthenticationException, ApiException, InvalidRequestException {
		return ConnecClient.all(CnTaxCode.class,groupId);
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
	public static CnTaxCode retrieve(String groupId, String id) throws AuthenticationException, ApiException, InvalidRequestException {
		return ConnecClient.retrieve(CnTaxCode.class, groupId,id);
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
	public static CnTaxCode create(String groupId, Map<String,Object> params) throws AuthenticationException, ApiException, InvalidRequestException {
		return ConnecClient.create(CnTaxCode.class,groupId,params);
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
		
		CnTaxCode obj;
		if (this.id == null) {
			obj = ConnecClient.create(CnTaxCode.class,this.groupId,this);
		} else {
			obj = ConnecClient.update(CnTaxCode.class,this.groupId,this.id,this);
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getSaleTaxRate() {
		return saleTaxRate;
	}
	public void setSaleTaxRate(double saleTaxRate) {
		this.saleTaxRate = saleTaxRate;
	}
	public double getPurchaseTaxRate() {
		return purchaseTaxRate;
	}
	public void setPurchaseTaxRate(double purchaseTaxRate) {
		this.purchaseTaxRate = purchaseTaxRate;
	}
	public List<CnTaxRate> getSaleTaxes() {
		return saleTaxes;
	}
	public void setSaleTaxes(List<CnTaxRate> saleTaxes) {
		this.saleTaxes = saleTaxes;
	}
	public List<CnTaxRate> getPurchaseTaxes() {
		return purchaseTaxes;
	}
	public void setPurchaseTaxes(List<CnTaxRate> purchaseTaxes) {
		this.purchaseTaxes = purchaseTaxes;
	}
	
	
}
