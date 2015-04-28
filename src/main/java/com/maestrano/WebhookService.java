package com.maestrano;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class WebhookService {
	private static WebhookService instance;

	private String accountGroupsPath;
	private String accountGroupUsersPath;
	private Map<String, Object> connecSubscriptions;

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
		
		// Map properties under "webhook.connec.subscriptions" as a Map
		Map<String, Object> connecSubscriptions = new HashMap<String, Object>();
		Enumeration<String> propertyNames = (Enumeration<String>) props.propertyNames();
		while(propertyNames.hasMoreElements()) {
		    String propertyName = propertyNames.nextElement();
		    if(propertyName.startsWith("webhook.connec.subscriptions")) {
		        String entityName = propertyName.substring(propertyName.lastIndexOf('.') + 1);
		        Boolean subscriptionFlag = Boolean.parseBoolean(props.getProperty(propertyName));
		        connecSubscriptions.put(entityName, subscriptionFlag);
		    }
        }
		this.connecSubscriptions = connecSubscriptions;
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
	
	/**
     * Return the Connec! entities subscriptions
     * @return connec > subscriptions
     */
    public Map<String, Object> getConnecSubscriptions() {
        if (connecSubscriptions == null) return new HashMap<String, Object>();
        return connecSubscriptions;
    }

    public void setConnecSubscriptions(Map<String, Object> connecSubscriptions) {
        this.connecSubscriptions = connecSubscriptions;
    }
	
	public Map<String,Object> toMetadataHash() {
	    Map<String,Object> hash = new HashMap<String,Object>();
	    
	    // Account
		Map<String,Object> accountHash = new HashMap<String,Object>();
		accountHash.put("groups_path",getAccountGroupsPath());
		accountHash.put("group_users_path",getAccountGroupUsersPath());
		hash.put("account", accountHash);
		
		// Subscriptions
		Map<String,Object> connecHash = new HashMap<String,Object>();
		connecHash.put("subscriptions", getConnecSubscriptions());
		hash.put("connec", connecHash);
		
		return hash;
	}
}
