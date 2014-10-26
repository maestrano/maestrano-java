package com.maestrano;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public final class Maestrano 
{
    private static final String version = "0.4.0";
    
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
     * Return the current application environment
     * @return either 'test' or 'production'
     */
    public static String getEnvironment() {
    	return appService().getEnvironment();
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
    
    /**
     * Return the Maestrano API configuration as a hash
     * @return metadata hash
     */
    public static Map<String,Object> toMetadataHash() {
    	Map<String,Object> hash = new HashMap<String,Object>();
		hash.put("environment",Maestrano.getEnvironment());
		hash.put("app",Maestrano.appService().toMetadataHash());
		hash.put("api",Maestrano.apiService().toMetadataHash());
		hash.put("sso",Maestrano.ssoService().toMetadataHash());
		hash.put("webhook",Maestrano.webhookService().toMetadataHash());
		
		return hash;
    }
    
    /**
     * Return the Maestrano API configuration as a json hash
     * @return metadata hash
     */
    public static String toMetadata() {
    	Gson gson = new Gson();
		
		return gson.toJson(toMetadataHash());
    }
}
