package com.maestrano.connec;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.reflect.TypeToken;
import com.maestrano.helpers.MnoDateHelper;
import com.maestrano.net.ConnecClient;

public class CnItemTest {
	private String jsonStr;
	private CnItem subject;
	private Map<String,Object> hash;
	
	@Before
	public void beforeEach() {
		jsonStr = "{\"created_at\":\"2014-11-18T03:53:13Z\",\"updated_at\":\"2014-11-18T03:53:13Z\",\"id\":\"57cb40b1-5104-0132-6968-600308937d74\",\"name\":\"MyItem3\",\"code\":\"ITEM-3\",\"description\":\"SomeProduct\",\"status\":\"ACTIVE\",\"type\":\"PURCHASED\",\"unit\":\"EACH\",\"sale_price\":{\"total_amount\":0.0,\"net_amount\":0.0,\"tax_amount\":0.0,\"tax_rate\":10.0,\"currency\":\"USD\"},\"purchase_price\":{\"total_amount\":0.0,\"net_amount\":0.0,\"tax_amount\":0.0,\"tax_rate\":10.0,\"currency\":\"USD\"},\"parent_item_id\":\"57cbdcf1-5104-0132-6969-600308937d74\"}";
		
		// Get hash version of the json string
		Type stringStringMap = new TypeToken<Map<String, Object>>(){}.getType();
		hash = ConnecClient.GSON.fromJson(jsonStr, stringStringMap);
		
		subject = CnItem.fromJson(jsonStr);
	}
	
	@Test
	public void toJson_itReturnsTheRightRepresentation() {
		assertEquals(ConnecClient.GSON.toJson(subject),subject.toJson());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void gsonCasting_itBuildsTheRightObject() {
		assertEquals(hash.get("id"),subject.getId()); 
		assertEquals(hash.get("created_at"), MnoDateHelper.toIso8601(subject.getCreatedAt()));
		assertEquals(hash.get("updated_at"), MnoDateHelper.toIso8601(subject.getUpdatedAt()));
		assertEquals(hash.get("name"),subject.getName());
		assertEquals(hash.get("code"),subject.getCode());
		assertEquals(hash.get("unit"),subject.getUnit());
		assertEquals(hash.get("description"),subject.getDescription());
		
		// Embedded Sale Price
		Map<String,Object> salePrice = (Map<String, Object>) hash.get("sale_price");
		assertEquals(salePrice.get("total_amount"),subject.getSalePrice().getTotalAmount());
		assertEquals(salePrice.get("tax_rate"),subject.getPurchasePrice().getTaxRate());
		
		// Embedded Purchase Price
		Map<String,Object> purchasePrice = (Map<String, Object>) hash.get("sale_price");
		assertEquals(purchasePrice.get("total_amount"),subject.getPurchasePrice().getTotalAmount());
		assertEquals(purchasePrice.get("tax_rate"),subject.getPurchasePrice().getTaxRate());
	}
	
	@Test
	public void fromHash_itBuildsTheRightObjectWhenHashMapProvided() {
		Map<String,Object> camelCaseHash = new HashMap<String,Object>();
		camelCaseHash.put("name", "Chair");
		camelCaseHash.put("description", "Some chair of some color");
		
		// Tests
		subject = CnItem.fromMap(camelCaseHash);
		assertEquals(camelCaseHash.get("name"),subject.getName());
		assertEquals(camelCaseHash.get("description"),subject.getDescription());
	}
}
