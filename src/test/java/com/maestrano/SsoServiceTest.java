package com.maestrano;

import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SsoServiceTest {
	private Properties props = new Properties();
	private SsoService subject;
	
	@Before
	public void beforeEach() {
		props.setProperty("app.environment", "production");
		props.setProperty("api.id", "someid");
		props.setProperty("api.key", "somekey");
		Maestrano.configure(props);
		subject = Maestrano.ssoService();
	}
	
	@Test
	public void getIssuer_itReturnsTheRightValue() {
		assertEquals(props.getProperty("api.id"),subject.getIssuer());
	}
	
	@Test
	public void getSessionCheckUrl_itReturnsTheRightUrl() {
		String uid = "usr-1";
		String session = "somesessiontoken";
		String expected = "https://maestrano.com/api/v1/auth/saml/" + uid + "?session=" + session;
		
		assertEquals(expected,subject.getSessionCheckUrl(uid, session));
	}
	
	@Test
	public void getIdpUrl_itReturnsTheRightUrl() {
		String expected = "https://maestrano.com/api/v1/auth/saml";
		assertEquals(expected, subject.getIdpUrl());
	}
	
	@Test
	public void getinitUrl_itReturnsTheRightUrl() {
		String host = "https://mysuperapp.com";
		String path = "/mno/start-sso";
		props.setProperty("app.host",host);
		props.setProperty("sso.initPath",path);
		Maestrano.configure(props);
		
		String expected = host + path;
		assertEquals(expected, subject.getInitUrl());
	}
	
	@Test
	public void getConsumeUrl_itReturnsTheRightUrl() {
		String host = "https://mysuperapp.com";
		String path = "/mno/consume-sso";
		props.setProperty("app.host",host);
		props.setProperty("sso.consumePath", path);
		Maestrano.configure(props);
		
		String expected = host + path;
		assertEquals(expected, subject.getConsumeUrl());
	}
	
	@Test
	public void getLogoutUrl_itReturnsTheRightUrl() {
		String expected = "https://maestrano.com/app_logout";
		assertEquals(expected, subject.getLogoutUrl());
	}
	
	@Test
	public void getUnauthorizedUrl_itReturnsTheRightUrl() {
		String expected = "https://maestrano.com/app_access_unauthorized";
		assertEquals(expected, subject.getUnauthorizedUrl());
	}
	
	@Test
	public void getX509Certificate_itReturnsTheRightProductionValue() {
		String expected = "MIIDezCCAuSgAwIBAgIJAPFpcH2rW0pyMA0GCSqGSIb3DQEBBQUAMIGGMQswCQYD\nVQQGEwJBVTEMMAoGA1UECBMDTlNXMQ8wDQYDVQQHEwZTeWRuZXkxGjAYBgNVBAoT\nEU1hZXN0cmFubyBQdHkgTHRkMRYwFAYDVQQDEw1tYWVzdHJhbm8uY29tMSQwIgYJ\nKoZIhvcNAQkBFhVzdXBwb3J0QG1hZXN0cmFuby5jb20wHhcNMTQwMTA0MDUyNDEw\nWhcNMzMxMjMwMDUyNDEwWjCBhjELMAkGA1UEBhMCQVUxDDAKBgNVBAgTA05TVzEP\nMA0GA1UEBxMGU3lkbmV5MRowGAYDVQQKExFNYWVzdHJhbm8gUHR5IEx0ZDEWMBQG\nA1UEAxMNbWFlc3RyYW5vLmNvbTEkMCIGCSqGSIb3DQEJARYVc3VwcG9ydEBtYWVz\ndHJhbm8uY29tMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQD3feNNn2xfEz5/\nQvkBIu2keh9NNhobpre8U4r1qC7h7OeInTldmxGL4cLHw4ZAqKbJVrlFWqNevM5V\nZBkDe4mjuVkK6rYK1ZK7eVk59BicRksVKRmdhXbANk/C5sESUsQv1wLZyrF5Iq8m\na9Oy4oYrIsEF2uHzCouTKM5n+O4DkwIDAQABo4HuMIHrMB0GA1UdDgQWBBSd/X0L\n/Pq+ZkHvItMtLnxMCAMdhjCBuwYDVR0jBIGzMIGwgBSd/X0L/Pq+ZkHvItMtLnxM\nCAMdhqGBjKSBiTCBhjELMAkGA1UEBhMCQVUxDDAKBgNVBAgTA05TVzEPMA0GA1UE\nBxMGU3lkbmV5MRowGAYDVQQKExFNYWVzdHJhbm8gUHR5IEx0ZDEWMBQGA1UEAxMN\nbWFlc3RyYW5vLmNvbTEkMCIGCSqGSIb3DQEJARYVc3VwcG9ydEBtYWVzdHJhbm8u\nY29tggkA8WlwfatbSnIwDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQUFAAOBgQDE\nhe/18oRh8EqIhOl0bPk6BG49AkjhZZezrRJkCFp4dZxaBjwZTddwo8O5KHwkFGdy\nyLiPV326dtvXoKa9RFJvoJiSTQLEn5mO1NzWYnBMLtrDWojOe6Ltvn3x0HVo/iHh\nJShjAn6ZYX43Tjl1YXDd1H9O+7/VgEWAQQ32v8p5lA==";
		assertEquals(expected, subject.getX509Certificate());
	}
	
	@Test
	public void getX509Fingerprint_itReturnsTheRightProductionValue() {
		String expected = "2f:57:71:e4:40:19:57:37:a6:2c:f0:c5:82:52:2f:2e:41:b7:9d:7e";
		assertEquals(expected, subject.getX509Fingerprint());
	}
	
	@Test
	public void getSamlSettings_itReturnsTheRightObject() {
		com.maestrano.saml.Settings settings = subject.getSamlSettings();
		
		assertEquals(subject.getConsumeUrl(), settings.getAssertionConsumerServiceUrl());
		assertEquals(subject.getIdpUrl(), settings.getIdpSsoTargetUrl());
		assertEquals(subject.getX509Certificate(), settings.getIdpCertificate());
		assertEquals(Maestrano.apiService().getId(), settings.getIssuer());
		assertEquals(subject.getNameIdFormat(), settings.getNameIdentifierFormat());
	}
	
	@Test
	public void toMetadataHash_itReturnsTheRightValue() {
		Map<String,String> hash = subject.toMetadataHash();
		assertEquals(subject.getEnabled().toString(), hash.get("enabled"));
		assertEquals(subject.getCreationMode(), hash.get("creation_mode"));
		assertEquals(subject.getInitPath(), hash.get("init_path"));
		assertEquals(subject.getConsumePath(), hash.get("consume_path"));
		assertEquals(subject.getIdm(), hash.get("idm"));
		assertEquals(subject.getIdp(), hash.get("idp"));
		assertEquals(subject.getNameIdFormat(), hash.get("name_id_format"));
		assertEquals(subject.getX509Fingerprint(), hash.get("x509_fingerprint"));
		assertEquals(subject.getX509Certificate(), hash.get("x509_certificate"));
	}
}
