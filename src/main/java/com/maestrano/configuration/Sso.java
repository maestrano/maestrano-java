package com.maestrano.configuration;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.exception.MnoException;
import com.maestrano.helpers.MnoPropertiesHelper;
import com.maestrano.saml.Response;
import com.maestrano.sso.User;

/**
 * Maestrano SSO Configuration, related to all SSO interaction
 */
public class Sso {

	private final static String NAME_ID_FORMAT = "urn:oasis:names:tc:SAML:2.0:nameid-format:persistent";
	
	private final String issuer;
	private final String initPath;
	private final String consumePath;
	private final String idm;
	private final String idp;
	private final String x509Fingerprint;
	private final String x509Certificate;

	// Package Private Constructor
	Sso(Api api, App app, Properties props) throws MnoConfigurationException {
		this.initPath = MnoPropertiesHelper.getProperty(props, "sso.initPath");
		this.consumePath = MnoPropertiesHelper.getProperty(props, "sso.consumePath");
		this.issuer = api.getId();
		String idm = MnoPropertiesHelper.getPropertyOrDefault(props, "sso.idm", null);
		if (MnoPropertiesHelper.isNullOrEmpty(idm)) {
			idm = app.getHost();
		}
		this.idm = idm;
		this.idp = MnoPropertiesHelper.getProperty(props, "sso.idp");
		this.x509Fingerprint = MnoPropertiesHelper.getProperty(props, "sso.x509Fingerprint");
		this.x509Certificate = MnoPropertiesHelper.getProperty(props, "sso.x509Certificate");
	}


	/**
	 * Return the API application id (used as issuer)
	 * 
	 * @return String application id
	 */
	public String getIssuer() {
		return issuer;
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
	 * @return String the configured idm, or application host if not defined
	 */
	public String getIdm() {
		return idm;
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


	/**
	 * Return the certificate fingerprint used by SAML. Unless this property has been set manually it returns the production fingerprint or test (sandbox) fingerprint based on the current environment
	 * (See Maestrano.appService().getEnvironment())
	 * 
	 * @return String certificate fingerprint
	 */
	public String getX509Fingerprint() {
		return x509Fingerprint;
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
	 * @param user
	 *            the user currently logged id
	 * @return the Idp logout url where users should be redirected to upon logging out
	 */
	public String getLogoutUrl(User user) {
		return getLogoutUrl(user.getUid());
	}

	/**
	 * @param userUid
	 *            the user currently logged id UID
	 * @return the Idp logout url where users should be redirected to upon logging out
	 */
	public String getLogoutUrl(String userUid) {
		return this.getIdp() + "/app_logout?user_uid=" + userUid;
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
		return new com.maestrano.saml.Settings(this.getConsumeUrl(), this.getIssuer(), this.getIdpUrl(), this.getX509Certificate(), NAME_ID_FORMAT);
	}

	/**
	 * Build a {@linkplain Response} with the provided base64 encoded XML string
	 * 
	 * @param samlResponse
	 * @return
	 * @throws MnoException
	 */
	public Response buildResponse(String samlResponse) throws MnoException {
		try {
			return Response.loadFromBase64XML(this, samlResponse);
		} catch (Exception e) {
			throw new MnoException("Could not build Response from samlResponse: " + samlResponse, e);
		}
	}
	
	public Map<String, String> toMetadataHash() {
		Map<String, String> hash = new LinkedHashMap<String, String>();
		hash.put("init_path", getInitPath());
		hash.put("consume_path", getConsumePath());
		hash.put("idm", getIdm());
		hash.put("idp", getIdp());
		hash.put("x509_fingerprint", getX509Fingerprint());
		hash.put("x509_certificate", getX509Certificate());

		return hash;
	}
}
