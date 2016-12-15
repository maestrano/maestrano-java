package com.maestrano.testhelpers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.maestrano.saml.Response;

public class SamlMnoRespStub extends Response {
	private final HashMap<String, String> attributes;

	@Override
	public Map<String, String> getAttributes() {
		return attributes;
	}

	public SamlMnoRespStub() {
		super();
		SimpleDateFormat simpleDf = new SimpleDateFormat("yyyy-MM-dd'T'H:mm:ssZ");
		this.attributes = new HashMap<String, String>();

		this.attributes.put("mno_session", "7ds8f9789a7fd7x0b898bvb8vc9h0gg");
		this.attributes.put("mno_session_recheck", simpleDf.format(new Date()));
		this.attributes.put("group_uid", "cld-1");
		this.attributes.put("group_name", "SomeGroupName");
		this.attributes.put("group_email", "email@example.com");
		this.attributes.put("group_role", "Admin");
		this.attributes.put("group_end_free_trial", simpleDf.format(new Date()));
		this.attributes.put("group_has_credit_card", "true");
		this.attributes.put("group_currency", "USD");
		this.attributes.put("group_timezone", "America/Los_Angeles");
		this.attributes.put("group_country", "US");
		this.attributes.put("group_city", "Los Angeles");
		this.attributes.put("uid", "usr-1");
		this.attributes.put("virtual_uid", "user-1.cld-1");
		this.attributes.put("email", "j.doe@doecorp.com");
		this.attributes.put("virtual_email", "user-1.cld-1@mail.maestrano.com");
		this.attributes.put("name", "John");
		this.attributes.put("surname", "Doe");
		this.attributes.put("country", "AU");
		this.attributes.put("company_name", "DoeCorp");
	}
}
