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
	public void class_getEntityName_itReturnsTheRightEntityName() {
		assertEquals("some_model",ConnecClient.getEntityName(CnSomeModel.class));
	}

	@Test
	public void class_getEntitiesName_itReturnsTheRightEntitiesName() {
		assertEquals("some_models",ConnecClient.getEntitiesName(CnSomeModel.class));
	}
	
	@Test
	public void class_getEntitiesName_itReturnTheRightPluralVersionForPerson() {
		assertEquals("people",ConnecClient.getEntitiesName(CnPerson.class));
	}

	@Test
	public void class_getCollectionEndpoint_itReturnsTheRightEntityApiEndpoint() {
		assertEquals("/api/v2/cld-1/some_models",ConnecClient.getCollectionEndpoint(CnSomeModel.class,"cld-1"));
	}

	@Test
	public void getInstanceEndpoint_itReturnsTheRightEntityInstanceApiEndpoint() {
		assertEquals("/api/v2/cld-1/some_models/1",ConnecClient.getInstanceEndpoint(CnSomeModel.class,"cld-1","1"));
	}

	@Test
	public void class_getCollectionUrl_itReturnsTheRightEntityApiUrl() {
		assertEquals("https://connec.maestrano.com/api/v2/cld-1/some_models",ConnecClient.getCollectionUrl(CnSomeModel.class,"cld-1"));
	}

	@Test
	public void getInstanceUrl_itReturnsTheRightEntityInstanceApiUrl() {
		assertEquals("https://connec.maestrano.com/api/v2/cld-1/some_models/1",ConnecClient.getInstanceUrl(CnSomeModel.class,"cld-1","1"));
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
		httpClient.setResponseStub(gson.toJson(hash), ConnecClient.getCollectionUrl(CnPerson.class,this.groupId));

		// Test
		List<CnPerson> respList = ConnecClient.all(CnPerson.class, this.groupId, null, httpClient);
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
		httpClient.setResponseStub(gson.toJson(hash), ConnecClient.getInstanceUrl(CnPerson.class,this.groupId,"bill-1234"));

		// Test
		CnPerson resp = ConnecClient.retrieve(CnPerson.class, this.groupId, "bill-1234", httpClient);
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
		createHash.put("first_name", "John");
		createHash.put("last_name", "Doe");
		
		// Prepare response
		Gson gson = new Gson();
		httpClient = new MnoHttpClientStub();
		httpClient.setResponseStub(gson.toJson(hash), 
				ConnecClient.getCollectionUrl(CnPerson.class, this.groupId),null,gson.toJson(createHash));
		
		// Test
		CnPerson resp = ConnecClient.create(CnPerson.class, this.groupId, createHash, httpClient);
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
		updHash.put("last_name", "Robert");
		
		// Prepare response
		Gson gson = new Gson();
		httpClient = new MnoHttpClientStub();
		httpClient.setResponseStub(gson.toJson(hash), 
				ConnecClient.getInstanceUrl(CnPerson.class, this.groupId,"123456"),null,gson.toJson(updHash));
		
		// Test
		CnPerson resp = ConnecClient.update(CnPerson.class, this.groupId, "123456", updHash, httpClient);
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
				ConnecClient.getInstanceUrl(CnPerson.class, this.groupId,"123456"));
		
		// Test
		CnPerson resp = ConnecClient.delete(CnPerson.class, this.groupId, "123456", httpClient);
		assertEquals("123456",resp.getId());
		assertEquals("John",resp.getFirstName());
		assertEquals("Doe",resp.getLastName());
		assertEquals(this.groupId,resp.getGroupId());
	}
}
