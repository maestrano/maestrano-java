package com.maestrano.connec;

import java.util.Date;
import java.util.Map;

import com.maestrano.helpers.MnoMapHelper;
import com.maestrano.net.ConnecClient;

public class CnPerson extends ConnecResource {
	private String title;
	private String firstName;
	private String lastName;
	private Date birthDate;
	private String organizationId;
	private Boolean isCustomer;
	private Boolean isSupplier;
	private Boolean isLead;
	private CnAddressGroup addressWork;
	private CnAddressGroup addressHome;
	private CnEmail email;
	private CnWebsite website;
	private CnPhone phoneWork;
	private CnPhone phoneHome;
	private Map<String,CnNote> notes;
	
	public String getEntityName() {
        return "people";
    }
	
	public static CnPerson fromJson(String jsonStr) {
		CnPerson obj = ConnecClient.GSON.fromJson(jsonStr, CnPerson.class);
		return obj;
	}
	
	/**
	 * Instantiate a new Person entity from a map of attributes
	 * @param map of attributes
	 * @return
	 */
	public static CnPerson fromMap(Map<String,Object> map) {
		String jsonStr = ConnecClient.GSON.toJson(MnoMapHelper.toUnderscoreHash(map));
		CnPerson obj = fromJson(jsonStr);
		return obj;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public Boolean getIsCustomer() {
		return isCustomer;
	}
	public void setIsCustomer(Boolean isCustomer) {
		this.isCustomer = isCustomer;
	}
	public Boolean getIsSupplier() {
		return isSupplier;
	}
	public void setIsSupplier(Boolean isSupplier) {
		this.isSupplier = isSupplier;
	}
	public Boolean getIsLead() {
		return isLead;
	}
	public void setIsLead(Boolean isLead) {
		this.isLead = isLead;
	}
	public CnAddressGroup getAddressWork() {
		return addressWork;
	}
	public void setAddressWork(CnAddressGroup addressWork) {
		this.addressWork = addressWork;
	}
	public CnAddressGroup getAddressHome() {
		return addressHome;
	}
	public void setAddressHome(CnAddressGroup addressHome) {
		this.addressHome = addressHome;
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
	public CnPhone getPhoneWork() {
		return phoneWork;
	}
	public void setPhoneWork(CnPhone phoneWork) {
		this.phoneWork = phoneWork;
	}
	public CnPhone getPhoneHome() {
		return phoneHome;
	}
	public void setPhoneHome(CnPhone phoneHome) {
		this.phoneHome = phoneHome;
	}
	public Map<String, CnNote> getNotes() {
		return notes;
	}
	public void setNotes(Map<String, CnNote> notes) {
		this.notes = notes;
	}
}
