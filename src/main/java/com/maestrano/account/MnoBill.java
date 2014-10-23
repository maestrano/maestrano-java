package com.maestrano.account;

import java.util.Date;

public class MnoBill {
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
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Float getUnits() {
		return units;
	}
	public void setUnits(Float units) {
		this.units = units;
	}
	public Date getPeriodStartedAt() {
		return periodStartedAt;
	}
	public void setPeriodStartedAt(Date periodStartedAt) {
		this.periodStartedAt = periodStartedAt;
	}
	public Date getPeriodEndedAt() {
		return periodEndedAt;
	}
	public void setPeriodEndedAt(Date periodEndedAt) {
		this.periodEndedAt = periodEndedAt;
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
