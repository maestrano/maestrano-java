package com.maestrano.configuration;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;
import com.maestrano.account.Bill.BillClient;
import com.maestrano.account.Group.GroupClient;
import com.maestrano.account.RecurringBill.RecurringBillClient;
import com.maestrano.account.User.UserClient;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.exception.MnoException;
import com.maestrano.helpers.MnoPropertiesHelper;
import com.maestrano.net.ConnecClient;

public class Preset {

	private final String marketplace;
	private final String environment;
	private final App app;
	private final Api api;
	private final Sso sso;
	private final Connec connec;
	private final Webhook webhook;
	
	public Preset(String marketplace, Properties props) throws MnoConfigurationException {
		Properties trimmedProperties = MnoPropertiesHelper.trimProperties(props);
		this.marketplace = marketplace;
		this.environment = props.getProperty("environment");
		this.app = new App(trimmedProperties);
		this.api = new Api(trimmedProperties);
		this.sso = new Sso(api, app, trimmedProperties);
		this.connec = new Connec(app, props);
		this.webhook = new Webhook(props);
	}

	/**
	 * Authenticate a Maestrano request using the appId and apiKey
	 * 
	 * @param marketplace
	 * 
	 * @param appId
	 * 
	 * @param apiKey
	 * 
	 * @return authenticated or not
	 * 
	 * @throws MnoException
	 */
	public boolean authenticate(String appId, String apiKey) throws MnoConfigurationException {
		return appId != null && apiKey != null && appId.equals(api.getId()) && apiKey.equals(api.getKey());
	}

	/**
	 * Authenticate a Maestrano request by reading the Authorization header
	 * 
	 * @param marketplace
	 * @param appId
	 * @return authenticated or not
	 * @throws UnsupportedEncodingException
	 * @throws MnoException
	 */
	public boolean authenticate(HttpServletRequest request) throws MnoException {
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || authHeader.isEmpty()) {
			return false;
		}
		String[] auth = authHeader.trim().split("\\s+");

		if (auth == null || auth.length != 2 || !auth[0].equalsIgnoreCase("basic")) {
			return false;
		}
		byte[] decodedStr = DatatypeConverter.parseBase64Binary(auth[1]);
		String[] creds;
		try {
			creds = (new String(decodedStr, "UTF-8")).split(":");
		} catch (UnsupportedEncodingException e) {
			throw new MnoException("Could not decode basic authentication" + Arrays.toString(auth), e);
		}

		if (creds.length == 2) {
			return authenticate(creds[0], creds[1]);
		} else {
			return false;
		}
	}

	/**
	 * Return the Maestrano API configuration as a hash
	 * 
	 * @param marketplace
	 * @return metadata hash
	 */
	public Map<String, Object> toMetadataHash() {
		Map<String, Object> hash = new LinkedHashMap<String, Object>();
		hash.put("marketplace", marketplace);
		hash.put("app", app.toMetadataHash());
		hash.put("api", api.toMetadataHash());
		hash.put("sso", sso.toMetadataHash());
		hash.put("connec", connec.toMetadataHash());
		hash.put("webhook", webhook.toMetadataHash());
		return hash;
	}

	/**
	 * 
	 * @return the Maestrano API configuration as a json hash
	 */
	public String toMetadata() {
		Gson gson = new Gson();
		return gson.toJson(toMetadataHash());
	}

	/**
	 * 
	 * @return the marketplace name of the given preset
	 */
	public String getMarketplace() {
		return marketplace;
	}

	/**
	 * 
	 * @return Developer platform environment
	 */
	public String getEnvironment() {
		return environment;
	}

	public App getApp() {
		return app;
	}

	public Api getApi() {
		return api;
	}

	public Sso getSso() {
		return sso;
	}

	public Connec getConnec() {
		return connec;
	}
	
	public Webhook getWebhook() {
		return webhook;
	}

	public BillClient billClient() {
		return new BillClient(this);
	}

	public RecurringBillClient recurringBillClient() {
		return new RecurringBillClient(this);
	}

	public UserClient userClient() {
		return new UserClient(this);
	}

	public GroupClient groupClient() {
		return new GroupClient(this);
	}

	public ConnecClient connecClient() {
		return new ConnecClient(this);
	}

}
