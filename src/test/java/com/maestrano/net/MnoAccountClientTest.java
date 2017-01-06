package com.maestrano.net;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.maestrano.Maestrano;
import com.maestrano.account.MnoBill;
import com.maestrano.account.MnoBill.MnoBillClient;
import com.maestrano.testhelpers.MnoHttpClientStub;

public class MnoAccountClientTest {
	private Properties props = new Properties();
	private MnoHttpClientStub httpClient;
	private MnoAccountClient<MnoSomeModel> mnoAccountClient;
	private MnoBillClient mnoBillClient;
	public class MnoSomeModel {}

	@Before
	public void beforeEach() throws Exception {
		props.setProperty("environment", "production");
		props.setProperty("app.host", "https://mysuperapp.com");
		props.setProperty("api.id", "someid");
		props.setProperty("api.key", "somekey");
		props.setProperty("sso.sloEnabled", "true");
		Maestrano.reloadConfiguration(props);

		httpClient = new MnoHttpClientStub();
		mnoAccountClient = new MnoAccountClient<MnoSomeModel>(MnoSomeModel.class);
		mnoBillClient = new MnoBillClient();
	}

	@Test
	public void class_getEntityName_itReturnsTheRightEntityName() {
		
		assertEquals("some_model",mnoAccountClient.getEntityName());
	}

	@Test
	public void class_getEntitiesName_itReturnsTheRightEntitiesName() {
		assertEquals("some_models",mnoAccountClient.getEntitiesName());
	}

	@Test
	public void class_getCollectionEndpoint_itReturnsTheRightEntityApiEndpoint() {
		assertEquals("/api/v1/account/some_models",mnoAccountClient.getCollectionEndpoint());
	}

	@Test
	public void getInstanceEndpoint_itReturnsTheRightEntityInstanceApiEndpoint() {
		assertEquals("/api/v1/account/some_models/1",mnoAccountClient.getInstanceEndpoint("1"));
	}

	@Test
	public void class_getCollectionUrl_itReturnsTheRightEntityApiUrl() {
		assertEquals("https://api-hub.maestrano.com/api/v1/account/some_models",mnoAccountClient.getCollectionUrl());
	}

	@Test
	public void getInstanceUrl_itReturnsTheRightEntityInstanceApiUrl() {
		assertEquals("https://api-hub.maestrano.com/api/v1/account/some_models/1",mnoAccountClient.getInstanceUrl("1"));
	}

	@Test
	public void all_itReturnsAllTheSpecifiedEntities() throws Exception {

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
		httpClient.setResponseStub(gson.toJson(hash), mnoBillClient.getCollectionUrl());

		// Test
		List<MnoBill> respList = mnoBillClient.all(null, httpClient);
		assertEquals("bill-1234",respList.get(0).getId());
	}

	@Test
	public void all_withQueryParameters_itReturnsTheSpecifiedEntities() throws Exception {
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
		httpClient.setResponseStub(gson.toJson(hash), mnoBillClient.getCollectionUrl(),params);

		// Test
		List<MnoBill> respList = mnoBillClient.all( params, httpClient);
		assertEquals("bill-1234",respList.get(0).getId());
	}

	@Test
	public void retrieve_itReturnsTheRightEntity() throws Exception {
		// Prepare data
		Map<String,Object> hash = new HashMap<String,Object>();
		Map<String,Object> obj = new HashMap<String,Object>();
		obj.put("id","bill-1234");
		hash.put("data", obj);

		// Prepare response
		Gson gson = new Gson();
		httpClient = new MnoHttpClientStub();
		httpClient.setResponseStub(gson.toJson(hash), mnoBillClient.getInstanceUrl("bill-1234"));

		// Test
		MnoBill resp = mnoBillClient.retrieve( "bill-1234", httpClient);
		assertEquals("bill-1234",resp.getId());

	}
	
	@Test
	public void create_itCreatesTheRightEntity() throws Exception {
		// Prepare Response data
		Map<String,Object> hash = new HashMap<String,Object>();
		Map<String,Object> obj = new HashMap<String,Object>();
		obj.put("id","bill-1234");
		hash.put("data", obj);
		
		// Prepare Creation data
		Map<String,Object> updHash = new HashMap<String,Object>();
		updHash.put("description", "somedescription");
		
		// Prepare response
		Gson gson = new Gson();
		httpClient = new MnoHttpClientStub();
		httpClient.setResponseStub(gson.toJson(hash), 
				mnoBillClient.getCollectionUrl(),null,gson.toJson(updHash));
		
		// Test
		MnoBill resp = mnoBillClient.create( updHash, httpClient);
		assertEquals("bill-1234",resp.getId());
	}
	
	@Test
	public void update_itUpdatesTheRightEntity() throws Exception {
		// Prepare Response data
		Map<String,Object> hash = new HashMap<String,Object>();
		Map<String,Object> obj = new HashMap<String,Object>();
		obj.put("id","bill-1234");
		hash.put("data", obj);
		
		// Prepare update data
		Map<String,Object> updHash = new HashMap<String,Object>();
		updHash.put("description", "somedescription");
		
		// Prepare response
		Gson gson = new Gson();
		httpClient = new MnoHttpClientStub();
		httpClient.setResponseStub(gson.toJson(hash), mnoBillClient.getInstanceUrl("bill-1234"),null,gson.toJson(updHash));
		
		// Test
		MnoBill resp = mnoBillClient.update( "bill-1234", updHash, httpClient);
		assertEquals("bill-1234",resp.getId());
	}
	
	@Test
	public void delete_itDeletesTheRightEntity() throws Exception {
		// Prepare Response data
		Map<String,Object> hash = new HashMap<String,Object>();
		Map<String,Object> obj = new HashMap<String,Object>();
		obj.put("id","bill-1234");
		hash.put("data", obj);
		
		// Prepare response
		Gson gson = new Gson();
		httpClient = new MnoHttpClientStub();
		httpClient.setResponseStub(gson.toJson(hash), mnoBillClient.getInstanceUrl("bill-1234"));
		
		// Test
		MnoBill resp = mnoBillClient.delete( "bill-1234", httpClient);
		assertEquals("bill-1234",resp.getId());
	}
}
