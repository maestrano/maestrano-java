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

public class CnTaxCodeIntegrationTest {
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
		attrsMap.put("name", "GST (Sales)");
		attrsMap.put("rate", 6.0);
		
		CnTaxCode entity = CnTaxCode.create(this.groupId, attrsMap);
		System.out.println(entity.getId());
		assertFalse(entity.getId() == null);
		assertEquals(this.groupId,entity.getGroupId());
		assertEquals("GST (Sales)",entity.getName());
		assertTrue(6.0 == entity.getRate());
	}
	
	@Test 
	public void all_itRetrievesAllEntities() throws Exception {
		List<CnTaxCode> entities = CnTaxCode.all(groupId);
		CnTaxCode entity = entities.get(0);
		
		assertTrue(entity.getId() != null);
		assertEquals(this.groupId,entity.getGroupId());
	}
	
	@Test 
	public void retrieve_itRetrievesASingleEntity() throws Exception {
		CnTaxCode entity = CnTaxCode.retrieve(groupId, "3a40d150-5a73-0132-9102-6a46f43bd3fe");
		
		assertTrue(entity.getId() != null);
		assertEquals(this.groupId,entity.getGroupId());
	}
	
	@Test 
	public void save_itUpdatesAnEntity() throws Exception {
		CnTaxCode entity = CnTaxCode.retrieve(groupId, "3a40d150-5a73-0132-9102-6a46f43bd3fe");
		double newRate = entity.getRate() + 1;
		entity.setRate(newRate);
		
		assertTrue(entity.save());
		assertTrue(entity.getId() != null);
		assertTrue(newRate == entity.getRate());
		assertEquals(this.groupId,entity.getGroupId());
	}
}
