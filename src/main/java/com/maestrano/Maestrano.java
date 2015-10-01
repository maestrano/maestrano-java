package com.maestrano;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;

public final class Maestrano 
{
    private static final String version = "0.7.0";
    
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
     * Configure Maestrano API using a Properties file
     * @param String filename
     */
    public static void configure(String filename) {
        configure("default", filename);
    }
    
    /**
     * Configure Maestrano API using a Properties object
     * @param Properties props
     */
    public static void configure(Properties props) {
        configure("default", props);
    }
    
    /**
     * Configure Maestrano API using a Properties file and preset
     * @param String preset
     * @param Properties props
     */
    public static void configure(String preset, String filename) {
        appService().configure(preset, filename);
        ssoService().configure(preset, filename);
        apiService().configure(preset, filename);
        webhookService().configure(preset, filename);
    }
    
    /**
     * Configure Maestrano API using a Properties object and preset
     * @param String preset
     * @param Properties props
     */
    public static void configure(String preset, Properties props) {
        appService().configure(preset, props);
        ssoService().configure(preset, props);
        apiService().configure(preset, props);
        webhookService().configure(preset, props);
    }
    
    /**
     * Authenticate a Maestrano request using the appId and apiKey
     * @param appId
     * @param apiKey
     * @return authenticated or not
     */
    public static Boolean authenticate(String appId, String apiKey) {
        return authenticate("default", appId, apiKey);
    }
    
    /**
     * Authenticate a Maestrano request using the appId and apiKey
     * @param preset
     * @param appId
     * @param apiKey
     * @return authenticated or not
     */
    public static Boolean authenticate(String preset, String appId, String apiKey) {
        return appId != null && apiKey != null && 
                appId.equals(apiService().getId(preset)) && apiKey.equals(apiService().getKey(preset));
    }
    
    /**
     * Authenticate a Maestrano request by reading the Authorization header
     * @param appId
     * @return authenticated or not
     * @throws UnsupportedEncodingException 
     */
    public static Boolean authenticate(HttpServletRequest request) throws UnsupportedEncodingException {
        return authenticate("default", request);
    }

    /**
     * Authenticate a Maestrano request by reading the Authorization header
     * @param preset
     * @param appId
     * @return authenticated or not
     * @throws UnsupportedEncodingException 
     */
    public static Boolean authenticate(String preset, HttpServletRequest request) throws UnsupportedEncodingException {
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
    			return authenticate(preset,creds[0],creds[1]);
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
        return toMetadataHash("default");
    }
    
    /**
     * Return the Maestrano API configuration as a hash
     * @param preset
     * @return metadata hash
     */
    public static Map<String,Object> toMetadataHash(String preset) {
    	Map<String,Object> hash = new HashMap<String,Object>();
		hash.put("environment",Maestrano.appService().getEnvironment(preset));
		hash.put("app",Maestrano.appService().toMetadataHash(preset));
		hash.put("api",Maestrano.apiService().toMetadataHash(preset));
		hash.put("sso",Maestrano.ssoService().toMetadataHash(preset));
		hash.put("webhook",Maestrano.webhookService().toMetadataHash(preset));
		
		return hash;
    }
    
    /**
     * Return the Maestrano API configuration as a json hash
     * @return metadata hash
     */
    public static String toMetadata() {
        return toMetadata("default");
    }
    
   /**
    * Return the Maestrano API configuration as a json hash
    * @param preset
    * @return metadata hash
    */
    public static String toMetadata(String preset) {
    	Gson gson = new Gson();
		
		return gson.toJson(toMetadataHash(preset));
    }
}
