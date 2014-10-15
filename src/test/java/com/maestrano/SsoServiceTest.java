package com.maestrano;

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
		Maestrano.configure(props);
		subject = Maestrano.ssoService();
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
	
}
