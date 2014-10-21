package com.maestrano;

import java.util.Properties;

/**
 * Hello world!
 *
 */
public final class Maestrano 
{
    private static final String version = "0.1.0";
    
    // Private constructor
    private Maestrano() {}
    
    /**
     * Return the current Maestrano API version
     * @return String version
     */
    public static String getVersion() {
    	return version;
    }
    
    /**
     * Configure Maestrano API using a Properties object
     * @param Properties props
     */
    public static void configure(Properties props) {
    	appService().configure(props);
    	ssoService().configure(props);
    	apiService().configure(props);
    	webhookService().configure(props);
    }
    
    /**
     * Return the Maestrano App Service
     * @return AppService singleton
     */
    public static AppService appService() {
    	return AppService.getInstance();
    }
    
    /**
     * Return the Maestrano Sso Service
     * @return SsoService singleton
     */
    public static SsoService ssoService() {
    	return SsoService.getInstance();
    }
    
    /**
     * Return the Maestrano Api Service
     * @return ApiService singleton
     */
    public static ApiService apiService() {
    	return ApiService.getInstance();
    }
    
    /**
     * Return the Maestrano Webhook Service
     * @return ApiService singleton
     */
    public static WebhookService webhookService() {
    	return WebhookService.getInstance();
    }
}
