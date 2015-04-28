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
import com.maestrano.connec.CnPerson;
import com.maestrano.helpers.MnoMapHelper;
import com.maestrano.testhelpers.MnoHttpClientStub;

public class ConnecClientTest {
	private Properties props = new Properties();
	private String groupId;
	private MnoHttpClientStub httpClient;

	public class CnSomeModel {}
	
	public Map<String,Object> preparePersonObj() {
		Map<String,Object> obj = new HashMap<String,Object>();
		obj.put("id","123456");
		obj.put("group_id",this.groupId);
		obj.put("first_name","John");
		obj.put("last_name","Doe");
		
		return obj;
	}
	
	@Before
	public void beforeEach() throws Exception {
		props.setProperty("app.environment", "production");
		props.setProperty("app.host", "https://mysuperapp.com");
		props.setProperty("api.id", "someid");
		props.setProperty("api.key", "somekey");
		props.setProperty("sso.sloEnabled", "true");
		Maestrano.configure(props);
		
		groupId = "654321";
		httpClient = new MnoHttpClientStub();
	}
	
	@Test
	public void class_getCollectionEndpoint_itReturnsTheRightEntityApiEndpoint() {
		assertEquals("/api/v2/cld-1/some_models",ConnecClient.getCollectionEndpoint("some_models","cld-1"));
	}

	@Test
	public void getInstanceEndpoint_itReturnsTheRightEntityInstanceApiEndpoint() {
		assertEquals("/api/v2/cld-1/some_models/1",ConnecClient.getInstanceEndpoint("some_models","cld-1","1"));
	}

	@Test
	public void class_getCollectionUrl_itReturnsTheRightEntityApiUrl() {
		assertEquals("https://connec.maestrano.com/api/v2/cld-1/some_models",ConnecClient.getCollectionUrl("some_models","cld-1"));
	}

	@Test
	public void getInstanceUrl_itReturnsTheRightEntityInstanceApiUrl() {
		assertEquals("https://connec.maestrano.com/api/v2/cld-1/some_models/1",ConnecClient.getInstanceUrl("some_models","cld-1","1"));
	}
	
	@Test
	public void all_itReturnsAllTheSpecifiedEntities() throws Exception {
		// Prepare data
		Map<String,Object> hash = new HashMap<String,Object>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.add(this.preparePersonObj());
		hash.put("entities", list);

		// Prepare response
		Gson gson = new Gson();
		httpClient = new MnoHttpClientStub();
		httpClient.setResponseStub(gson.toJson(hash), ConnecClient.getCollectionUrl("people",this.groupId));

		// Test
		List<CnPerson> respList = ConnecClient.all("people", this.groupId, null, httpClient, CnPerson.class);
		assertEquals("123456",respList.get(0).getId());
		assertEquals("John",respList.get(0).getFirstName());
		assertEquals("Doe",respList.get(0).getLastName());
		assertEquals(this.groupId,respList.get(0).getGroupId());
	}

	@Test
	public void retrieve_itReturnsTheRightEntity() throws Exception {
		// Prepare data
		Map<String,Object> hash = new HashMap<String,Object>();
		hash.put("entity", this.preparePersonObj());

		// Prepare response
		Gson gson = new Gson();
		httpClient = new MnoHttpClientStub();
		httpClient.setResponseStub(gson.toJson(hash), ConnecClient.getInstanceUrl("people",this.groupId,"bill-1234"));

		// Test
		CnPerson resp = ConnecClient.retrieve("people", this.groupId, "bill-1234", httpClient, CnPerson.class);
		assertEquals("123456",resp.getId());
		assertEquals("John",resp.getFirstName());
		assertEquals("Doe",resp.getLastName());
		assertEquals(this.groupId,resp.getGroupId());

	}
	
	@Test
	public void create_itCreatesTheRightEntity() throws Exception {
		// Prepare Response data
		Map<String,Object> hash = new HashMap<String,Object>();
		hash.put("entity", this.preparePersonObj());
		
		// Prepare Creation data
		Map<String,Object> createHash = new HashMap<String,Object>();
		createHash.put("firstName", "John");
		createHash.put("lastName", "Doe");
		
		// Prepare response
		Gson gson = new Gson();
		Map<String,Map<String,Object>> reqEnvelope = new HashMap<String,Map<String,Object>>();
		reqEnvelope.put("entity", MnoMapHelper.toUnderscoreHash(createHash));
		
		httpClient = new MnoHttpClientStub();
		httpClient.setResponseStub(gson.toJson(hash), 
				ConnecClient.getCollectionUrl("people", this.groupId),null,gson.toJson(reqEnvelope));
		
		// Test
		CnPerson resp = ConnecClient.create("people", this.groupId, createHash, httpClient, CnPerson.class);
		assertEquals("123456",resp.getId());
		assertEquals("John",resp.getFirstName());
		assertEquals("Doe",resp.getLastName());
		assertEquals(this.groupId,resp.getGroupId());
	}
	
	@Test
	public void update_itUpdatesTheRightEntity() throws Exception {
		// Prepare Response data
		Map<String,Object> hash = new HashMap<String,Object>();
		hash.put("entity", this.preparePersonObj());
		
		// Prepare update data
		Map<String,Object> updHash = new HashMap<String,Object>();
		updHash.put("lastName", "Robert");
		
		// Prepare response
		Gson gson = new Gson();
		Map<String,Map<String,Object>> reqEnvelope = new HashMap<String,Map<String,Object>>();
		reqEnvelope.put("entity", MnoMapHelper.toUnderscoreHash(updHash));
		httpClient = new MnoHttpClientStub();
		httpClient.setResponseStub(gson.toJson(hash), 
				ConnecClient.getInstanceUrl("people", this.groupId,"123456"),null,gson.toJson(reqEnvelope));
		
		// Test
		CnPerson resp = ConnecClient.update("people", this.groupId, "123456", updHash, httpClient, CnPerson.class);
		assertEquals("123456",resp.getId());
		assertEquals("John",resp.getFirstName());
		assertEquals("Doe",resp.getLastName());
		assertEquals(this.groupId,resp.getGroupId());
	}
	
	@Test
	public void delete_itDeletesTheRightEntity() throws Exception {
		// Prepare Response data
		Map<String,Object> hash = new HashMap<String,Object>();
		hash.put("entity", this.preparePersonObj());
		
		// Prepare response
		Gson gson = new Gson();
		httpClient = new MnoHttpClientStub();
		httpClient.setResponseStub(gson.toJson(hash), 
				ConnecClient.getInstanceUrl("people", this.groupId,"123456"));
		
		// Test
		CnPerson resp = ConnecClient.delete("people", this.groupId, "123456", httpClient, CnPerson.class);
		assertEquals("123456",resp.getId());
		assertEquals("John",resp.getFirstName());
		assertEquals("Doe",resp.getLastName());
		assertEquals(this.groupId,resp.getGroupId());
	}
}
