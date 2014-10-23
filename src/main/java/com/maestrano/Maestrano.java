package com.maestrano;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

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
     * Authenticate a Maestrano request using the appId and apiKey
     * @param appId
     * @param apiKey
     * @return authenticated or not
     */
    public static Boolean authenticate(String appId, String apiKey) {
    	return appId != null && apiKey != null && 
    			appId.equals(apiService().getId()) && apiKey.equals(apiService().getKey());
    }
    
    /**
     * Authenticate a Maestrano request by reading the Authorization header
     * @param appId
     * @return authenticated or not
     * @throws UnsupportedEncodingException 
     */
    public static Boolean authenticate(HttpServletRequest request) throws UnsupportedEncodingException {
    	String authHeader = request.getHeader("Authorization");
    	String[] auth = null;
    	if (authHeader != null && !authHeader.isEmpty()) {
    		authHeader = authHeader.trim();
    		auth = authHeader.split("\\s+");	
    	}
    	
    	if ( auth != null && auth.length == 2 && auth[0].equalsIgnoreCase("basic")) {
    		byte[] decodedStr = DatatypeConverter.parseBase64Binary(auth[1]);
    		String[] creds = (new String(decodedStr,"UTF-8")).split(":");
    		
    		if (creds.length == 2) {
    			return authenticate(creds[0],creds[1]);
    		}
    	}
    	
    	return false;
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
