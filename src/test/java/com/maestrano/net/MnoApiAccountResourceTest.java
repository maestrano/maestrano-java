package com.maestrano.net;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maestrano.Maestrano;
import com.maestrano.account.MnoBill;
import com.maestrano.testhelpers.HttpSessionStub;
import com.maestrano.testhelpers.MnoHttpClientStub;

public class MnoApiAccountResourceTest {
	private Properties props = new Properties();
	private MnoHttpClientStub httpClient;

	public class MnoSomeModel {}

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
		assertEquals("some_model",MnoApiAccountResource.getEntityName(MnoSomeModel.class));
	}

	@Test
	public void class_getEntityName_itReturnsTheRightEntitiesName() {
		assertEquals("some_models",MnoApiAccountResource.getEntitiesName(MnoSomeModel.class));
	}

	@Test
	public void class_getCollectionEndpoint_itReturnsTheRightEntityApiEndpoint() {
		assertEquals("/api/v1/account/some_models",MnoApiAccountResource.getCollectionEndpoint(MnoSomeModel.class));
	}

	@Test
	public void getInstanceEndpoint_itReturnsTheRightEntityInstanceApiEndpoint() {
		assertEquals("/api/v1/account/some_models/1",MnoApiAccountResource.getInstanceEndpoint(MnoSomeModel.class,"1"));
	}

	@Test
	public void class_getCollectionUrl_itReturnsTheRightEntityApiUrl() {
		assertEquals("https://maestrano.com/api/v1/account/some_models",MnoApiAccountResource.getCollectionUrl(MnoSomeModel.class));
	}

	@Test
	public void getInstanceUrl_itReturnsTheRightEntityInstanceApiUrl() {
		assertEquals("https://maestrano.com/api/v1/account/some_models/1",MnoApiAccountResource.getInstanceUrl(MnoSomeModel.class,"1"));
	}

	@Test
	public void all_itReturnsAllTheSpecifiedEntities() throws IOException {
		// Prepare data
		Map<String,Object> hash = new HashMap<String,Object>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> obj = new HashMap<String,Object>();
		obj.put("id","bill-1234");
		list.add(obj);
		hash.put("data", list);

		// Prepare response
		Gson gson = new Gson();
		httpClient = new MnoHttpClientStub();
		httpClient.setResponseStub(gson.toJson(hash), MnoApiAccountResource.getCollectionUrl(MnoBill.class));

		// Test
		List<MnoBill> respList = MnoApiAccountResource.all(MnoBill.class, null, httpClient);
		assertEquals("bill-1234",respList.get(0).getId());
	}

	@Test
	public void all_withQueryParameters_itReturnsTheSpecifiedEntities() throws IOException {
		// Prepare data
		Map<String,Object> hash = new HashMap<String,Object>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> obj = new HashMap<String,Object>();
		obj.put("id","bill-1234");
		list.add(obj);
		hash.put("data", list);
		
		// Params
		Map<String,String> params = new HashMap<String,String>();
		params.put("group_id", "cld-4567");
		
		// Prepare response
		Gson gson = new Gson();
		httpClient = new MnoHttpClientStub();
		httpClient.setResponseStub(gson.toJson(hash), MnoApiAccountResource.getCollectionUrl(MnoBill.class),params);

		// Test
		List<MnoBill> respList = MnoApiAccountResource.all(MnoBill.class, params, httpClient);
		assertEquals("bill-1234",respList.get(0).getId());
	}
}
