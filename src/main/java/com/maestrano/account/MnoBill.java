package com.maestrano.account;

import java.util.Date;

import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.net.MnoAccountClient;

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

	public static MnoBillClient client() {
		return new MnoBillClient();
	}

	public static MnoBillClient client(String presetId) throws MnoConfigurationException {
		return new MnoBillClient(presetId);
	}
	
	public static class MnoBillClient extends MnoAccountClient<MnoBill> {
		public MnoBillClient(String preset) throws MnoConfigurationException {
			super(MnoBill.class, preset);
		}

		public MnoBillClient() {
			super(MnoBill.class);
		}

		/**
		 * Cancel the bill
		 * 
		 * @return whether the bill was cancelled or not
		 * @throws ApiException
		 * @throws AuthenticationException
		 */
		public Boolean cancel(MnoBill bill) throws AuthenticationException, ApiException {
			if (bill.id != null && !bill.id.isEmpty()) {
				MnoBill newBill = delete(bill.id);
				bill.merge(newBill);
				return bill.status.equals("cancelled");
			}

			return false;
		}
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
