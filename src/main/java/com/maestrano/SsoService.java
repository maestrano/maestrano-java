package com.maestrano;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.maestrano.helpers.MnoPropertiesHelper;

/**
 * Maestrano SSO Service, related to all SSO interaction
 */
public class SsoService {
	private final ApiService apiService;

	private final boolean enabled;
	private final boolean sloEnabled;
	private final String creationMode;
	private final String initPath;
	private final String consumePath;
	private final String idm;
	private final String idp;
	private final String nameIdFormat;
	private final String x509Fingerprint;
	private final String x509Certificate;

	// Package Private Constructor
	SsoService(ApiService apiService, AppService appService, Properties props) {
		this.apiService = apiService;
		this.enabled = MnoPropertiesHelper.getBooleanProperty("sso.enabled", props);
		this.sloEnabled = MnoPropertiesHelper.getBooleanProperty("sso.sloEnabled", props);
		this.creationMode = MnoPropertiesHelper.getProperty("sso.creationMode", props);
		this.initPath = MnoPropertiesHelper.getProperty("sso.initPath", props);
		this.consumePath = MnoPropertiesHelper.getProperty("sso.consumePath", props);
		this.idm = getIdm(appService, props);
		this.idp = getIdp(appService, props);
		this.nameIdFormat = MnoPropertiesHelper.getProperty("sso.nameIdFormat", props);
		this.x509Fingerprint = getX509Fingerprint(appService, props);
		this.x509Certificate = getX509Certificate(appService, props);

	}

	/**
	 * Is Single Sign-On enabled - true by default
	 * 
	 * @return Boolean
	 */
	public boolean getEnabled() {
		return enabled;
	}

	/**
	 * Is Single Logout enabled - true by default
	 * 
	 * @return Boolean
	 */
	public Boolean getSloEnabled() {
		return sloEnabled;
	}

	/**
	 * Return the API application id (used as issuer)
	 * 
	 * @return String application id
	 */
	public String getIssuer() {
		return apiService.getId();
	}

	/**
	 * SSO user creation mode
	 * 
	 * @return String either 'real' or 'virtual'. Default: 'real'
	 */
	public String getCreationMode() {
		return creationMode;
	}

	/**
	 * Path to SAML init action
	 * 
	 * @return String
	 */
	public String getInitPath() {
		return initPath;
	}

	/**
	 * Path to SAML consume action
	 * 
	 * @return
	 */
	public String getConsumePath() {
		return consumePath;
	}

	/**
	 * Host name of your application identity manager (if applicable)
	 * 
	 * @return String the configured idm, or application host or localhost if nothing set
	 */
	public String getIdm() {
		return idm;
	}

	private static String getIdm(AppService appService, Properties properties) {
		String idm = properties.getProperty("sso.idm");
		if (!MnoPropertiesHelper.isNullOrEmpty(idm)) {
			return idm;
		} else {
			String host = appService.getHost();
			if (MnoPropertiesHelper.isNullOrEmpty(host)) {
				return "http://localhost";
			} else {
				return host;
			}
		}
	}

	/**
	 * Return the address of the identity provider. Unless this property has been set manually it returns the production idp (maestrano.com) or test fingerprint (api-sandbox) based on the current
	 * environment (See Maestrano.appService().getEnvironment())
	 * 
	 * @return String Identity Provider address
	 */
	public String getIdp() {
		return idp;
	}

	private static String getIdp(AppService appService, Properties properties) {
		String idp = properties.getProperty("sso.idp");
		if (!MnoPropertiesHelper.isNullOrEmpty(idp)) {
			return idp;
		} else {
			if (appService.isProduction()) {
				return "https://maestrano.com";
			} else {
				return "http://api-sandbox.maestrano.io";
			}
		}
	}

	/**
	 * Return the SAML Name ID Format. Currently nameid-format:persistent
	 * 
	 * @return
	 */
	public String getNameIdFormat() {
		return nameIdFormat;
	}

	/**
	 * Return the certificate fingerprint used by SAML. Unless this property has been set manually it returns the production fingerprint or test (sandbox) fingerprint based on the current environment
	 * (See Maestrano.appService().getEnvironment())
	 * 
	 * @return String certificate fingerprint
	 */
	public String getX509Fingerprint() {
		return x509Fingerprint;
	}

	private static String getX509Fingerprint(AppService appService, Properties properties) {
		String x509Fingerprint = properties.getProperty("sso.x509Fingerprint");
		if (!MnoPropertiesHelper.isNullOrEmpty(x509Fingerprint)) {
			return x509Fingerprint;
		} else {
			if (appService.isProduction()) {
				return "2f:57:71:e4:40:19:57:37:a6:2c:f0:c5:82:52:2f:2e:41:b7:9d:7e";
			} else {
				return "01:06:15:89:25:7d:78:12:28:a6:69:c7:de:63:ed:74:21:f9:f5:36";
			}
		}
	}

	/**
	 * Return the public certificate used for SAML SSO. Unless this property has been set manually it returns the production certificate or test (sandbox) fingerprint based on the current environment
	 * (See Maestrano.appService().getEnvironment())
	 * 
	 * @return String certificate fingerprint
	 */
	public String getX509Certificate() {
		return x509Certificate;
	}

	/**
	 * Return the public certificate used for SAML SSO. Unless this property has been set manually it returns the production certificate or test (sandbox) fingerprint based on the current environment
	 * (See Maestrano.appService().getEnvironment())
	 * 
	 * @return String certificate fingerprint
	 */
	private static String getX509Certificate(AppService appService, Properties properties) {
		String x509Certificate = properties.getProperty("sso.x509Certificate");
		if (!MnoPropertiesHelper.isNullOrEmpty(x509Certificate)) {
			return x509Certificate;
		} else {
			if (appService.isProduction()) {
				return "MIIDezCCAuSgAwIBAgIJAPFpcH2rW0pyMA0GCSqGSIb3DQEBBQUAMIGGMQswCQYD\nVQQGEwJBVTEMMAoGA1UECBMDTlNXMQ8wDQYDVQQHEwZTeWRuZXkxGjAYBgNVBAoT\nEU1hZXN0cmFubyBQdHkgTHRkMRYwFAYDVQQDEw1tYWVzdHJhbm8uY29tMSQwIgYJ\nKoZIhvcNAQkBFhVzdXBwb3J0QG1hZXN0cmFuby5jb20wHhcNMTQwMTA0MDUyNDEw\nWhcNMzMxMjMwMDUyNDEwWjCBhjELMAkGA1UEBhMCQVUxDDAKBgNVBAgTA05TVzEP\nMA0GA1UEBxMGU3lkbmV5MRowGAYDVQQKExFNYWVzdHJhbm8gUHR5IEx0ZDEWMBQG\nA1UEAxMNbWFlc3RyYW5vLmNvbTEkMCIGCSqGSIb3DQEJARYVc3VwcG9ydEBtYWVz\ndHJhbm8uY29tMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQD3feNNn2xfEz5/\nQvkBIu2keh9NNhobpre8U4r1qC7h7OeInTldmxGL4cLHw4ZAqKbJVrlFWqNevM5V\nZBkDe4mjuVkK6rYK1ZK7eVk59BicRksVKRmdhXbANk/C5sESUsQv1wLZyrF5Iq8m\na9Oy4oYrIsEF2uHzCouTKM5n+O4DkwIDAQABo4HuMIHrMB0GA1UdDgQWBBSd/X0L\n/Pq+ZkHvItMtLnxMCAMdhjCBuwYDVR0jBIGzMIGwgBSd/X0L/Pq+ZkHvItMtLnxM\nCAMdhqGBjKSBiTCBhjELMAkGA1UEBhMCQVUxDDAKBgNVBAgTA05TVzEPMA0GA1UE\nBxMGU3lkbmV5MRowGAYDVQQKExFNYWVzdHJhbm8gUHR5IEx0ZDEWMBQGA1UEAxMN\nbWFlc3RyYW5vLmNvbTEkMCIGCSqGSIb3DQEJARYVc3VwcG9ydEBtYWVzdHJhbm8u\nY29tggkA8WlwfatbSnIwDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQUFAAOBgQDE\nhe/18oRh8EqIhOl0bPk6BG49AkjhZZezrRJkCFp4dZxaBjwZTddwo8O5KHwkFGdy\nyLiPV326dtvXoKa9RFJvoJiSTQLEn5mO1NzWYnBMLtrDWojOe6Ltvn3x0HVo/iHh\nJShjAn6ZYX43Tjl1YXDd1H9O+7/VgEWAQQ32v8p5lA==";
			} else {
				return "MIIDezCCAuSgAwIBAgIJAOehBr+YIrhjMA0GCSqGSIb3DQEBBQUAMIGGMQswCQYD\nVQQGEwJBVTEMMAoGA1UECBMDTlNXMQ8wDQYDVQQHEwZTeWRuZXkxGjAYBgNVBAoT\nEU1hZXN0cmFubyBQdHkgTHRkMRYwFAYDVQQDEw1tYWVzdHJhbm8uY29tMSQwIgYJ\nKoZIhvcNAQkBFhVzdXBwb3J0QG1hZXN0cmFuby5jb20wHhcNMTQwMTA0MDUyMjM5\nWhcNMzMxMjMwMDUyMjM5WjCBhjELMAkGA1UEBhMCQVUxDDAKBgNVBAgTA05TVzEP\nMA0GA1UEBxMGU3lkbmV5MRowGAYDVQQKExFNYWVzdHJhbm8gUHR5IEx0ZDEWMBQG\nA1UEAxMNbWFlc3RyYW5vLmNvbTEkMCIGCSqGSIb3DQEJARYVc3VwcG9ydEBtYWVz\ndHJhbm8uY29tMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDVkIqo5t5Paflu\nP2zbSbzxn29n6HxKnTcsubycLBEs0jkTkdG7seF1LPqnXl8jFM9NGPiBFkiaR15I\n5w482IW6mC7s8T2CbZEL3qqQEAzztEPnxQg0twswyIZWNyuHYzf9fw0AnohBhGu2\n28EZWaezzT2F333FOVGSsTn1+u6tFwIDAQABo4HuMIHrMB0GA1UdDgQWBBSvrNxo\neHDm9nhKnkdpe0lZjYD1GzCBuwYDVR0jBIGzMIGwgBSvrNxoeHDm9nhKnkdpe0lZ\njYD1G6GBjKSBiTCBhjELMAkGA1UEBhMCQVUxDDAKBgNVBAgTA05TVzEPMA0GA1UE\nBxMGU3lkbmV5MRowGAYDVQQKExFNYWVzdHJhbm8gUHR5IEx0ZDEWMBQGA1UEAxMN\nbWFlc3RyYW5vLmNvbTEkMCIGCSqGSIb3DQEJARYVc3VwcG9ydEBtYWVzdHJhbm8u\nY29tggkA56EGv5giuGMwDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQUFAAOBgQCc\nMPgV0CpumKRMulOeZwdpnyLQI/NTr3VVHhDDxxCzcB0zlZ2xyDACGnIG2cQJJxfc\n2GcsFnb0BMw48K6TEhAaV92Q7bt1/TYRvprvhxUNMX2N8PHaYELFG2nWfQ4vqxES\nRkjkjqy+H7vir/MOF3rlFjiv5twAbDKYHXDT7v1YCg==";
			}
		}
	}

	/**
	 * Return the full Idp SSO endpoint
	 * 
	 * @return
	 */
	public String getIdpUrl() {
		return this.getIdp() + "/api/v1/auth/saml";
	}

	/**
	 * Return the application SSO initialization endpoint
	 * 
	 * @return String initialization endpoint
	 */
	public String getInitUrl() {
		return this.getIdm() + this.getInitPath();
	}

	/**
	 * Return the application SSO consume endpoint
	 * 
	 * @return String consume endpoint
	 */
	public String getConsumeUrl() {
		return this.getIdm() + this.getConsumePath();
	}

	/**
	 * Return the Idp logout url where users should be redirected to upon logging out
	 * 
	 * @return String Idp logout url
	 */
	public String getLogoutUrl() {
		return this.getIdp() + "/app_logout";
	}

	/**
	 * Return the Idp access not granted url where unauthorized users should be redirected to
	 * 
	 * @return String Idp access
	 */
	public String getUnauthorizedUrl() {
		return this.getIdp() + "/app_access_unauthorized";
	}

	/**
	 * Return the endpoint used for remote session check (used for single logout)
	 * 
	 * @param userUid
	 *            Maestrano User UID
	 * @param sessionToken
	 *            Maestrano User session token
	 * @return String the endpoint to reach
	 */
	public String getSessionCheckUrl(String userUid, String sessionToken) {
		return this.getIdpUrl() + "/" + userUid + "?session=" + sessionToken;
	}

	public com.maestrano.saml.Settings getSamlSettings() {
		return new com.maestrano.saml.Settings(this.getConsumeUrl(), this.getIssuer(), this.getIdpUrl(), this.getX509Certificate(), this.getNameIdFormat());
	}

	public Map<String, String> toMetadataHash() {
		Map<String, String> hash = new HashMap<String, String>();
		hash.put("enabled", Boolean.toString(getEnabled()));
		hash.put("creation_mode", getCreationMode());
		hash.put("init_path", getInitPath());
		hash.put("consume_path", getConsumePath());
		hash.put("idm", getIdm());
		hash.put("idp", getIdp());
		hash.put("name_id_format", getNameIdFormat());
		hash.put("x509_fingerprint", getX509Fingerprint());
		hash.put("x509_certificate", getX509Certificate());

		return hash;
	}
}
