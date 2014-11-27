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
		
		CnOrganization entity = CnOrganization.create(this.groupId, attrsMap);
		System.out.println(entity.getId());
		assertFalse(entity.getId() == null);
		assertEquals(this.groupId,entity.getGroupId());
		assertEquals("Doe Pty Ltd",entity.getName());
		assertEquals("Banking",entity.getIndustry());
	}
	
	@Test 
	public void all_itRetrievesAllOrganizations() throws Exception {
		List<CnOrganization> entities = CnOrganization.all(groupId);
		CnOrganization entity = entities.get(0);
		
		assertTrue(entity.getId() != null);
		assertEquals(this.groupId,entity.getGroupId());
	}
	
	@Test 
	public void retrieve_itRetrievesASingleOrganization() throws Exception {
		CnOrganization entity = CnOrganization.retrieve(groupId, "e50c42e0-583f-0132-c63c-1aa5759bfa45");
		
		assertTrue(entity.getId() != null);
		assertEquals(this.groupId,entity.getGroupId());
	}
	
	@Test 
	public void save_itUpdatesAnOrganization() throws Exception {
		CnOrganization entity = CnOrganization.retrieve(groupId, "e50c42e0-583f-0132-c63c-1aa5759bfa45");
		String newName = entity.getName() + "a";
		entity.setName(newName);
		
		assertTrue(entity.save());
		assertTrue(entity.getId() != null);
		assertEquals(newName,entity.getName());
		assertEquals(this.groupId,entity.getGroupId());
	}
}
