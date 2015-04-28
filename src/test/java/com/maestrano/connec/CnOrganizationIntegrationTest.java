package com.maestrano.connec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.maestrano.Maestrano;
import com.maestrano.net.ConnecClient;

public class CnOrganizationIntegrationTest {
	private Properties props = new Properties();
	private String groupId;
	
	@Before
	public void beforeEach() {
		props.setProperty("app.environment", "test");
		props.setProperty("api.id", "app-1");
		props.setProperty("api.key", "gfcmbu8269wyi0hjazk4t7o1sndpvrqxl53e1");
		Maestrano.configure(props);
		
		this.groupId = "cld-3";
	}
	
	@Test
    public void create_itCreatesAnOrganization() throws Exception {
        Map<String, Object> attrsMap = new HashMap<String, Object>();
        attrsMap.put("name", "Doe Pty Ltd");
        attrsMap.put("industry", "Banking");
        
        CnOrganization entity = CnOrganization.create(this.groupId, attrsMap, CnOrganization.class);
        assertFalse(entity.getId() == null);
        assertEquals(this.groupId,entity.getGroupId());
        assertEquals("Doe Pty Ltd",entity.getName());
        assertEquals("Banking",entity.getIndustry());
    }
	
	@Test 
	public void all_itRetrievesAllOrganizations() throws Exception {
		List<CnOrganization> entities = CnOrganization.all(groupId, CnOrganization.class);
		CnOrganization entity = entities.get(0);
		
		assertTrue(entity.getId() != null);
		assertEquals(this.groupId,entity.getGroupId());
	}
	
	@Test 
	public void retrieve_itRetrievesASingleOrganization() throws Exception {
		CnOrganization entity = CnOrganization.retrieve(groupId, "8aabb850-8394-0132-a4d1-2623376cdffe", CnOrganization.class);
		
		assertTrue(entity.getId() != null);
		assertEquals(this.groupId,entity.getGroupId());
	}
	
	@Test 
	public void save_itUpdatesAnOrganization() throws Exception {
		CnOrganization entity = CnOrganization.retrieve(groupId, "8aabb850-8394-0132-a4d1-2623376cdffe", CnOrganization.class);
		String newName = entity.getName() + "a";
		entity.setName(newName);
		
		assertTrue(entity.save());
		assertTrue(entity.getId() != null);
		assertEquals(newName,entity.getName());
		assertEquals(this.groupId,entity.getGroupId());
	}
	
	@Test
    public void crud_UsingMaps() throws Exception {
	    String groupId = "cld-3";

	    // Fetch all organizations
	    Map<String, Object> organizations = ConnecClient.all("organizations", groupId);
//	    System.out.println("Fetched organizations: " + organizations);
	    // Fetched organizations: {organizations=[{name=Doe Corp Inc., id=8afd71e0-8394-0132-a4d2-2623376cdffe, group_id=cld-3, type=organizations}, ... }
	    
	    // Retrieve first organization
	    List<Map<String, Object>> organizationsHashes = (List<Map<String, Object>>) organizations.get("organizations");
	    String firstOrganizationId = (String) organizationsHashes.get(0).get("id");
	    Map<String, Object> organization = (Map<String, Object>) ConnecClient.retrieve("organizations", groupId, firstOrganizationId).get("organizations");
//	    System.out.println("Retrieved first organization: " + organization);
	    // Retrieved first organization: {name=Doe Corp Inc., id=8afd71e0-8394-0132-a4d2-2623376cdffe, group_id=cld-3, type=organizations}
	    
	    // Create a new organization
	    Map<String, Object> newOrganization = new HashMap<String, Object>();
	    newOrganization.put("name", "New Organization");
	    organization = (Map<String, Object>) ConnecClient.create("organizations", groupId, newOrganization).get("organizations");
//	    System.out.println("Created new organization: " + organization);
	    // Created new organization: {name=New Organization, id=347e0fa0-cfaf-0132-4f1a-42f46dd33bd3, group_id=cld-3, type=organizations}
	    
	    // Update an organization
	    organization.put("industry", "Hardware");
	    String organizationId = (String) organization.get("id");
	    Map<String, Object> updatedOrganization = (Map<String, Object>) ConnecClient.update("organizations", groupId, organizationId, organization).get("organizations");
//        System.out.println("Updated organization: " + updatedOrganization);
        // Updated organization: {name=New Organization, id=347e0fa0-cfaf-0132-4f1a-42f46dd33bd3, group_id=cld-3, industry=Hardware, type=organizations}
    }
}
