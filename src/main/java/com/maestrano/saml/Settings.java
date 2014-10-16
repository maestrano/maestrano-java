package com.maestrano.saml;

public class Settings {
	private String assertionConsumerServiceUrl;
	private String issuer;
	private String idpSsoTargetUrl;
	private String idpCertificate;
	private String nameIdentifierFormat;
	
	public Settings(String acsUrl, String issuer, String targetUrl, String cert, String nameIdFormat) {
		this.assertionConsumerServiceUrl = acsUrl;
		this.issuer = issuer;
		this.idpSsoTargetUrl = targetUrl;
		this.idpCertificate = cert;
		this.nameIdentifierFormat = nameIdFormat;
	}
	
	/**
	 * Application return URL
	 * @return String consumer service url
	 */
	public String getAssertionConsumerServiceUrl() {
		return assertionConsumerServiceUrl;
	}
	
	public void setAssertionConsumerServiceUrl(String assertionConsumerServiceUrl) {
		this.assertionConsumerServiceUrl = assertionConsumerServiceUrl;
	}
	
	/**
	 * SAML application Id
	 * @return String saml application id
	 */
	public String getIssuer() {
		return issuer;
	}
	
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	
	/**
	 * Url of the SAML Identity Provider
	 * @return String saml idp url
	 */
	public String getIdpSsoTargetUrl() {
		return idpSsoTargetUrl;
	}
	
	public void setIdpSsoTargetUrl(String idpSsoTargetUrl) {
		this.idpSsoTargetUrl = idpSsoTargetUrl;
	}
	
	/**
	 * Return the public certificate used to sign the SAML request
	 * @return
	 */
	public String getIdpCertificate() {
		return idpCertificate;
	}
	
	public void setIdpCertificate(String idpCertificate) {
		this.idpCertificate = idpCertificate;
	}
	
	/**
	 * The user id type (uid,email etc.)
	 * @return String id type
	 */
	public String getNameIdentifierFormat() {
		return nameIdentifierFormat;
	}
	
	public void setNameIdentifierFormat(String nameIdentifierFormat) {
		this.nameIdentifierFormat = nameIdentifierFormat;
	}
}
