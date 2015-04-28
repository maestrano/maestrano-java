package com.maestrano.connec;

import java.util.Map;

import com.maestrano.helpers.MnoMapHelper;
import com.maestrano.net.ConnecClient;

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
	
	public String getEntityName() {
        return "organizations";
    }
    
    public static Class<?> getEntityClass() {
        return CnOrganization.class;
    }
	
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
