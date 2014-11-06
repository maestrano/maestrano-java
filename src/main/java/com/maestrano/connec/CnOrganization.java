package com.maestrano.connec;

import com.maestrano.net.MnoApiConnecClient;

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
	
	public static CnOrganization fromJson(String jsonStr) {
		CnOrganization obj = MnoApiConnecClient.GSON.fromJson(jsonStr, CnOrganization.class);
		obj.activateListeners();
		return obj;
	}
	
	public void activateListeners() {
		if (this.address != null) this.address.activateListeners("address", this._pcs);
		if (this.email != null) this.email.activateListeners("email", this._pcs);
		if (this.website != null) this.website.activateListeners("website", this._pcs);
		if (this.phone != null) this.phone.activateListeners("phone", this._pcs);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this._pcs.firePropertyChange("name", this.name, name);
		this.name = name;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this._pcs.firePropertyChange("industry", this.industry, industry);
		this.industry = industry;
	}
	public Integer getAnnualRevenue() {
		return annualRevenue;
	}
	public void setAnnualRevenue(Integer annualRevenue) {
		this._pcs.firePropertyChange("annualRevenue", this.annualRevenue, annualRevenue);
		this.annualRevenue = annualRevenue;
	}
	public Integer getCapital() {
		return capital;
	}
	public void setCapital(Integer capital) {
		this._pcs.firePropertyChange("capital", this.capital, capital);
		this.capital = capital;
	}
	public Boolean isCustomer() {
		return isCustomer;
	}
	public void setIsCustomer(Boolean isCustomer) {
		this._pcs.firePropertyChange("isCustomer", this.isCustomer, isCustomer);
		this.isCustomer = isCustomer;
	}
	public Boolean isSupplier() {
		return isSupplier;
	}
	public void setIsSupplier(Boolean isSupplier) {
		this._pcs.firePropertyChange("isSupplier", this.isSupplier, isSupplier);
		this.isSupplier = isSupplier;
	}
	public Boolean isLead() {
		return isLead;
	}
	public void setIsLead(Boolean isLead) {
		this._pcs.firePropertyChange("isLead", this.isLead, isLead);
		this.isLead = isLead;
	}
	public CnAddressGroup getAddress() {
		return address;
	}
	public void setAddress(CnAddressGroup address) {
		this.address.unsetParentListener();
		this.address = address;
		this.address.activateListeners("address", this._pcs);
	}
	public CnEmail getEmail() {
		return email;
	}
	public void setEmail(CnEmail email) {
		this.email.unsetParentListener();
		this.email = email;
		this.email.activateListeners("email", this._pcs);
	}
	public CnWebsite getWebsite() {
		return website;
	}
	public void setWebsite(CnWebsite website) {
		this.website.unsetParentListener();
		this.website = website;
		this.website.activateListeners("website", this._pcs);
	}
	public CnPhone getPhone() {
		return phone;
	}
	public void setPhone(CnPhone phone) {
		this.phone.unsetParentListener();
		this.phone = phone;
		this.phone.activateListeners("phone", this._pcs);
	}
	
}
