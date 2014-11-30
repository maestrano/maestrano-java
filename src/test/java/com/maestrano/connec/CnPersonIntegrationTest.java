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

public class CnPersonIntegrationTest {
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
	public void create_itCreatesAnEntity() throws Exception {
		Map<String, Object> attrsMap = new HashMap<String, Object>();
		attrsMap.put("firstName", "John");
		attrsMap.put("lastName", "Doe");
		
		CnPerson entity = CnPerson.create(this.groupId, attrsMap);
		System.out.println(entity.getId());
		assertFalse(entity.getId() == null);
		assertEquals(this.groupId,entity.getGroupId());
		assertEquals("John",entity.getFirstName());
		assertEquals("Doe",entity.getLastName());
	}
	
	@Test 
	public void all_itRetrievesAllEntities() throws Exception {
		List<CnPerson> entities = CnPerson.all(groupId);
		CnPerson entity = entities.get(0);
		
		assertTrue(entity.getId() != null);
		assertEquals(this.groupId,entity.getGroupId());
	}
	
	@Test 
	public void retrieve_itRetrievesASingleEntity() throws Exception {
		CnPerson entity = CnPerson.retrieve(groupId, "8a754b80-5a6c-0132-90fe-6a46f43bd3fe");
		
		assertTrue(entity.getId() != null);
		assertEquals(this.groupId,entity.getGroupId());
	}
	
	@Test 
	public void save_itUpdatesAnEntity() throws Exception {
		CnPerson entity = CnPerson.retrieve(groupId, "8a754b80-5a6c-0132-90fe-6a46f43bd3fe");
		String newName = entity.getFirstName() + "a";
		entity.setFirstName(newName);
		
		assertTrue(entity.save());
		assertTrue(entity.getId() != null);
		assertEquals(newName,entity.getFirstName());
		assertEquals(this.groupId,entity.getGroupId());
	}
}
