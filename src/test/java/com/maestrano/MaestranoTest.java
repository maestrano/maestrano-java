package com.maestrano;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.xml.bind.DatatypeConverter;

import org.junit.Before;
import org.junit.Test;

import com.maestrano.testhelpers.HttpRequestStub;

public class MaestranoTest {
	private Properties props = new Properties();
	
	@Before
	public void beforeEach() {
		props.setProperty("app.environment", "production");
		props.setProperty("app.host", "https://mysuperapp.com");
		props.setProperty("api.id", "someid");
		props.setProperty("api.key", "somekey");
		Maestrano.configure(props);
	}
	
	@Test
	public void authenticate_withCredentials_itReturnsTrueWithRightCredentials() {
		assertTrue(Maestrano.authenticate(props.getProperty("api.id"),props.getProperty("api.key")));
	}
	
	@Test
	public void authenticate_withCredentials_itReturnsFalseWithWrongCredentials() {
		assertFalse(Maestrano.authenticate(props.getProperty("api.id"),props.getProperty("api.key") + "aa"));
		assertFalse(Maestrano.authenticate(props.getProperty("api.id") + "aa",props.getProperty("api.key")));
	}
	
	@Test
	public void authenticate_withRequest_itReturnsTrueWithRightBasicAuth() throws UnsupportedEncodingException {
		HttpRequestStub request = new HttpRequestStub();
		String authStr = props.getProperty("api.id") + ":" + props.getProperty("api.key");
		authStr = "Basic " + DatatypeConverter.printBase64Binary(authStr.getBytes());
		request.setHeader("Authorization", authStr);
		
		assertTrue(Maestrano.authenticate(request));
	}
	
	@Test
	public void authenticate_withRequest_itReturnsTrueWithWrongBasicAuth() throws UnsupportedEncodingException {
		HttpRequestStub request = new HttpRequestStub();
		String authStr = props.getProperty("api.id") + ":" + props.getProperty("api.key") + "aaa";
		authStr = "Basic " + DatatypeConverter.printBase64Binary(authStr.getBytes());
		request.setHeader("Authorization", authStr);
		
		assertFalse(Maestrano.authenticate(request));
	}
}
