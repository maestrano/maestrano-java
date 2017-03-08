package com.maestrano.configuration;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.helpers.MnoPropertiesHelper;

public class Webhook {
	private final String accountGroupPath;
	private final String accountGroupUserPath;

	private final boolean connecExternalIds;
	private final String connecNotificationPath;

	// Package Private Constructor
	Webhook(Properties props) throws MnoConfigurationException {

		this.accountGroupPath = MnoPropertiesHelper.getProperty(props, "webhooks.account.groupPath");
		this.accountGroupUserPath = MnoPropertiesHelper.getProperty(props, "webhooks.account.groupUserPath");
		this.connecExternalIds = MnoPropertiesHelper.getBooleanProperty(props, "webhooks.connec.externalIds");
		this.connecNotificationPath = MnoPropertiesHelper.getProperty(props, "webhooks.connec.notificationPath");
	}

	/**
	 * @return the application endpoint to post groups updates
	 */
	public String getAccountGroupPath() {
		return accountGroupPath;
	}

	/**
	 * @return the application endpoint to post user updates on groups
	 */
	public String getAccountGroupUserPath() {
		return accountGroupUserPath;
	}

	/**
	 * @return the local notification endpoint
	 */
	public String getConnecNotificationPath() {
		return connecNotificationPath;
	}

	/**
	 * 
	 * @return true if connec will use external ids
	 */
	public boolean getConnecExternalIds() {
		return connecExternalIds;
	}

	public Map<String, Object> toMetadataHash() {
		Map<String, Object> hash = new LinkedHashMap<String, Object>();
		// Account
		Map<String, Object> accountHash = new LinkedHashMap<String, Object>();
		accountHash.put("group_path", accountGroupPath);
		accountHash.put("group_user_path", accountGroupUserPath);
		hash.put("account", accountHash);
		// Connec
		Map<String, Object> connecHash = new LinkedHashMap<String, Object>();
		hash.put("connec", connecHash);
		connecHash.put("external_ids", connecExternalIds);
		connecHash.put("notification_path", connecNotificationPath);
		return hash;
	}

}
