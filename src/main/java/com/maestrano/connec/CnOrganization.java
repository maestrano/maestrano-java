package com.maestrano.connec;

public class CnOrganization extends CnObject {
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
