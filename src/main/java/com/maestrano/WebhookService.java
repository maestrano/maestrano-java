package com.maestrano;

import java.util.Properties;

public class WebhookService {
	private static WebhookService instance;

	private String accountGroupsPath;
	private String accountGroupUsersPath;

	// Private Constructor
	private WebhookService() {}

	/**
	 * Return the service singleton
	 * @return singleton
	 */
	public static WebhookService getInstance() {
		if (instance == null) {
			instance = new WebhookService();
			instance.configure();
		}
		return instance;
	}

	/**
	 * Configure the service using the maestrano.properties
	 * file available in the class path
	 */
	public void configure() {
		this.configure(ConfigFile.getProperties());
	}
	
	/**
	 * Configure the service using a list of properties
	 * @param props Properties object
	 */
	public void configure(Properties props) {
		this.accountGroupsPath = props.getProperty("webhook.account.groupsPath");
		this.accountGroupUsersPath = props.getProperty("webhook.account.groupUsersPath");
	}
	
	/**
	 * Return the application endpoint to post groups updates
	 * @return groups endpoint
	 */
	public String getAccountGroupsPath() {
		if (accountGroupsPath == null) return "/maestrano/account/groups/:id";
		return accountGroupsPath;
	}

	public void setAccountGroupsPath(String accountGroupsPath) {
		this.accountGroupsPath = accountGroupsPath;
	}
	
	/**
	 * Return the application endpoint to post user updates on groups
	 * @return group > users endpoint
	 */
	public String getAccountGroupUsersPath() {
		if (accountGroupUsersPath == null) return "/maestrano/account/groups/:group_id/users/:id";
		return accountGroupUsersPath;
	}

	public void setAccountGroupUsersPath(String accountGroupUsersPath) {
		this.accountGroupUsersPath = accountGroupUsersPath;
	}
}
