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

public class CnTaxRateTest {
	private String jsonStr;
	private CnTaxRate subject;
	private Map<String,Object> hash;
	
	@Before
	public void beforeEach() {
		jsonStr = "{\"created_at\": \"2014-11-18T04:24:19Z\",\"updated_at\": \"2014-11-18T04:24:19Z\",\"id\": \"b02d2671-5108-0132-6a6b-600308937d74\", \"name\": \"GST (sales)\",\"rate\": 6.0}";
		
		// Get hash version of the json string
		Type stringStringMap = new TypeToken<Map<String, Object>>(){}.getType();
		hash = ConnecClient.GSON.fromJson(jsonStr, stringStringMap);
		
		subject = CnTaxRate.fromJson(jsonStr);
	}
	
	@Test
	public void toJson_itReturnsTheRightRepresentation() {
		assertEquals(ConnecClient.GSON.toJson(subject),subject.toJson());
	}
	
	@Test
	public void gsonCasting_itBuildsTheRightObject() {
		assertEquals(hash.get("id"),subject.getId()); 
		assertEquals(hash.get("created_at"), MnoDateHelper.toIso8601(subject.getCreatedAt()));
		assertEquals(hash.get("updated_at"), MnoDateHelper.toIso8601(subject.getUpdatedAt()));
		assertEquals(hash.get("name"),subject.getName());
		assertEquals(hash.get("rate"),subject.getRate());
	}
	
	@Test
	public void fromHash_itBuildsTheRightObjectWhenHashMapProvided() {
		Map<String,Object> camelCaseHash = new HashMap<String,Object>();
		camelCaseHash.put("name", "GST (sales)");
		camelCaseHash.put("rate", 5.0);
		
		subject = CnTaxRate.fromMap(camelCaseHash);
		
		assertEquals(camelCaseHash.get("name"),subject.getName());
		assertEquals(camelCaseHash.get("rate"),subject.getRate());
	}
}
