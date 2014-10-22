package com.maestrano.net;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.maestrano.Maestrano;
import com.maestrano.testhelpers.HttpSessionStub;
import com.maestrano.testhelpers.MnoHttpClientStub;

public class MnoApiAccountResourceTest {
	private Properties props = new Properties();
	private MnoHttpClientStub httpClient;
	
	public class SomeModel extends MnoApiAccountResource {}
	
	private SomeModel subject;
	
	@Before
	public void beforeEach() throws Exception {
		props.setProperty("app.environment", "production");
		props.setProperty("app.host", "https://mysuperapp.com");
		props.setProperty("api.id", "someid");
		props.setProperty("api.key", "somekey");
		props.setProperty("sso.sloEnabled", "true");
		Maestrano.configure(props);
		
		httpClient = new MnoHttpClientStub();
		subject = new SomeModel();
	}
	
	@Test
	public void class_getEntityName_itReturnsTheRightEntityName() {
		assertEquals("some_model",MnoApiAccountResource.getEntityName(SomeModel.class));
	}
	
	@Test
	public void class_getEntityName_itReturnsTheRightEntitiesName() {
		assertEquals("some_models",MnoApiAccountResource.getEntitiesName(SomeModel.class));
	}
	
	@Test
	public void class_getCollectionEndpoint_itReturnsTheRightEntityApiEndpoint() {
		assertEquals("/api/v1/account/some_models",MnoApiAccountResource.getCollectionEndpoint(SomeModel.class));
	}
	
	@Test
	public void getInstanceEndpoint_itReturnsTheRightEntityInstanceApiEndpoint() {
		assertEquals("/api/v1/account/some_models/1",MnoApiAccountResource.getInstanceEndpoint(SomeModel.class,"1"));
	}
	
	@Test
	public void class_getCollectionUrl_itReturnsTheRightEntityApiUrl() {
		assertEquals("https://maestrano.com/api/v1/account/some_models",MnoApiAccountResource.getCollectionUrl(SomeModel.class));
	}
	
	@Test
	public void getInstanceUrl_itReturnsTheRightEntityInstanceApiUrl() {
		assertEquals("https://maestrano.com/api/v1/account/some_models/1",MnoApiAccountResource.getInstanceUrl(SomeModel.class,"1"));
	}
}
