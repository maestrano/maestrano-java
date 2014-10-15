package com.maestrano;

import java.util.Properties;

class SsoService {
	private static SsoService instance;
	
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
		this.configure(ConfigFile.getProperties());
	}
	
	/**
	 * Configure the service using a list of properties
	 * @param props Properties object
	 */
	public void configure(Properties props) {
		if (props.getProperty("sso.sloEnabled") != null) {
			this.enabled = props.getProperty("sso.enabled").equalsIgnoreCase("true");
		}
		
		if (props.getProperty("sso.sloEnabled") != null) {
			this.sloEnabled = props.getProperty("sso.sloEnabled").equalsIgnoreCase("true");
		}
		
		this.creationMode = props.getProperty("sso.creationMode");
		this.initPath = props.getProperty("sso.initPath");
		this.consumePath = props.getProperty("sso.consumePath");
		this.idm = props.getProperty("sso.idm");
		this.idp = props.getProperty("sso.idp");
		this.nameIdFormat = props.getProperty("sso.nameIdFormat");
		this.x509Fingerprint = props.getProperty("sso.enabled");
		this.x509Certificate = props.getProperty("sso.enabled");
	}
	
	/**
	 * Is Single Sign-On enabled - true by default
	 * @return Boolean
	 */
	public Boolean getEnabled() {
		if (enabled == null) return true;
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * Is Single Logout enabled - true by default
	 * @return Boolean
	 */
	public Boolean getSloEnabled() {
		if (enabled == null) return true;
		return sloEnabled;
	}
	
	public void setSloEnabled(Boolean sloEnabled) {
		this.sloEnabled = sloEnabled;
	}
	
	/**
	 * SSO user creation mode
	 * @return String either 'real' or 'virtual'. Default: 'real'
	 */
	public String getCreationMode() {
		if (creationMode == null || creationMode != "virtual" || creationMode != "real" ) return "real";
		return creationMode;
	}
	
	public void setCreationMode(String creationMode) {
		this.creationMode = creationMode;
	}
	
	/**
	 * Path to SAML init action
	 * @return String
	 */
	public String getInitPath() {
		if (initPath == null || initPath.isEmpty()) return "/maestrano/auth/saml/init.jsp";
		return initPath;
	}
	
	public void setInitPath(String initPath) {
		this.initPath = initPath;
	}
	
	/**
	 * Path to SAML consume action
	 * @return
	 */
	public String getConsumePath() {
		if (consumePath == null || consumePath.isEmpty()) return "/maestrano/auth/saml/consume.jsp";
		return consumePath;
	}
	
	public void setConsumePath(String consumePath) {
		this.consumePath = consumePath;
	}
	
	/**
	 * Host name of your application identity manager (if applicable)
	 * @return String the configured idm, or application host or localhost if nothing set
	 */
	public String getIdm() {
		if (idm == null || idm.isEmpty()) {
			if (Maestrano.appService().getHost() == null || Maestrano.appService().getHost().isEmpty()) {
				return "http://localhost";
			} else {
				return Maestrano.appService().getHost();
			}
		}
		return idm;
	}
	
	public void setIdm(String idm) {
		this.idm = idm;
	}
	
	/**
	 * Return the address of the identity provider. Unless this property has been set manually
	 * it returns the production idp (maestrano.com) or test fingerprint (api-sandbox) based on the current environment
	 * (See Maestrano.appService().getEnvironment())
	 * 
	 * @return String Identity Provider address
	 */
	public String getIdp() {
		if (idp == null || idp.isEmpty()) {
			if (Maestrano.appService().getEnvironment().equals("production")) {
				return "https://maestrano.com";
			} else {
				return "http://api-sandbox.maestrano.io";
			}
		}
		return idp;
	}
	
	public void setIdp(String idp) {
		this.idp = idp;
	}
	
	/**
	 * Return the SAML Name ID Format. Currently nameid-format:persistent
	 * @return
	 */
	public String getNameIdFormat() {
		if (nameIdFormat == null || nameIdFormat.isEmpty()) return "urn:oasis:names:tc:SAML:2.0:nameid-format:persistent";
		return nameIdFormat;
	}
	
	public void setNameIdFormat(String nameIdFormat) {
		this.nameIdFormat = nameIdFormat;
	}
	
	/**
	 * Return the certificate fingerprint used by SAML. Unless this property has been set manually
	 * it returns the production fingerprint or test (sandbox) fingerprint based on the current environment
	 * (See Maestrano.appService().getEnvironment())
	 * @return String certificate fingerprint
	 */
	public String getX509Fingerprint() {
		if (x509Fingerprint == null || x509Fingerprint.isEmpty()) {
			if (Maestrano.appService().getEnvironment().equals("production")) {
				return "2f:57:71:e4:40:19:57:37:a6:2c:f0:c5:82:52:2f:2e:41:b7:9d:7e";
			} else {
				return "01:06:15:89:25:7d:78:12:28:a6:69:c7:de:63:ed:74:21:f9:f5:36";
			}
		}
		return x509Fingerprint;
	}
	
	public void setX509Fingerprint(String x509Fingerprint) {
		this.x509Fingerprint = x509Fingerprint;
	}
	
	/**
	 * Return the public certificate used for SAML SSO. Unless this property has been set manually
	 * it returns the production certificate or test (sandbox) fingerprint based on the current environment
	 * (See Maestrano.appService().getEnvironment())
	 * @return String certificate fingerprint
	 */
	public String getX509Certificate() {
		if (x509Certificate == null || x509Certificate.isEmpty()) {
			if (Maestrano.appService().getEnvironment().equals("production")) {
				return "MIIDezCCAuSgAwIBAgIJAPFpcH2rW0pyMA0GCSqGSIb3DQEBBQUAMIGGMQswCQYD\nVQQGEwJBVTEMMAoGA1UECBMDTlNXMQ8wDQYDVQQHEwZTeWRuZXkxGjAYBgNVBAoT\nEU1hZXN0cmFubyBQdHkgTHRkMRYwFAYDVQQDEw1tYWVzdHJhbm8uY29tMSQwIgYJ\nKoZIhvcNAQkBFhVzdXBwb3J0QG1hZXN0cmFuby5jb20wHhcNMTQwMTA0MDUyNDEw\nWhcNMzMxMjMwMDUyNDEwWjCBhjELMAkGA1UEBhMCQVUxDDAKBgNVBAgTA05TVzEP\nMA0GA1UEBxMGU3lkbmV5MRowGAYDVQQKExFNYWVzdHJhbm8gUHR5IEx0ZDEWMBQG\nA1UEAxMNbWFlc3RyYW5vLmNvbTEkMCIGCSqGSIb3DQEJARYVc3VwcG9ydEBtYWVz\ndHJhbm8uY29tMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQD3feNNn2xfEz5/\nQvkBIu2keh9NNhobpre8U4r1qC7h7OeInTldmxGL4cLHw4ZAqKbJVrlFWqNevM5V\nZBkDe4mjuVkK6rYK1ZK7eVk59BicRksVKRmdhXbANk/C5sESUsQv1wLZyrF5Iq8m\na9Oy4oYrIsEF2uHzCouTKM5n+O4DkwIDAQABo4HuMIHrMB0GA1UdDgQWBBSd/X0L\n/Pq+ZkHvItMtLnxMCAMdhjCBuwYDVR0jBIGzMIGwgBSd/X0L/Pq+ZkHvItMtLnxM\nCAMdhqGBjKSBiTCBhjELMAkGA1UEBhMCQVUxDDAKBgNVBAgTA05TVzEPMA0GA1UE\nBxMGU3lkbmV5MRowGAYDVQQKExFNYWVzdHJhbm8gUHR5IEx0ZDEWMBQGA1UEAxMN\nbWFlc3RyYW5vLmNvbTEkMCIGCSqGSIb3DQEJARYVc3VwcG9ydEBtYWVzdHJhbm8u\nY29tggkA8WlwfatbSnIwDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQUFAAOBgQDE\nhe/18oRh8EqIhOl0bPk6BG49AkjhZZezrRJkCFp4dZxaBjwZTddwo8O5KHwkFGdy\nyLiPV326dtvXoKa9RFJvoJiSTQLEn5mO1NzWYnBMLtrDWojOe6Ltvn3x0HVo/iHh\nJShjAn6ZYX43Tjl1YXDd1H9O+7/VgEWAQQ32v8p5lA==";
			} else {
				return "MIIDezCCAuSgAwIBAgIJAOehBr+YIrhjMA0GCSqGSIb3DQEBBQUAMIGGMQswCQYD\nVQQGEwJBVTEMMAoGA1UECBMDTlNXMQ8wDQYDVQQHEwZTeWRuZXkxGjAYBgNVBAoT\nEU1hZXN0cmFubyBQdHkgTHRkMRYwFAYDVQQDEw1tYWVzdHJhbm8uY29tMSQwIgYJ\nKoZIhvcNAQkBFhVzdXBwb3J0QG1hZXN0cmFuby5jb20wHhcNMTQwMTA0MDUyMjM5\nWhcNMzMxMjMwMDUyMjM5WjCBhjELMAkGA1UEBhMCQVUxDDAKBgNVBAgTA05TVzEP\nMA0GA1UEBxMGU3lkbmV5MRowGAYDVQQKExFNYWVzdHJhbm8gUHR5IEx0ZDEWMBQG\nA1UEAxMNbWFlc3RyYW5vLmNvbTEkMCIGCSqGSIb3DQEJARYVc3VwcG9ydEBtYWVz\ndHJhbm8uY29tMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDVkIqo5t5Paflu\nP2zbSbzxn29n6HxKnTcsubycLBEs0jkTkdG7seF1LPqnXl8jFM9NGPiBFkiaR15I\n5w482IW6mC7s8T2CbZEL3qqQEAzztEPnxQg0twswyIZWNyuHYzf9fw0AnohBhGu2\n28EZWaezzT2F333FOVGSsTn1+u6tFwIDAQABo4HuMIHrMB0GA1UdDgQWBBSvrNxo\neHDm9nhKnkdpe0lZjYD1GzCBuwYDVR0jBIGzMIGwgBSvrNxoeHDm9nhKnkdpe0lZ\njYD1G6GBjKSBiTCBhjELMAkGA1UEBhMCQVUxDDAKBgNVBAgTA05TVzEPMA0GA1UE\nBxMGU3lkbmV5MRowGAYDVQQKExFNYWVzdHJhbm8gUHR5IEx0ZDEWMBQGA1UEAxMN\nbWFlc3RyYW5vLmNvbTEkMCIGCSqGSIb3DQEJARYVc3VwcG9ydEBtYWVzdHJhbm8u\nY29tggkA56EGv5giuGMwDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQUFAAOBgQCc\nMPgV0CpumKRMulOeZwdpnyLQI/NTr3VVHhDDxxCzcB0zlZ2xyDACGnIG2cQJJxfc\n2GcsFnb0BMw48K6TEhAaV92Q7bt1/TYRvprvhxUNMX2N8PHaYELFG2nWfQ4vqxES\nRkjkjqy+H7vir/MOF3rlFjiv5twAbDKYHXDT7v1YCg==";
			}
		}
		return x509Certificate;
	}
	
	public void setX509Certificate(String x509Certificate) {
		this.x509Certificate = x509Certificate;
	}
	
	/**
	 * Return the full Idp SSO endpoint
	 * @return 
	 */
	public String getIdpUrl() {
		return this.getIdp() + "/api/v1/auth/saml";
	}
	
	/**
	 * Return the application SSO initialization endpoint
	 * @return String initialization endpoint
	 */
	public String getInitUrl() {
		return this.getIdm() + this.getInitPath();
	}
	
	/**
	 * Return the application SSO consume endpoint
	 * @return String consume endpoint
	 */
	public String getConsumeUrl() {
		return this.getIdm() + this.getConsumePath();
	}
	
	/**
	 * Return the Idp logout url where users should be redirected to
	 * upon logging out
	 * @return String Idp logout url
	 */
	public String getLogoutUrl() {
		return this.getIdp() + "/app_logout";
	}
	
	/**
	 * Return the Idp access not granted url where unauthorized users should 
	 * be redirected to
	 * 
	 * @return String Idp access 
	 */
	public String getUnauthorizedUrl() {
		return this.getIdp() + "/app_access_unauthorized";
	}
	
	/**
	 * 
	 * @return
	 */
	
	/**
	 * Return the endpoint used for remote session check (used for single logout)
	 * @param userUid Maestrano User UID
	 * @param sessionToken Maestrano User session token
	 * @return String the endpoint to reach
	 */
	public String getSessionCheckUrl(String userUid, String sessionToken) {
		String url = this.getIdpUrl();
		url += "/" + userUid + "?session=" + sessionToken;
		
		return url;
	}
	
	
}
