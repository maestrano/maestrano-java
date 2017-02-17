package com.maestrano.sso;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import com.maestrano.exception.MnoException;
import com.maestrano.helpers.MnoDateHelper;

public class Group {
	private String uid;
	private String name;
	private String email;
	private Boolean hasCreditCard;
	private Date freeTrialEndAt;
	private String companyName;
	private String currency;
	private TimeZone timezone;
	private String country;
	private String city;

	/**
	 * Constructor
	 * 
	 * @param samlResponse
	 *            a SAML Response from Maestrano IDP
	 * @throws MnoException
	 */
	public Group(com.maestrano.saml.Response samlResponse) throws MnoException {
		Map<String, String> att = samlResponse.getAttributes();

		// General info
		this.uid = att.get("group_uid");
		this.name = att.get("group_name");
		this.email = att.get("group_email");
		String groupEndFreeTrial = att.get("group_end_free_trial");
		try {
			this.freeTrialEndAt = MnoDateHelper.fromIso8601(groupEndFreeTrial);
		} catch (ParseException e) {
			throw new MnoException("Could not parse group_end_free_trial: " + groupEndFreeTrial, e);
		}

		this.companyName = att.get("company_name");
		this.hasCreditCard = (att.get("group_has_credit_card") != null && att.get("group_has_credit_card").equals("true"));

		// Geo info
		this.currency = att.get("group_currency");
		this.timezone = TimeZone.getTimeZone(att.get("group_timezone"));
		this.country = att.get("group_country");
		this.city = att.get("group_city");
	}

	/**
	 * Return the group UID
	 * 
	 * @return String group UID
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * Return the name of the group
	 * 
	 * @return String group name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the principal contact email for this group
	 * 
	 * @return String principal email address
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Return whether the group has a credit card
	 * 
	 * @return
	 */
	public Boolean hasCreditCard() {
		return hasCreditCard;
	}

	/**
	 * Return when the group free trial is finishing
	 * 
	 * @return Date end of free trial
	 */
	public Date getFreeTrialEndAt() {
		return freeTrialEndAt;
	}

	/**
	 * Return the original company name for this group Can be empty
	 * 
	 * @return String company name
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * Return the currency code of main currency used by this group
	 * 
	 * @return String currency code
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * Return the timezone for this group
	 * 
	 * @return TimeZone group timezone
	 */
	public TimeZone getTimezone() {
		return timezone;
	}

	/**
	 * Return the ALPHA2 country code for this group
	 * 
	 * @return String alpha2 country code
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Return the city in which this group is located
	 * 
	 * @return String group city
	 */
	public String getCity() {
		return city;
	}
}
