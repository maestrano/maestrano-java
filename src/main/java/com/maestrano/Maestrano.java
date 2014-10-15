package com.maestrano;

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
     * Return the Maestrano App Service
     * @return AppService singleton
     */
    public static AppService appService() {
    	return (AppService) AppService.getInstance();
    }
    
    /**
     * Return the Maestrano Sso Service
     * @return SsoService singleton
     */
    public static SsoService ssoService() {
    	return (SsoService) SsoService.getInstance();
    }
}
