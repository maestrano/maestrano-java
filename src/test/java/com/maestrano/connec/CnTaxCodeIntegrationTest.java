package com.maestrano.connec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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
	
	@SuppressWarnings("unchecked")
	@Test
	public void create_itCreatesAnEntity() throws Exception {
		Map<String, Object> attrsMap;
		
		// Create Sales Tax Rate
		attrsMap = new HashMap<String, Object>();
		attrsMap.put("name", "GST (Sales)");
		attrsMap.put("rate", 6.0);
		attrsMap.put("taxTypeApplicable", "TaxOnAmount");
		CnTaxRate saleTaxRate = CnTaxRate.create(this.groupId, attrsMap, CnTaxRate.class);
		
		// Create Purchase Tax Rate
		attrsMap = new HashMap<String, Object>();
		attrsMap.put("name", "GST (Purchase)");
		attrsMap.put("rate", 2.0);
		attrsMap.put("taxTypeApplicable", "TaxOnAmount");
		CnTaxRate purchaseTaxRate = CnTaxRate.create(this.groupId, attrsMap, CnTaxRate.class);
		
		// Create TaxCode
		attrsMap = new HashMap<String, Object>();
		attrsMap.put("name", "State Tax on goods");
		attrsMap.put("saleTaxRate", 6.0); // not needed - calculated by Connec!
		attrsMap.put("purchaseTaxRate", 2.0); // not needed - calculated by Connec!
		attrsMap.put("saleTaxes", new ArrayList<CnTaxRate>());
		((ArrayList<CnTaxRate>) attrsMap.get("saleTaxes")).add(saleTaxRate);
		attrsMap.put("purchaseTaxes", new ArrayList<CnTaxRate>());
		((ArrayList<CnTaxRate>) attrsMap.get("purchaseTaxes")).add(purchaseTaxRate);
		
		CnTaxCode entity = CnTaxCode.create(this.groupId, attrsMap, CnTaxCode.class);
		System.out.println(entity.getId());
		assertFalse(entity.getId() == null);
		assertEquals(this.groupId,entity.getGroupId());
		assertEquals("State Tax on goods",entity.getName());
		assertEquals("TaxOnAmount",entity.getSaleTaxes().get(0).getTaxTypeApplicable());
	}
	
	@Test 
	public void all_itRetrievesAllEntities() throws Exception {
		List<CnTaxCode> entities = CnTaxCode.all(groupId, CnTaxCode.class);
		CnTaxCode entity = entities.get(0);
		
		assertTrue(entity.getId() != null);
		assertEquals(this.groupId,entity.getGroupId());
	}
	
	@Test 
	public void retrieve_itRetrievesASingleEntity() throws Exception {
		CnTaxCode entity = CnTaxCode.retrieve(groupId, "8419d770-5a82-0132-9116-6a46f43bd3fe", CnTaxCode.class);
		
		assertTrue(entity.getId() != null);
		assertEquals(this.groupId,entity.getGroupId());
		assertTrue(entity.getSaleTaxes().get(0).getRate() > 0);
		assertTrue(entity.getPurchaseTaxes().get(0).getRate() > 0);
	}
	
	@Test 
	public void save_itUpdatesAnEntity() throws Exception {
		CnTaxCode entity = CnTaxCode.retrieve(groupId, "8419d770-5a82-0132-9116-6a46f43bd3fe", CnTaxCode.class);
		String newName = entity.getName() + "a";
		entity.setName(newName);
		
		assertTrue(entity.save());
		assertTrue(entity.getId() != null);
		assertEquals(this.groupId,entity.getGroupId());
		assertEquals(newName,entity.getName());
	}
}
