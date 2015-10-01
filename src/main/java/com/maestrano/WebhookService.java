package com.maestrano;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class WebhookService {
	private static WebhookService instance;
	
	// Map of Preset Name => AppServiceProperties
    private Map<String, WebhookServiceProperties> presetsProperties = new HashMap<String, WebhookServiceProperties> ();

    /**
     * Properties wrapper for a given preset
     */
    public static class WebhookServiceProperties {
        protected String preset;
        private String accountGroupsPath;
    	private String accountGroupUsersPath;
    	private String notificationspath;
    	private Map<String, Object> connecSubscriptions;
    	
    	private WebhookServiceProperties(String preset) {
            this.preset = preset;
        }
    }

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
        this.configure("default");
    }
    
    /**
     * Configure the service using the maestrano.properties
     * file available in the class path
     */
    public void configure(String preset) {
        this.configure(preset, ConfigFile.getProperties(preset));
    }
    
    /**
     * Configure the service using the specified properties file and preset
     * @param preset
     * @param filename
     */
    public void configure(String preset, String filename) {
        this.configure(preset, ConfigFile.getProperties(preset, filename));
    }
	
	/**
	 * Configure the service using a list of properties
	 * @param preset
	 * @param props Properties object
	 */
	public void configure(String preset, Properties props) {
	    WebhookServiceProperties webhookServiceProperties = new WebhookServiceProperties(preset);
	    
	    webhookServiceProperties.accountGroupsPath = props.getProperty("webhook.account.groupsPath");
	    webhookServiceProperties.accountGroupUsersPath = props.getProperty("webhook.account.groupUsersPath");
	    webhookServiceProperties.notificationspath = props.getProperty("webhook.connec.notificationsPath");
		
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
		webhookServiceProperties.connecSubscriptions = connecSubscriptions;
		
		this.presetsProperties.put(preset, webhookServiceProperties);
	}
	
	/**
     * Return the application endpoint to post groups updates
     * @return groups endpoint
     */
    public String getAccountGroupsPath() {
        return getAccountGroupsPath("default");
    }
    
    /**
     * Return the application endpoint to post groups updates
     * @return groups endpoint
     */
    public String getAccountGroupsPath(String preset) {
        WebhookServiceProperties webhookServiceProperties = this.presetsProperties.get(preset);
        if (webhookServiceProperties == null || webhookServiceProperties.accountGroupsPath == null) return "/maestrano/account/groups/:id";
        return webhookServiceProperties.accountGroupsPath;
    }

    /**
     * Return the application endpoint to post user updates on groups
     * @return group > users endpoint
     */
    public String getAccountGroupUsersPath() {
        return getAccountGroupUsersPath("default");
    }
    
    /**
     * Return the application endpoint to post user updates on groups
     * @return group > users endpoint
     */
    public String getAccountGroupUsersPath(String preset) {
        WebhookServiceProperties webhookServiceProperties = this.presetsProperties.get(preset);
        if (webhookServiceProperties == null || webhookServiceProperties.accountGroupUsersPath == null) return "/maestrano/account/groups/:group_id/users/:id";
        return webhookServiceProperties.accountGroupUsersPath;
    }
	
    /**
     * Return the local notification endpoint
     * @return group > users endpoint
     */
    public String getNotificationsPath() {
        return getNotificationsPath("default");
    }
    
    /**
     * Return the local notification endpoint
     * @return group > users endpoint
     */
    public String getNotificationsPath(String preset) {
        WebhookServiceProperties webhookServiceProperties = this.presetsProperties.get(preset);
        if (webhookServiceProperties == null || webhookServiceProperties.notificationspath == null) return "/maestrano/connec/notifications";
        return webhookServiceProperties.notificationspath;
    }
	
    /**
     * Return the Connec! entities subscriptions
     * @return connec > subscriptions
     */
    public Map<String, Object> getConnecSubscriptions() {
        return getConnecSubscriptions("default");
    }
    
    /**
     * Return the Connec! entities subscriptions
     * @return connec > subscriptions
     */
    public Map<String, Object> getConnecSubscriptions(String preset) {
        WebhookServiceProperties webhookServiceProperties = this.presetsProperties.get(preset);
        if (webhookServiceProperties == null || webhookServiceProperties.connecSubscriptions == null) return new HashMap<String, Object>();
        return webhookServiceProperties.connecSubscriptions;
    }
	
	public Map<String,Object> toMetadataHash() {
        return toMetadataHash("default");
    }
	
	public Map<String,Object> toMetadataHash(String preset) {
        Map<String,Object> hash = new HashMap<String,Object>();
        
        // Account
        Map<String,Object> accountHash = new HashMap<String,Object>();
        accountHash.put("groups_path",getAccountGroupsPath(preset));
        accountHash.put("group_users_path",getAccountGroupUsersPath(preset));
        hash.put("account", accountHash);
        
        // Connec
        Map<String,Object> connecHash = new HashMap<String,Object>();
        hash.put("connec", connecHash);
        
        // Connec > Notifications
        connecHash.put("notifications_path", getNotificationsPath(preset));
        
        // Connec > Subscriptions
        connecHash.put("subscriptions", getConnecSubscriptions(preset));
        
        return hash;
    }
}
