package com.maestrano.connec;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.reflect.TypeToken;
import com.maestrano.helpers.MnoDateHelper;
import com.maestrano.net.ConnecClient;

public class CnTaxCodeTest {
	private String jsonStr;
	private CnTaxCode subject;
	private Map<String,Object> hash;
	
	@Before
	public void beforeEach() {
		jsonStr = "{\"created_at\":\"2014-11-18T04:22:47Z\",\"updated_at\":\"2014-11-18T04:22:47Z\",\"id\":\"79b86a51-5108-0132-6a09-600308937d74\",\"name\":\"CustomSalesTax\",\"description\":\"CustomStateTax\",\"sales_taxes\":[{\"id\":\"79b3af61-5108-0132-6a04-600308937d74\",\"name\":\"GST(sales)\",\"rate\":6.0}],\"purchase_taxes\":[{\"id\":\"79b5f951-5108-0132-6a07-600308937d74\",\"name\":\"GST(purchase)\",\"rate\":1.0}],\"sale_tax_rate\":6.0,\"purchase_tax_rate\":1.0}";
		
		// Get hash version of the json string
		Type stringStringMap = new TypeToken<Map<String, Object>>(){}.getType();
		hash = ConnecClient.GSON.fromJson(jsonStr, stringStringMap);
		
		subject = CnTaxCode.fromJson(jsonStr);
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
		assertEquals(hash.get("description"),subject.getDescription());
		assertEquals(hash.get("sale_tax_rate"), subject.getSaleTaxRate());
		assertEquals(hash.get("purchase_tax_rate"), subject.getPurchaseTaxRate());
		
		// Embedded Sales Taxes
		Map<String,Object> firstSaleTax = ((List<Map<String, Object>>) hash.get("sales_taxes")).get(0);
		assertEquals(firstSaleTax.get("name"),subject.getSalesTaxes().get(0).getName());
		assertEquals(firstSaleTax.get("rate"),subject.getSalesTaxes().get(0).getRate());
		
		// Embedded Purchase Taxes
		Map<String,Object> firstPurchaseTax = ((List<Map<String, Object>>) hash.get("purchase_taxes")).get(0);
		assertEquals(firstPurchaseTax.get("name"),subject.getPurchaseTaxes().get(0).getName());
		assertEquals(firstPurchaseTax.get("rate"),subject.getPurchaseTaxes().get(0).getRate());
	}
	
	@Test
	public void fromHash_itBuildsTheRightObjectWhenHashMapProvided() {
		Map<String,Object> camelCaseHash = new HashMap<String,Object>();
		camelCaseHash.put("name", "GST (sales)");
		camelCaseHash.put("description", "Some tax code description");
		camelCaseHash.put("saleTaxRate", 5.0);
		camelCaseHash.put("purchaseTaxRate", 4.0);
		
		// Tests
		subject = CnTaxCode.fromMap(camelCaseHash);
		assertEquals(camelCaseHash.get("name"),subject.getName());
		assertEquals(camelCaseHash.get("description"),subject.getDescription());
		assertEquals(camelCaseHash.get("saleTaxRate"), subject.getSaleTaxRate());
		assertEquals(camelCaseHash.get("purchaseTaxRate"), subject.getPurchaseTaxRate());
	}
}
