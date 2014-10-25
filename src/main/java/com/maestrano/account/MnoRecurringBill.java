package com.maestrano.account;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.maestrano.net.MnoApiAccountClient;

public class MnoRecurringBill extends MnoObject {
	public String id;
	public Date createdAt;
	public Date updatedAt;
	public String status;
	public Float units;
	public String period;
	public Integer frequency;
	public Integer cycles;
	public Date startDate;
	public Integer initialCents;
	
	// Mandatory for creation
	public String groupId;
	public Integer priceCents;
	public String currency;
	public String description;
	
	
	/**
	 * Return all application bills
	 * @return list of bills
	 * @throws IOException
	 */
	public static List<MnoRecurringBill> all() throws IOException {
		return MnoApiAccountClient.all(MnoRecurringBill.class);
	}
	
	/**
	 * Return all bills matching the criteria passed in argument
	 * @param params
	 * @return list of bills
	 * @throws IOException
	 */
	public static List<MnoRecurringBill> all(Map<String,String> params) throws IOException {
		return MnoApiAccountClient.all(MnoRecurringBill.class, params);
	}
	
	/**
	 * Retrieve a single bill by id
	 * @param billId
	 * @return a bill if found, null otherwise
	 * @throws IOException
	 */
	public static MnoRecurringBill retrieve(String billId) throws IOException {
		return MnoApiAccountClient.retrieve(MnoRecurringBill.class, billId);
	}
	
	/**
	 * Create a new Bill
	 * @param params
	 * @return created bill
	 * @throws IOException
	 */
	public static MnoRecurringBill create(Map<String,Object> params) throws IOException {
		return MnoApiAccountClient.create(MnoRecurringBill.class, params);
	}
	
	/**
	 * Cancel the bill
	 * @return whether the bill was cancelled or not
	 * @throws IOException 
	 */
	public Boolean cancel() throws IOException {
		if (this.id != null && !this.id.isEmpty()) {
			MnoRecurringBill newBill = MnoApiAccountClient.delete(MnoRecurringBill.class, this.id);
			this.merge(newBill);
			return this.status.equals("cancelled");
		}
		
		return false;
	}
	
	
	public String getId() {
		return id;
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
		this.units = units;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public Integer getFrequency() {
		return frequency;
	}
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	public Integer getCycles() {
		return cycles;
	}
	public void setCycles(Integer cycles) {
		this.cycles = cycles;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Integer getInitialCents() {
		return initialCents;
	}
	public void setInitialCents(Integer initialCents) {
		this.initialCents = initialCents;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public Integer getPriceCents() {
		return priceCents;
	}
	public void setPriceCents(Integer priceCents) {
		this.priceCents = priceCents;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
