package com.maestrano;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.maestrano.helpers.MnoPropertiesHelper;

public class WebhookService {

	private final String accountGroupsPath;
	private final String accountGroupUsersPath;
	private final String notificationspath;
	private final Map<String, Boolean> connecSubscriptions;

	// Package Private Constructor
	WebhookService(Properties props) {

		this.accountGroupsPath = MnoPropertiesHelper.getPropertyOrDefault(props, "webhook.account.groupsPath");
		this.accountGroupUsersPath = MnoPropertiesHelper.getPropertyOrDefault(props, "webhook.account.groupUsersPath");
		this.notificationspath = MnoPropertiesHelper.getPropertyOrDefault(props, "webhook.connec.notificationsPath");
		this.connecSubscriptions = new HashMap<String, Boolean>();
		// Map properties under "webhook.connec.subscriptions" as a Map
		for (String key : props.stringPropertyNames()) {
			if (key.startsWith("webhook.connec.subscriptions")) {
				String entityName = key.substring(key.lastIndexOf('.') + 1);
				boolean subscriptionFlag = Boolean.valueOf(props.getProperty(key));
				connecSubscriptions.put(entityName, subscriptionFlag);
			}
		}

	}

	/**
	 * Return the application endpoint to post groups updates
	 * 
	 * @return groups endpoint
	 */
	public String getAccountGroupsPath() {
		return accountGroupsPath;
	}

	/**
	 * Return the application endpoint to post user updates on groups
	 * 
	 * @return group > users endpoint
	 */
	public String getAccountGroupUsersPath() {
		return accountGroupUsersPath;
	}

	/**
	 * Return the local notification endpoint
	 * 
	 * @return group > users endpoint
	 */
	public String getNotificationsPath() {
		return notificationspath;
	}

	/**
	 * Return the Connec! entities subscriptions
	 * 
	 * @return connec > subscriptions
	 */
	public Map<String, Boolean> getConnecSubscriptions() {
		return connecSubscriptions;
	}

	public Map<String, Object> toMetadataHash() {
		Map<String, Object> hash = new LinkedHashMap<String, Object>();

		// Account
		Map<String, Object> accountHash = new LinkedHashMap<String, Object>();
		accountHash.put("groups_path", getAccountGroupsPath());
		accountHash.put("group_users_path", getAccountGroupUsersPath());
		hash.put("account", accountHash);

		// Connec
		Map<String, Object> connecHash = new LinkedHashMap<String, Object>();
		hash.put("connec", connecHash);

		// Connec > Notifications
		connecHash.put("notifications_path", getNotificationsPath());

		// Connec > Subscriptions
		connecHash.put("subscriptions", getConnecSubscriptions());

		return hash;
	}
}
