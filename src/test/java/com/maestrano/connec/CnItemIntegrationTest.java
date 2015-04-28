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

public class CnItemIntegrationTest {
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
		Map<String, Object> attrsMap;
		
		// Create Sale Price
		attrsMap = new HashMap<String, Object>();
		attrsMap.put("totalAmount", 22.0);
		attrsMap.put("net_amount", 20.0);
		attrsMap.put("tax_amount", 2.0);
		attrsMap.put("tax_rate", 10.0);
		attrsMap.put("currency", "USD");
		CnPrice saleTaxRate = CnPrice.fromMap(attrsMap);
		
		// Create Purchase Price
		attrsMap = new HashMap<String, Object>();
		attrsMap.put("totalAmount", 22.0);
		attrsMap.put("net_amount", 20.0);
		attrsMap.put("tax_amount", 2.0);
		attrsMap.put("tax_rate", 10.0);
		attrsMap.put("currency", "USD");
		CnPrice purchasePrice = CnPrice.fromMap(attrsMap);
		
		// Create TaxCode
		attrsMap = new HashMap<String, Object>();
		attrsMap.put("name", "Chair");
		attrsMap.put("description", "Coloured chair");
		attrsMap.put("code", "CH-CLD-1234");
		attrsMap.put("unit", "EACH");
		attrsMap.put("type", "PURCHASED"); //PURCHASED, MANUFACTURED, SERVICE
		attrsMap.put("salePrice", saleTaxRate);
		attrsMap.put("purchasePrice", purchasePrice);
		
		CnItem entity = CnItem.create(this.groupId, attrsMap, CnItem.class);
		System.out.println(entity.getId());
		assertFalse(entity.getId() == null);
		assertEquals(this.groupId,entity.getGroupId());
		assertEquals("Chair",entity.getName());
	}
	
	@Test 
	public void all_itRetrievesAllEntities() throws Exception {
		List<CnItem> entities = CnItem.all(groupId, CnItem.class);
		CnItem entity = entities.get(0);
		
		assertTrue(entity.getId() != null);
		assertEquals(this.groupId,entity.getGroupId());
	}
	
	@Test 
	public void retrieve_itRetrievesASingleEntity() throws Exception {
		CnItem entity = CnItem.retrieve(groupId, "bd89b1c0-5a81-0132-9112-6a46f43bd3fe", CnItem.class);
		
		assertTrue(entity.getId() != null);
		assertEquals(this.groupId,entity.getGroupId());
		assertTrue(entity.getSalePrice().getTotalAmount() > 0);
		assertTrue(entity.getPurchasePrice().getTotalAmount() > 0);
	}
	
	@Test 
	public void save_itUpdatesAnEntity() throws Exception {
		CnItem entity = CnItem.retrieve(groupId, "bd89b1c0-5a81-0132-9112-6a46f43bd3fe", CnItem.class);
		String newName = entity.getName() + "a";
		entity.setName(newName);
		
		assertTrue(entity.save());
		assertTrue(entity.getId() != null);
		assertEquals(this.groupId,entity.getGroupId());
		assertEquals(newName,entity.getName());
	}
}
