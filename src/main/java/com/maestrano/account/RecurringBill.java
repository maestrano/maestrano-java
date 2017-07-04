package com.maestrano.account;

import java.util.Date;

import com.maestrano.configuration.Preset;
import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.net.AccountClient;

public class RecurringBill extends MnoObject {
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

	public static RecurringBillClient client(Preset preset) {
		return new RecurringBillClient(preset);
	}

	public static class RecurringBillClient extends AccountClient<RecurringBill> {
		public RecurringBillClient(Preset preset) {
			super(RecurringBill.class, preset);
		}

		/**
		 * Cancel the bill
		 * 
		 * @return whether the bill was cancelled or not
		 * @throws ApiException
		 * @throws AuthenticationException
		 */
		public boolean cancel(RecurringBill bill) throws AuthenticationException, ApiException {
			if (bill.getId() != null && !bill.getId().isEmpty()) {
				RecurringBill newBill = delete(bill.getId());
				return newBill.status.equals("cancelled");
			}
			return false;
		}
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
