package com.maestrano;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.DatatypeConverter;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.maestrano.testhelpers.HttpRequestStub;

public class MaestranoTest {
    private Properties defaultProps = new Properties();
    private Properties otherProps = new Properties();
	
	@Before
	public void beforeEach() {
	    defaultProps.setProperty("app.environment", "production");
        defaultProps.setProperty("app.host", "https://mysuperapp.com");
        defaultProps.setProperty("api.id", "someid");
        defaultProps.setProperty("api.key", "somekey");
        Maestrano.configure(defaultProps);
        
        otherProps.setProperty("app.environment", "production");
        otherProps.setProperty("app.host", "https://myotherapp.com");
        otherProps.setProperty("api.id", "otherid");
        otherProps.setProperty("api.key", "otherkey");
        Maestrano.configure("other", otherProps);
	}
	
	@Test
	public void authenticate_withCredentials_itReturnsTrueWithRightCredentials() {
		assertTrue(Maestrano.authenticate(defaultProps.getProperty("api.id"),defaultProps.getProperty("api.key")));
	}
	
	@Test
	public void authenticate_withCredentials_itReturnsFalseWithWrongCredentials() {
		assertFalse(Maestrano.authenticate(defaultProps.getProperty("api.id"),defaultProps.getProperty("api.key") + "aa"));
		assertFalse(Maestrano.authenticate(defaultProps.getProperty("api.id") + "aa",defaultProps.getProperty("api.key")));
	}
	
	@Test
	public void authenticate_withRequest_itReturnsTrueWithRightBasicAuth() throws UnsupportedEncodingException {
		HttpRequestStub request = new HttpRequestStub();
		String authStr = defaultProps.getProperty("api.id") + ":" + defaultProps.getProperty("api.key");
		authStr = "Basic " + DatatypeConverter.printBase64Binary(authStr.getBytes());
		request.setHeader("Authorization", authStr);
		
		assertTrue(Maestrano.authenticate(request));
	}
	
	@Test
	public void authenticate_withRequest_itReturnsFalseWithWrongBasicAuth() throws UnsupportedEncodingException {
		HttpRequestStub request = new HttpRequestStub();
		String authStr = defaultProps.getProperty("api.id") + ":" + defaultProps.getProperty("api.key") + "aaa";
		authStr = "Basic " + DatatypeConverter.printBase64Binary(authStr.getBytes());
		request.setHeader("Authorization", authStr);
		
		assertFalse(Maestrano.authenticate(request));
	}
	
	@Test
    public void authenticate_withRequestPreset_itReturnsTrueWithRightBasicAuth() throws UnsupportedEncodingException {
        HttpRequestStub request = new HttpRequestStub();
        String authStr = otherProps.getProperty("api.id") + ":" + otherProps.getProperty("api.key");
        authStr = "Basic " + DatatypeConverter.printBase64Binary(authStr.getBytes());
        request.setHeader("Authorization", authStr);
        
        assertTrue(Maestrano.authenticate("other", request));
    }
    
    @Test
    public void authenticate_withRequestPreset_itReturnsFalseWithWrongBasicAuth() throws UnsupportedEncodingException {
        HttpRequestStub request = new HttpRequestStub();
        String authStr = otherProps.getProperty("api.id") + ":" + otherProps.getProperty("api.key") + "aaa";
        authStr = "Basic " + DatatypeConverter.printBase64Binary(authStr.getBytes());
        request.setHeader("Authorization", authStr);
        
        assertFalse(Maestrano.authenticate("other", request));
    }
	
	@Test
	public void toMetadata_itReturnTheRightValue() {
		Map<String,Object> hash = new HashMap<String,Object>();
		hash.put("environment",Maestrano.appService().getEnvironment());
		hash.put("app",Maestrano.appService().toMetadataHash());
		hash.put("api",Maestrano.apiService().toMetadataHash());
		hash.put("sso",Maestrano.ssoService().toMetadataHash());
		hash.put("webhook",Maestrano.webhookService().toMetadataHash());
		
		Gson gson = new Gson();
		assertEquals(gson.toJson(hash),Maestrano.toMetadata());
	}
}
