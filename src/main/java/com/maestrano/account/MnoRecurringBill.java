package com.maestrano.account;

import java.util.Date;

import com.maestrano.Maestrano;
import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.net.MnoAccountClient;

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
	 * @deprecated use {@link #client(Maestrano)} instead
	 */
	public static MnoRecurringBillClient client() {
		return new MnoRecurringBillClient();
	}

	public static MnoRecurringBillClient client(String marketplace) throws MnoConfigurationException {
		return new MnoRecurringBillClient(marketplace);
	}

	public static MnoRecurringBillClient client(Maestrano maestrano) {
		return new MnoRecurringBillClient(maestrano);
	}

	public static class MnoRecurringBillClient extends MnoAccountClient<MnoRecurringBill> {
		public MnoRecurringBillClient(Maestrano maestrano) {
			super(MnoRecurringBill.class, maestrano);
		}

		public MnoRecurringBillClient(String marketplace) throws MnoConfigurationException {
			super(MnoRecurringBill.class, Maestrano.get(marketplace));
		}

		public MnoRecurringBillClient() {
			super(MnoRecurringBill.class);
		}

		/**
		 * Cancel the bill
		 * 
		 * @return whether the bill was cancelled or not
		 * @throws ApiException
		 * @throws AuthenticationException
		 */
		public Boolean cancel(MnoRecurringBill bill) throws AuthenticationException, ApiException {
			if (bill.id != null && !bill.id.isEmpty()) {
				MnoRecurringBill newBill = delete(bill.id);
				bill.merge(newBill);
				return bill.status.equals("cancelled");
			}
			return false;
		}

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
