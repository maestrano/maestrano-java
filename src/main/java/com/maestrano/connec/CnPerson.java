package com.maestrano.connec;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.exception.InvalidRequestException;
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
	
	/**
	 * Return all people
	 * @param groupId the groupId for which to retrieve organizations
	 * @return list of organizations
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 * @throws InvalidRequestException 
	 */
	public static List<CnPerson> all(String groupId) throws AuthenticationException, ApiException, InvalidRequestException {
		return ConnecClient.all(CnPerson.class,groupId);
	}
	
	/**
	 * Retrieve the person corresponding to the provided group and id
	 * @param groupId customer group id
	 * @param id group id
	 * @return a company entity if found, null otherwise
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 * @throws InvalidRequestException 
	 */
	public static CnPerson retrieve(String groupId, String id) throws AuthenticationException, ApiException, InvalidRequestException {
		return ConnecClient.retrieve(CnPerson.class, groupId,id);
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
	public static CnPerson create(String groupId, Map<String,Object> params) throws AuthenticationException, ApiException, InvalidRequestException {
		return ConnecClient.create(CnPerson.class,groupId,params);
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
		
		CnPerson obj;
		if (this.id == null) {
			obj = ConnecClient.create(CnPerson.class,this.groupId,this);
		} else {
			obj = ConnecClient.update(CnPerson.class,this.groupId,this.id,this);
		}
		
		this.merge(obj);
		
		return true;
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
