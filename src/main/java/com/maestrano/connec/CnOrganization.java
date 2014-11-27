package com.maestrano.connec;

import java.util.List;
import java.util.Map;

import com.maestrano.account.MnoBill;
import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.exception.InvalidRequestException;
import com.maestrano.helpers.MnoMapHelper;
import com.maestrano.net.ConnecClient;
import com.maestrano.net.MnoAccountClient;

public class CnOrganization extends ConnecResource {
	private String name;
	private String industry;
	private Integer annualRevenue;
	private Integer capital;
	private Boolean isCustomer;
	private Boolean isSupplier;
	private Boolean isLead;
	private CnAddressGroup address;
	private CnEmail email;
	private CnWebsite website;
	private CnPhone phone;
	
	/**
	 * Instantiate a new CnOrganization entity from a JSON string
	 * 
	 * @param jsonStr representing the object attributes using snake case keys
	 * @return
	 */
	public static CnOrganization fromJson(String jsonStr) {
		CnOrganization obj = ConnecClient.GSON.fromJson(jsonStr, CnOrganization.class);
		return obj;
	}
	
	/**
	 * Instantiate a new Company entity from a map of attributes
	 * @param map of attributes
	 * @return
	 */
	public static CnOrganization fromMap(Map<String,Object> map) {
		String jsonStr = ConnecClient.GSON.toJson(MnoMapHelper.toUnderscoreHash(map));
		CnOrganization obj = fromJson(jsonStr);
		return obj;
	}
	
	/**
	 * Return all group organizations
	 * @param groupId the groupId for which to retrieve organizations
	 * @return list of organizations
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 * @throws InvalidRequestException 
	 */
	public static List<CnOrganization> all(String groupId) throws AuthenticationException, ApiException, InvalidRequestException {
		return ConnecClient.all(CnOrganization.class,groupId);
	}
	
	/**
	 * Retrieve the company corresponding to the provided groupId
	 * @param groupId customer group id
	 * @param id group id
	 * @return a company entity if found, null otherwise
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 * @throws InvalidRequestException 
	 */
	public static CnOrganization retrieve(String groupId, String id) throws AuthenticationException, ApiException, InvalidRequestException {
		return ConnecClient.retrieve(CnOrganization.class, groupId,id);
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
	public static CnOrganization create(String groupId, Map<String,Object> params) throws AuthenticationException, ApiException, InvalidRequestException {
		return ConnecClient.create(CnOrganization.class,groupId,params);
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
		
		CnOrganization obj;
		if (this.id == null) {
			obj = ConnecClient.create(CnOrganization.class,this.groupId,this);
		} else {
			obj = ConnecClient.update(CnOrganization.class,this.groupId,this.id,this);
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
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public Integer getAnnualRevenue() {
		return annualRevenue;
	}
	public void setAnnualRevenue(Integer annualRevenue) {
		this.annualRevenue = annualRevenue;
	}
	public Integer getCapital() {
		return capital;
	}
	public void setCapital(Integer capital) {
		this.capital = capital;
	}
	public Boolean isCustomer() {
		return isCustomer;
	}
	public void setIsCustomer(Boolean isCustomer) {
		this.isCustomer = isCustomer;
	}
	public Boolean isSupplier() {
		return isSupplier;
	}
	public void setIsSupplier(Boolean isSupplier) {
		this.isSupplier = isSupplier;
	}
	public Boolean isLead() {
		return isLead;
	}
	public void setIsLead(Boolean isLead) {
		this.isLead = isLead;
	}
	public CnAddressGroup getAddress() {
		return address;
	}
	public void setAddress(CnAddressGroup address) {
		this.address = address;
	}
	public CnEmail getEmail() {
		return email;
	}
	public void setEmail(CnEmail email) {
		this.email = email;
	}
	public CnWebsite getWebsite() {
		return website;
	}
	public void setWebsite(CnWebsite website) {
		this.website = website;
	}
	public CnPhone getPhone() {
		return phone;
	}
	public void setPhone(CnPhone phone) {
		this.phone = phone;
	}
	
}
