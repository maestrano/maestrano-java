package com.maestrano.net;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.maestrano.Maestrano;
import com.maestrano.testhelpers.MnoHttpClientStub;

public class MnoApiConnecClientTest {
	private Properties props = new Properties();
	private MnoHttpClientStub httpClient;

	public class CncSomeModel {}
	public class CncPerson {}

	@Before
	public void beforeEach() throws Exception {
		props.setProperty("app.environment", "production");
		props.setProperty("app.host", "https://mysuperapp.com");
		props.setProperty("api.id", "someid");
		props.setProperty("api.key", "somekey");
		props.setProperty("sso.sloEnabled", "true");
		Maestrano.configure(props);

		httpClient = new MnoHttpClientStub();
	}

	@Test
	public void class_getEntityName_itReturnsTheRightEntityName() {
		assertEquals("some_model",MnoApiConnecClient.getEntityName(CncSomeModel.class));
	}

	@Test
	public void class_getEntitiesName_itReturnsTheRightEntitiesName() {
		assertEquals("some_models",MnoApiConnecClient.getEntitiesName(CncSomeModel.class));
	}
	
	@Test
	public void class_getEntitiesName_itReturnTheRightPluralVersionForPerson() {
		assertEquals("people",MnoApiConnecClient.getEntitiesName(CncPerson.class));
	}

	@Test
	public void class_getCollectionEndpoint_itReturnsTheRightEntityApiEndpoint() {
		assertEquals("/api/v2/some_models",MnoApiConnecClient.getCollectionEndpoint(CncSomeModel.class));
	}

	@Test
	public void getInstanceEndpoint_itReturnsTheRightEntityInstanceApiEndpoint() {
		assertEquals("/api/v2/some_models/1",MnoApiConnecClient.getInstanceEndpoint(CncSomeModel.class,"1"));
	}

	@Test
	public void class_getCollectionUrl_itReturnsTheRightEntityApiUrl() {
		assertEquals("https://connec.maestrano.com/api/v2/some_models",MnoApiConnecClient.getCollectionUrl(CncSomeModel.class));
	}

	@Test
	public void getInstanceUrl_itReturnsTheRightEntityInstanceApiUrl() {
		assertEquals("https://connec.maestrano.com/api/v2/some_models/1",MnoApiConnecClient.getInstanceUrl(CncSomeModel.class,"1"));
	}
}
