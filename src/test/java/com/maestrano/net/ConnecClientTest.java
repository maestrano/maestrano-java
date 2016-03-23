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
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.helpers.MnoMapHelper;
import com.maestrano.testhelpers.MnoHttpClientStub;

public class ConnecClientTest {
    private Properties defaultProps = new Properties();
    private Properties otherProps = new Properties();
    
	private String groupId = "654321";
	private MnoHttpClientStub httpClient = new MnoHttpClientStub();
	private ConnecClient connecClient;

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
	public void beforeEach()  {
	    defaultProps.setProperty("environment", "production");
        defaultProps.setProperty("app.host", "https://mysuperapp.com");
        defaultProps.setProperty("api.id", "someid");
        defaultProps.setProperty("api.key", "somekey");
        defaultProps.setProperty("connec.host", "https://api-connec.maestrano.com");
        Maestrano.reloadConfiguration(defaultProps);
        
        otherProps.setProperty("environment", "production");
        otherProps.setProperty("app.host", "https://myotherapp.com");
        otherProps.setProperty("api.id", "otherid");
        otherProps.setProperty("api.key", "otherkey");
        otherProps.setProperty("connec.host", "https://api-connec.other.com");
        Maestrano.reloadConfiguration("other", otherProps);
        
        this.connecClient = ConnecClient.defaultClient();
	}
	
	@Test
	public void class_getCollectionEndpoint_itReturnsTheRightEntityApiEndpoint() {
		assertEquals("/api/v2/cld-1/some_models", connecClient.getCollectionEndpoint("some_models", "cld-1"));
	}

	@Test
	public void getInstanceEndpoint_itReturnsTheRightEntityInstanceApiEndpoint() {
		assertEquals("/api/v2/cld-1/some_models/1", connecClient.getInstanceEndpoint("some_models", "cld-1","1"));
	}

	@Test
	public void class_getCollectionUrl_itReturnsTheRightEntityApiUrl() {
		assertEquals("https://api-connec.maestrano.com/api/v2/cld-1/some_models", connecClient.getCollectionUrl("some_models", "cld-1"));
	}

	@Test
	public void getInstanceUrl_itReturnsTheRightEntityInstanceApiUrl() throws MnoConfigurationException {
		assertEquals("https://api-connec.maestrano.com/api/v2/cld-1/some_models/1", ConnecClient.withPreset("default").getInstanceUrl("some_models", "cld-1","1"));
	}
	
	@Test
    public void class_getCollectionUrl_itReturnsTheRightEntityApiUrlWithPreset() throws MnoConfigurationException {
        assertEquals("https://api-connec.other.com/api/v2/cld-1/some_models", ConnecClient.withPreset("other").getCollectionUrl("some_models", "cld-1"));
    }
	
	@Test
	public void all_itReturnsAllTheSpecifiedEntities() throws Exception {
		// Prepare data
		Map<String,Object> hash = new HashMap<String,Object>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.add(this.preparePersonObj());
		hash.put("people", list);

		// Prepare response
		Gson gson = new Gson();
		httpClient = new MnoHttpClientStub();
		httpClient.setResponseStub(gson.toJson(hash), connecClient.getCollectionUrl("people",this.groupId));

		// Test
		Map<String, Object> people = connecClient.all("people", groupId, null, httpClient);
        @SuppressWarnings("unchecked")
		List<Map<String, Object>> peopleHashes = (List<Map<String, Object>>) people.get("people");
		assertEquals("123456", peopleHashes.get(0).get("id"));
		assertEquals("John", peopleHashes.get(0).get("first_name"));
		assertEquals("Doe", peopleHashes.get(0).get("last_name"));
		assertEquals(this.groupId, peopleHashes.get(0).get("group_id"));
	}

	@Test
	public void retrieve_itReturnsTheRightEntity() throws Exception {
		// Prepare data
		Map<String,Object> hash = new HashMap<String,Object>();
		hash.put("people", this.preparePersonObj());

		// Prepare response
		Gson gson = new Gson();
		httpClient = new MnoHttpClientStub();
		httpClient.setResponseStub(gson.toJson(hash), connecClient.getInstanceUrl("people", this.groupId, "usr-1234"));

		// Test
		@SuppressWarnings("unchecked")
		Map<String, Object> personHash = (Map<String, Object>) connecClient.retrieve("people", this.groupId, "usr-1234", httpClient).get("people");
		assertEquals("123456", personHash.get("id"));
		assertEquals("John", personHash.get("first_name"));
		assertEquals("Doe", personHash.get("last_name"));
		assertEquals(this.groupId, personHash.get("group_id"));
	}
	
	@Test
	public void create_itCreatesTheRightEntity() throws Exception {
		// Prepare Response data
		Map<String,Object> hash = new HashMap<String,Object>();
		hash.put("people", this.preparePersonObj());
		
		// Prepare Creation data
		Map<String,Object> createHash = new HashMap<String,Object>();
		createHash.put("first_name", "John");
		createHash.put("last_name", "Doe");
		
		// Prepare response
		Gson gson = new Gson();
		Map<String,Object> reqEnvelope = new HashMap<String,Object>();
		reqEnvelope.put("people", MnoMapHelper.toUnderscoreHash(createHash));
		reqEnvelope.put("resource", "people");
		
		httpClient = new MnoHttpClientStub();
		httpClient.setResponseStub(gson.toJson(hash), connecClient.getCollectionUrl("people", this.groupId), null, gson.toJson(reqEnvelope));
		
		// Test
		@SuppressWarnings("unchecked")
		Map<String, Object> personHash = (Map<String, Object>) connecClient.create("people", this.groupId, createHash, httpClient).get("people");
        assertEquals("123456", personHash.get("id"));
        assertEquals("John", personHash.get("first_name"));
        assertEquals("Doe", personHash.get("last_name"));
        assertEquals(this.groupId, personHash.get("group_id"));
	}
	
	@Test
	public void update_itUpdatesTheRightEntity() throws Exception {
		// Prepare Response data
		Map<String,Object> hash = new HashMap<String,Object>();
		hash.put("people", this.preparePersonObj());
		hash.put("resource", "people");
		
		// Prepare update data
		Map<String,Object> updHash = new HashMap<String,Object>();
		updHash.put("lastName", "Robert");
		
		// Prepare response
		Gson gson = new Gson();
		Map<String,Object> reqEnvelope = new HashMap<String,Object>();
		reqEnvelope.put("people", MnoMapHelper.toUnderscoreHash(updHash));
		reqEnvelope.put("resource", "people");
		httpClient = new MnoHttpClientStub();
		httpClient.setResponseStub(gson.toJson(hash), 
				connecClient.getInstanceUrl("people", this.groupId, "123456"),null,gson.toJson(reqEnvelope));

		// Test
		@SuppressWarnings("unchecked")
		Map<String, Object> personHash = (Map<String, Object>) connecClient.update("people", this.groupId, "123456", updHash, httpClient).get("people");
        assertEquals("123456", personHash.get("id"));
        assertEquals("John", personHash.get("first_name"));
        assertEquals("Doe", personHash.get("last_name"));
        assertEquals(this.groupId, personHash.get("group_id"));
	}
	
	@Test
	public void delete_itDeletesTheRightEntity() throws Exception {
		// Prepare Response data
		Map<String,Object> hash = new HashMap<String,Object>();
		hash.put("people", this.preparePersonObj());
		hash.put("resource", "people");
		
		// Prepare response
		Gson gson = new Gson();
		httpClient = new MnoHttpClientStub();
		httpClient.setResponseStub(gson.toJson(hash), 
				connecClient.getInstanceUrl("people", this.groupId,"123456"));
		
		// Test
		@SuppressWarnings("unchecked")
		Map<String, Object> personHash = (Map<String, Object>) connecClient.delete("people", this.groupId, "123456", httpClient).get("people");
        assertEquals("123456", personHash.get("id"));
        assertEquals("John", personHash.get("first_name"));
        assertEquals("Doe", personHash.get("last_name"));
        assertEquals(this.groupId, personHash.get("group_id"));
	}
}
