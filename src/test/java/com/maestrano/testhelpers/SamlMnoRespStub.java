package com.maestrano.testhelpers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.maestrano.saml.Response;

public class SamlMnoRespStub extends Response {
	
	public SamlMnoRespStub() throws Exception {
		SimpleDateFormat simpleDf = new SimpleDateFormat("yyyy-MM-dd'T'H:mm:ssZ");
		this.cachedAttributes = new HashMap<String,String>();
		
		this.cachedAttributes.put("mno_session", "7ds8f9789a7fd7x0b898bvb8vc9h0gg");
		this.cachedAttributes.put("mno_session_recheck", simpleDf.format(new Date()));
		this.cachedAttributes.put("group_uid", "cld-1");
		this.cachedAttributes.put("group_name", "SomeGroupName");
		this.cachedAttributes.put("group_email", "email@example.com");
		this.cachedAttributes.put("group_role", "Admin");
		this.cachedAttributes.put("group_end_free_trial", simpleDf.format(new Date()));
		this.cachedAttributes.put("group_has_credit_card", "true");
		this.cachedAttributes.put("group_currency", "USD");
		this.cachedAttributes.put("group_timezone", "America/Los_Angeles");
		this.cachedAttributes.put("group_country", "US");
		this.cachedAttributes.put("group_city", "Los Angeles");
		this.cachedAttributes.put("uid", "usr-1");
		this.cachedAttributes.put("virtual_uid", "user-1.cld-1");
		this.cachedAttributes.put("email", "j.doe@doecorp.com");
		this.cachedAttributes.put("virtual_email", "user-1.cld-1@mail.maestrano.com");
		this.cachedAttributes.put("name", "John");
		this.cachedAttributes.put("surname", "Doe");
		this.cachedAttributes.put("country", "AU");
		this.cachedAttributes.put("company_name", "DoeCorp");
	}
}
