package com.maestrano.account;

import java.util.Date;

import com.maestrano.configuration.Preset;
import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.net.AccountClient;

public class Bill extends MnoObject {

	public String status;
	public Float units;
	public Date periodStartedAt;
	public Date periodEndedAt;

	// Mandatory for creation
	public String groupId;
	public Integer priceCents;
	public String currency;
	public String description;

	public static BillClient client(Preset preset) {
		return new BillClient(preset);
	}

	public static class BillClient extends AccountClient<Bill> {

		public BillClient(Preset preset) {
			super(Bill.class, preset);
		}

		/**
		 * Cancel the bill
		 * 
		 * @return whether the bill was cancelled or not
		 * @throws ApiException
		 * @throws AuthenticationException
		 */
		public Boolean cancel(Bill bill) throws AuthenticationException, ApiException {
			return cancel(bill.getId());
		}

		public Boolean cancel(String id) throws AuthenticationException, ApiException {
			if (id != null && !id.isEmpty()) {
				Bill newBill = delete(id);
				return "cancelled".equals(newBill.status);
			}

			return false;
		}

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
