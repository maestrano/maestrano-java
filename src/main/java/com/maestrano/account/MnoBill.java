package com.maestrano.account;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.exception.InvalidRequestException;
import com.maestrano.net.MnoApiAccountClient;

public class MnoBill extends MnoObject {
	
	public String id;
	public Date createdAt;
	public Date updatedAt;
	public String status;
	public Float units;
	public Date periodStartedAt;
	public Date periodEndedAt;

	// Mandatory for creation
	public String groupId;
	public Integer priceCents;
	public String currency;
	public String description;
	
	/**
	 * Return all application bills
	 * @return list of bills
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 * @throws InvalidRequestException 
	 */
	public static List<MnoBill> all() throws AuthenticationException, ApiException, InvalidRequestException {
		return MnoApiAccountClient.all(MnoBill.class);
	}
	
	/**
	 * Return all bills matching the criteria passed in argument
	 * @param params
	 * @return list of bills
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 * @throws InvalidRequestException 
	 */
	public static List<MnoBill> all(Map<String,String> params) throws AuthenticationException, ApiException, InvalidRequestException {
		return MnoApiAccountClient.all(MnoBill.class, params);
	}
	
	/**
	 * Retrieve a single bill by id
	 * @param billId
	 * @return a bill if found, null otherwise
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 * @throws InvalidRequestException 
	 */
	public static MnoBill retrieve(String billId) throws AuthenticationException, ApiException, InvalidRequestException {
		return MnoApiAccountClient.retrieve(MnoBill.class, billId);
	}
	
	/**
	 * Create a new Bill
	 * @param params
	 * @return created bill
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 * @throws InvalidRequestException 
	 */
	public static MnoBill create(Map<String,Object> params) throws AuthenticationException, ApiException, InvalidRequestException {
		return MnoApiAccountClient.create(MnoBill.class, params);
	}
	
	/**
	 * Cancel the bill
	 * @return whether the bill was cancelled or not
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 */
	public Boolean cancel() throws AuthenticationException, ApiException {
		if (this.id != null && !this.id.isEmpty()) {
			MnoBill newBill = MnoApiAccountClient.delete(MnoBill.class, this.id);
			this.merge(newBill);
			return this.status.equals("cancelled");
		}
		
		return false;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public Date getUpdatedAt() {
		return updatedAt;
	}
	
	public String getStatus() {
		return status;
	}
	
	public Float getUnits() {
		return units;
	}
	
	public void setUnits(Float units) {
		this.changeAttribute("units", units);
	}
	
	public Date getPeriodStartedAt() {
		return periodStartedAt;
	}
	
	public void setPeriodStartedAt(Date periodStartedAt) {
		this.changeAttribute("periodStartedAt", periodStartedAt);
	}
	
	public Date getPeriodEndedAt() {
		return periodEndedAt;
	}
	
	public void setPeriodEndedAt(Date periodEndedAt) {
		this.changeAttribute("periodEndedAt", periodEndedAt);
	}
	
	public String getGroupId() {
		return groupId;
	}
	
	public void setGroupId(String groupId) {
		this.changeAttribute("groupId", groupId);
	}
	
	public Integer getPriceCents() {
		return priceCents;
	}
	
	public void setPriceCents(Integer priceCents) {
		this.changeAttribute("priceCents", priceCents);
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.changeAttribute("currency", currency);
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.changeAttribute("description", description);
	}
	
	
	
}
