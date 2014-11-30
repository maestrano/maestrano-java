package com.maestrano.connec;

import java.util.List;
import java.util.Map;

import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.exception.InvalidRequestException;
import com.maestrano.helpers.MnoMapHelper;
import com.maestrano.net.ConnecClient;

public class CnItem extends ConnecResource {
	private String name;
	private String code;
	private String description;
	private String status;
	private String type;
	private String unit;
	private String parentItemId;
	private CnPrice salePrice;
	private CnPrice purchasePrice;
	
	/**
	 * Instantiate a new TaxRate from a JSON string
	 * @param jsonStr
	 * @return new tax code object
	 */
	public static CnItem fromJson(String jsonStr) {
		CnItem obj = ConnecClient.GSON.fromJson(jsonStr, CnItem.class);
		return obj;
	}
	
	/**
	 * Instantiate a new TaxRate entity from a map of attributes
	 * @param map of attributes
	 * @return new tax code object
	 */
	public static CnItem fromMap(Map<String,Object> map) {
		String jsonStr = ConnecClient.GSON.toJson(MnoMapHelper.toUnderscoreHash(map));
		CnItem obj = fromJson(jsonStr);
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
	public static List<CnItem> all(String groupId) throws AuthenticationException, ApiException, InvalidRequestException {
		return ConnecClient.all(CnItem.class,groupId);
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
	public static CnItem retrieve(String groupId, String id) throws AuthenticationException, ApiException, InvalidRequestException {
		return ConnecClient.retrieve(CnItem.class, groupId,id);
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
	public static CnItem create(String groupId, Map<String,Object> params) throws AuthenticationException, ApiException, InvalidRequestException {
		return ConnecClient.create(CnItem.class,groupId,params);
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
		
		CnItem obj;
		if (this.id == null) {
			obj = ConnecClient.create(CnItem.class,this.groupId,this);
		} else {
			obj = ConnecClient.update(CnItem.class,this.groupId,this.id,this);
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public CnPrice getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(CnPrice salePrice) {
		this.salePrice = salePrice;
	}
	public CnPrice getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(CnPrice purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public String getParentItemId() {
		return parentItemId;
	}
	public void setParentItemId(String parentItemId) {
		this.parentItemId = parentItemId;
	}
}
