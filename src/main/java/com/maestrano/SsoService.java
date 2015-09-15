package com.maestrano;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SsoService {
	private static SsoService instance;
	
	// Map of Preset Name => AppServiceProperties
    private Map<String, SsoServiceProperties> presetsProperties = new HashMap<String, SsoServiceProperties> ();
	
    /**
     * Properties wrapper for a given preset
     */
    public static class SsoServiceProperties {
        protected String preset;
        private Boolean enabled;
    	private Boolean sloEnabled;
    	private String creationMode;
    	private String initPath;
    	private String consumePath;
    	private String idm;
    	private String idp;
    	private String nameIdFormat;
    	private String x509Fingerprint;
    	private String x509Certificate;
    	
    	private SsoServiceProperties(String preset) {
            this.preset = preset;
        }
    }
	
	// Private Constructor
	private SsoService() {}
	
	/**
	 * Return the service singleton
	 * @return SsoService singleton
	 */
	public static SsoService getInstance() {
		if (instance == null) {
			instance = new SsoService();
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
	 * Configure the service using a list of properties
	 * @param preset
	 * @param props Properties object
	 */
	public void configure(String preset, Properties props) {
	    SsoServiceProperties ssoServiceProperties = new SsoServiceProperties(preset);        
        
		if (props.getProperty("sso.enabled") != null) {
			ssoServiceProperties.enabled = props.getProperty("sso.enabled").equalsIgnoreCase("true");
		}
		
		if (props.getProperty("sso.sloEnabled") != null) {
		    ssoServiceProperties.sloEnabled = props.getProperty("sso.sloEnabled").equalsIgnoreCase("true");
		}
		
		ssoServiceProperties.creationMode = props.getProperty("sso.creationMode");
		ssoServiceProperties.initPath = props.getProperty("sso.initPath");
		ssoServiceProperties.consumePath = props.getProperty("sso.consumePath");
		ssoServiceProperties.idm = props.getProperty("sso.idm");
		ssoServiceProperties.idp = props.getProperty("sso.idp");
		ssoServiceProperties.nameIdFormat = props.getProperty("sso.nameIdFormat");
		ssoServiceProperties.x509Fingerprint = props.getProperty("sso.enabled");
		ssoServiceProperties.x509Certificate = props.getProperty("sso.enabled");
		
		this.presetsProperties.put(preset, ssoServiceProperties);
	}
	
	/**
     * Is Single Sign-On enabled - true by default
     * @return Boolean
     */
    public Boolean getEnabled() {
        return getEnabled("default");
    }
	
	/**
	 * Is Single Sign-On enabled - true by default
	 * @return Boolean
	 */
	public Boolean getEnabled(String preset) {
	    SsoServiceProperties ssoServiceProperties = this.presetsProperties.get(preset);
	    
		if (ssoServiceProperties == null || ssoServiceProperties.enabled == null) return true;
		return ssoServiceProperties.enabled;
	}

    /**
     * Is Single Logout enabled - true by default
     * @return Boolean
     */
    public Boolean getSloEnabled() {
        return getSloEnabled("default");
    }
	
	/**
	 * Is Single Logout enabled - true by default
	 * @return Boolean
	 */
	public Boolean getSloEnabled(String preset) {
	    SsoServiceProperties ssoServiceProperties = this.presetsProperties.get(preset);
	    
		if (ssoServiceProperties == null || ssoServiceProperties.sloEnabled == null) return true;
		return ssoServiceProperties.sloEnabled;
	}
	
	/**
     * Return the API application id (used as issuer)
     * @return String application id
     */
    public String getIssuer() {
        return getIssuer("default");
    }
    
    /**
	 * Return the API application id (used as issuer)
	 * @return String application id
	 */
	public String getIssuer(String preset) {
		return Maestrano.apiService().getId(preset);
	}
	
	/**
     * SSO user creation mode
     * @return String either 'real' or 'virtual'. Default: 'real'
     */
    public String getCreationMode() {
        return getCreationMode("default");
    }
    
    /**
	 * SSO user creation mode
	 * @return String either 'real' or 'virtual'. Default: 'real'
	 */
	public String getCreationMode(String preset) {
	    SsoServiceProperties ssoServiceProperties = this.presetsProperties.get(preset);
	    
		if (ssoServiceProperties == null || ssoServiceProperties.creationMode == null || !ssoServiceProperties.creationMode.equals("virtual")) return "real";
		return ssoServiceProperties.creationMode;
	}
	
	/**
     * Path to SAML init action
     * @return String
     */
    public String getInitPath() {
        return getInitPath("default");
    }
    
    /**
	 * Path to SAML init action
	 * @return String
	 */
	public String getInitPath(String preset) {
	    SsoServiceProperties ssoServiceProperties = this.presetsProperties.get(preset);
	    
		if (ssoServiceProperties == null || ssoServiceProperties.initPath == null || ssoServiceProperties.initPath.isEmpty()) return "/maestrano/auth/saml/init";
		return ssoServiceProperties.initPath;
	}
	
	/**
     * Path to SAML consume action
     * @return
     */
    public String getConsumePath() {
        return getConsumePath("default");
    }
    
    /**
     * Path to SAML consume action
     * @return
     */
    public String getConsumePath(String preset) {
        SsoServiceProperties ssoServiceProperties = this.presetsProperties.get(preset);
        
        if (ssoServiceProperties == null || ssoServiceProperties.consumePath == null || ssoServiceProperties.consumePath.isEmpty()) return "/maestrano/auth/saml/consume";
        return ssoServiceProperties.consumePath;
    }
	
    /**
     * Host name of your application identity manager (if applicable)
     * @return String the configured idm, or application host or localhost if nothing set
     */
    public String getIdm() {
        return getIdm("default");
    }
    
    /**
	 * Host name of your application identity manager (if applicable)
	 * @return String the configured idm, or application host or localhost if nothing set
	 */
	public String getIdm(String preset) {
	    SsoServiceProperties ssoServiceProperties = this.presetsProperties.get(preset);
	    
		if (ssoServiceProperties == null || ssoServiceProperties.idm == null || ssoServiceProperties.idm.isEmpty()) {
			if (Maestrano.appService().getHost() == null || Maestrano.appService().getHost().isEmpty()) {
				return "http://localhost";
			} else {
				return Maestrano.appService().getHost();
			}
		}
		return ssoServiceProperties.idm;
	}

    /**
     * Return the address of the identity provider. Unless this property has been set manually
     * it returns the production idp (maestrano.com) or test fingerprint (api-sandbox) based on the current environment
     * (See Maestrano.appService().getEnvironment())
     * 
     * @return String Identity Provider address
     */
    public String getIdp() {
        return getIdp("default");
    }
    
	/**
	 * Return the address of the identity provider. Unless this property has been set manually
	 * it returns the production idp (maestrano.com) or test fingerprint (api-sandbox) based on the current environment
	 * (See Maestrano.appService().getEnvironment())
	 * 
	 * @return String Identity Provider address
	 */
	public String getIdp(String preset) {
	    SsoServiceProperties ssoServiceProperties = this.presetsProperties.get(preset);
	    
		if (ssoServiceProperties == null || ssoServiceProperties.idp == null || ssoServiceProperties.idp.isEmpty()) {
			if (Maestrano.appService().getEnvironment().equals("production")) {
				return "https://maestrano.com";
			} else {
				return "http://api-sandbox.maestrano.io";
			}
		}
		return ssoServiceProperties.idp;
	}
	
	/**
     * Return the SAML Name ID Format. Currently nameid-format:persistent
     * @return
     */
    public String getNameIdFormat() {
        return getNameIdFormat("default");
    }
    
    /**
	 * Return the SAML Name ID Format. Currently nameid-format:persistent
	 * @return
	 */
	public String getNameIdFormat(String preset) {
	    SsoServiceProperties ssoServiceProperties = this.presetsProperties.get(preset);
	    
		if (ssoServiceProperties == null || ssoServiceProperties.nameIdFormat == null || ssoServiceProperties.nameIdFormat.isEmpty()) return "urn:oasis:names:tc:SAML:2.0:nameid-format:persistent";
		return ssoServiceProperties.nameIdFormat;
	}
	
	/**
     * Return the certificate fingerprint used by SAML. Unless this property has been set manually
     * it returns the production fingerprint or test (sandbox) fingerprint based on the current environment
     * (See Maestrano.appService().getEnvironment())
     * @return String certificate fingerprint
     */
    public String getX509Fingerprint() {
        return getX509Fingerprint("default");
    }
    
    /**
	 * Return the certificate fingerprint used by SAML. Unless this property has been set manually
	 * it returns the production fingerprint or test (sandbox) fingerprint based on the current environment
	 * (See Maestrano.appService().getEnvironment())
	 * @return String certificate fingerprint
	 */
	public String getX509Fingerprint(String preset) {
	    SsoServiceProperties ssoServiceProperties = this.presetsProperties.get(preset);
	    
		if (ssoServiceProperties == null || ssoServiceProperties.x509Fingerprint == null || ssoServiceProperties.x509Fingerprint.isEmpty()) {
			if (Maestrano.appService().getEnvironment().equals("production")) {
				return "2f:57:71:e4:40:19:57:37:a6:2c:f0:c5:82:52:2f:2e:41:b7:9d:7e";
			} else {
				return "01:06:15:89:25:7d:78:12:28:a6:69:c7:de:63:ed:74:21:f9:f5:36";
			}
		}
		return ssoServiceProperties.x509Fingerprint;
	}
	
	/**
     * Return the public certificate used for SAML SSO. Unless this property has been set manually
     * it returns the production certificate or test (sandbox) fingerprint based on the current environment
     * (See Maestrano.appService().getEnvironment())
     * @return String certificate fingerprint
     */
    public String getX509Certificate() {
        return getX509Certificate("default");
    }
    
    /**
	 * Return the public certificate used for SAML SSO. Unless this property has been set manually
	 * it returns the production certificate or test (sandbox) fingerprint based on the current environment
	 * (See Maestrano.appService().getEnvironment())
	 * @return String certificate fingerprint
	 */
	public String getX509Certificate(String preset) {
	    SsoServiceProperties ssoServiceProperties = this.presetsProperties.get(preset);
	    
		if (ssoServiceProperties == null || ssoServiceProperties.x509Certificate == null || ssoServiceProperties.x509Certificate.isEmpty()) {
			if (Maestrano.appService().getEnvironment().equals("production")) {
				return "MIIDezCCAuSgAwIBAgIJAPFpcH2rW0pyMA0GCSqGSIb3DQEBBQUAMIGGMQswCQYD\nVQQGEwJBVTEMMAoGA1UECBMDTlNXMQ8wDQYDVQQHEwZTeWRuZXkxGjAYBgNVBAoT\nEU1hZXN0cmFubyBQdHkgTHRkMRYwFAYDVQQDEw1tYWVzdHJhbm8uY29tMSQwIgYJ\nKoZIhvcNAQkBFhVzdXBwb3J0QG1hZXN0cmFuby5jb20wHhcNMTQwMTA0MDUyNDEw\nWhcNMzMxMjMwMDUyNDEwWjCBhjELMAkGA1UEBhMCQVUxDDAKBgNVBAgTA05TVzEP\nMA0GA1UEBxMGU3lkbmV5MRowGAYDVQQKExFNYWVzdHJhbm8gUHR5IEx0ZDEWMBQG\nA1UEAxMNbWFlc3RyYW5vLmNvbTEkMCIGCSqGSIb3DQEJARYVc3VwcG9ydEBtYWVz\ndHJhbm8uY29tMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQD3feNNn2xfEz5/\nQvkBIu2keh9NNhobpre8U4r1qC7h7OeInTldmxGL4cLHw4ZAqKbJVrlFWqNevM5V\nZBkDe4mjuVkK6rYK1ZK7eVk59BicRksVKRmdhXbANk/C5sESUsQv1wLZyrF5Iq8m\na9Oy4oYrIsEF2uHzCouTKM5n+O4DkwIDAQABo4HuMIHrMB0GA1UdDgQWBBSd/X0L\n/Pq+ZkHvItMtLnxMCAMdhjCBuwYDVR0jBIGzMIGwgBSd/X0L/Pq+ZkHvItMtLnxM\nCAMdhqGBjKSBiTCBhjELMAkGA1UEBhMCQVUxDDAKBgNVBAgTA05TVzEPMA0GA1UE\nBxMGU3lkbmV5MRowGAYDVQQKExFNYWVzdHJhbm8gUHR5IEx0ZDEWMBQGA1UEAxMN\nbWFlc3RyYW5vLmNvbTEkMCIGCSqGSIb3DQEJARYVc3VwcG9ydEBtYWVzdHJhbm8u\nY29tggkA8WlwfatbSnIwDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQUFAAOBgQDE\nhe/18oRh8EqIhOl0bPk6BG49AkjhZZezrRJkCFp4dZxaBjwZTddwo8O5KHwkFGdy\nyLiPV326dtvXoKa9RFJvoJiSTQLEn5mO1NzWYnBMLtrDWojOe6Ltvn3x0HVo/iHh\nJShjAn6ZYX43Tjl1YXDd1H9O+7/VgEWAQQ32v8p5lA==";
			} else {
				return "MIIDezCCAuSgAwIBAgIJAOehBr+YIrhjMA0GCSqGSIb3DQEBBQUAMIGGMQswCQYD\nVQQGEwJBVTEMMAoGA1UECBMDTlNXMQ8wDQYDVQQHEwZTeWRuZXkxGjAYBgNVBAoT\nEU1hZXN0cmFubyBQdHkgTHRkMRYwFAYDVQQDEw1tYWVzdHJhbm8uY29tMSQwIgYJ\nKoZIhvcNAQkBFhVzdXBwb3J0QG1hZXN0cmFuby5jb20wHhcNMTQwMTA0MDUyMjM5\nWhcNMzMxMjMwMDUyMjM5WjCBhjELMAkGA1UEBhMCQVUxDDAKBgNVBAgTA05TVzEP\nMA0GA1UEBxMGU3lkbmV5MRowGAYDVQQKExFNYWVzdHJhbm8gUHR5IEx0ZDEWMBQG\nA1UEAxMNbWFlc3RyYW5vLmNvbTEkMCIGCSqGSIb3DQEJARYVc3VwcG9ydEBtYWVz\ndHJhbm8uY29tMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDVkIqo5t5Paflu\nP2zbSbzxn29n6HxKnTcsubycLBEs0jkTkdG7seF1LPqnXl8jFM9NGPiBFkiaR15I\n5w482IW6mC7s8T2CbZEL3qqQEAzztEPnxQg0twswyIZWNyuHYzf9fw0AnohBhGu2\n28EZWaezzT2F333FOVGSsTn1+u6tFwIDAQABo4HuMIHrMB0GA1UdDgQWBBSvrNxo\neHDm9nhKnkdpe0lZjYD1GzCBuwYDVR0jBIGzMIGwgBSvrNxoeHDm9nhKnkdpe0lZ\njYD1G6GBjKSBiTCBhjELMAkGA1UEBhMCQVUxDDAKBgNVBAgTA05TVzEPMA0GA1UE\nBxMGU3lkbmV5MRowGAYDVQQKExFNYWVzdHJhbm8gUHR5IEx0ZDEWMBQGA1UEAxMN\nbWFlc3RyYW5vLmNvbTEkMCIGCSqGSIb3DQEJARYVc3VwcG9ydEBtYWVzdHJhbm8u\nY29tggkA56EGv5giuGMwDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQUFAAOBgQCc\nMPgV0CpumKRMulOeZwdpnyLQI/NTr3VVHhDDxxCzcB0zlZ2xyDACGnIG2cQJJxfc\n2GcsFnb0BMw48K6TEhAaV92Q7bt1/TYRvprvhxUNMX2N8PHaYELFG2nWfQ4vqxES\nRkjkjqy+H7vir/MOF3rlFjiv5twAbDKYHXDT7v1YCg==";
			}
		}
		return ssoServiceProperties.x509Certificate;
	}
	
	/**
     * Return the full Idp SSO endpoint
     * @return 
     */
    public String getIdpUrl() {
        return getIdpUrl("default");
    }
    
    /**
     * Return the full Idp SSO endpoint
     * @return 
     */
    public String getIdpUrl(String preset) {
        return this.getIdp(preset) + "/api/v1/auth/saml";
    }
	
    /**
     * Return the application SSO initialization endpoint
     * @return String initialization endpoint
     */
    public String getInitUrl() {
        return getInitUrl("default");
    }
    
    /**
     * Return the application SSO initialization endpoint
     * @return String initialization endpoint
     */
    public String getInitUrl(String preset) {
        return this.getIdm(preset) + this.getInitPath(preset);
    }
	
    /**
     * Return the application SSO consume endpoint
     * @return String consume endpoint
     */
    public String getConsumeUrl() {
        return getConsumeUrl("default");
    }
    
    /**
     * Return the application SSO consume endpoint
     * @return String consume endpoint
     */
    public String getConsumeUrl(String preset) {
        return this.getIdm(preset) + this.getConsumePath(preset);
    }
	
    /**
     * Return the Idp logout url where users should be redirected to
     * upon logging out
     * @return String Idp logout url
     */
    public String getLogoutUrl() {
        return getLogoutUrl("default");
    }
    
    /**
     * Return the Idp logout url where users should be redirected to
     * upon logging out
     * @return String Idp logout url
     */
    public String getLogoutUrl(String preset) {
        return this.getIdp(preset) + "/app_logout";
    }
	
    /**
     * Return the Idp access not granted url where unauthorized users should 
     * be redirected to
     * 
     * @return String Idp access 
     */
    public String getUnauthorizedUrl() {
        return getUnauthorizedUrl("default");
    }
    
    /**
     * Return the Idp access not granted url where unauthorized users should 
     * be redirected to
     * 
     * @return String Idp access 
     */
    public String getUnauthorizedUrl(String preset) {
        return this.getIdp(preset) + "/app_access_unauthorized";
    }
	
    /**
     * Return the endpoint used for remote session check (used for single logout)
     * @param userUid Maestrano User UID
     * @param sessionToken Maestrano User session token
     * @return String the endpoint to reach
     */
    public String getSessionCheckUrl(String userUid, String sessionToken) {
        return getSessionCheckUrl("default", userUid, sessionToken);
    }
    
    /**
     * Return the endpoint used for remote session check (used for single logout)
     * @param preset
     * @param userUid Maestrano User UID
     * @param sessionToken Maestrano User session token
     * @return String the endpoint to reach
     */
    public String getSessionCheckUrl(String preset, String userUid, String sessionToken) {
        String url = this.getIdpUrl(preset);
        url += "/" + userUid + "?session=" + sessionToken;
        
        return url;
    }
	
    public com.maestrano.saml.Settings getSamlSettings() {
        return getSamlSettings("default");
    }
    
    public com.maestrano.saml.Settings getSamlSettings(String preset) {
        return new com.maestrano.saml.Settings(
                this.getConsumeUrl(preset), 
                this.getIssuer(preset), 
                this.getIdpUrl(preset), 
                this.getX509Certificate(preset), 
                this.getNameIdFormat(preset)
                );
    }
	
    public Map<String,String> toMetadataHash() {
        return toMetadataHash("default");
    }
    
    public Map<String,String> toMetadataHash(String preset) {
        Map<String,String> hash = new HashMap<String,String>();
        hash.put("enabled",getEnabled(preset).toString());
        hash.put("creation_mode",getCreationMode(preset));
        hash.put("init_path",getInitPath(preset));
        hash.put("consume_path",getConsumePath(preset));
        hash.put("idm",getIdm(preset));
        hash.put("idp",getIdp(preset));
        hash.put("name_id_format",getNameIdFormat(preset));
        hash.put("x509_fingerprint",getX509Fingerprint(preset));
        hash.put("x509_certificate",getX509Certificate(preset));
        
        return hash;
    }
}
