package com.maestrano.connec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.reflect.TypeToken;
import com.maestrano.helpers.MnoDateHelper;
import com.maestrano.net.MnoApiConnecClient;

public class CnOrganizationTest {
	private String jsonStr;
	private CnOrganization subject;
	private Map<String,Object> hash;
	
	@Before
	public void beforeEach() {
		jsonStr = "{\"created_at\":\"2014-11-06T04:35:50Z\",\"updated_at\":\"2014-11-06T04:35:50Z\",\"id\":\"50bc49c1-479c-0132-3eac-600308937d74\",\"name\":\"Orga8\",\"industry\":\"Banking\",\"annual_revenue\":800.0,\"capital\":8000.0,\"number_of_employees\":80,\"is_customer\":true,\"is_supplier\":true,\"is_lead\":true,\"address\":{\"billing\":{\"line1\":\"62 Elizabeth Street\",\"line2\":\"\",\"city\":\"Sydney\",\"region\":\"NSW\",\"postal_code\":\"2062\",\"country\":\"AU\"},\"billing2\":{\"line1\":\"63 Elizabeth Street\",\"line2\":\"\",\"city\":\"Sydney\",\"region\":\"NSW\",\"postal_code\":\"2063\",\"country\":\"AU\"},\"shipping\":{\"line1\":\"64 Elizabeth Street\",\"line2\":\"\",\"city\":\"Sydney\",\"region\":\"NSW\",\"postal_code\":\"2064\",\"country\":\"AU\"},\"shipping2\":{\"line1\":\"65 Elizabeth Street\",\"line2\":\"\",\"city\":\"Sydney\",\"region\":\"NSW\",\"postal_code\":\"2065\",\"country\":\"AU\"}},\"email\":{\"address\":\"john13@maestrano.com\",\"address2\":\"jack13@example.com\"},\"website\":{\"url\":\"www.website13.com\",\"url2\":\"www.mywebsite13.com\"},\"phone\":{\"landline\":\"+61 2 8574 1216\",\"landline2\":\"+1 2 8574 1216\",\"mobile\":\"+61 449 785 116\",\"mobile2\":\"+1 449 785 116\",\"fax\":\"+61 2 9974 1216\",\"fax2\":\"+1 2 9974 1216\",\"pager\":\"+61 440 785 116\",\"pager2\":\"+1 440 785 116\"}}";
		
		// Get hash version of the json string
		Type stringStringMap = new TypeToken<Map<String, Object>>(){}.getType();
		hash = MnoApiConnecClient.GSON.fromJson(jsonStr, stringStringMap);
		
		subject = CnOrganization.fromJson(jsonStr);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void gsonCasting_itBuildsTheRightObject() {
		assertEquals(hash.get("id"),subject.getId()); 
		assertEquals(hash.get("created_at"), MnoDateHelper.toIso8601(subject.getCreatedAt()));
		assertEquals(hash.get("updated_at"), MnoDateHelper.toIso8601(subject.getUpdatedAt()));
		assertEquals(hash.get("name"),subject.getName());
		
		Map<String,Object> addressGroup = (Map<String,Object>) hash.get("address");
		Map<String,String> billing = (Map<String,String>) addressGroup.get("billing");
		assertEquals(billing.get("line1"),subject.getAddress().getBilling().getLine1());
		assertEquals(billing.get("city"),subject.getAddress().getBilling().getCity());
		assertEquals(billing.get("region"),subject.getAddress().getBilling().getRegion());
		assertEquals(billing.get("postal_code"),subject.getAddress().getBilling().getPostalCode());
		
		Map<String,Object> phone = (Map<String,Object>) hash.get("phone");
		assertEquals(phone.get("landline"),subject.getPhone().getLandline());
		assertEquals(phone.get("mobile"),subject.getPhone().getMobile());
		assertEquals(phone.get("fax"),subject.getPhone().getFax());
	}
	
	@Test
	public void attributes_itTracksChanges() {
		subject.setIndustry("something");
		
		//System.out.println(MnoApiConnecClient.GSON.toJson(subject.getChangedAttributes()));
		
		assertTrue(subject.isChanged());
	}
	
	@Test
	public void subObject_itTracksChanges() {
		subject.getAddress().getBilling().setCity("some-city-in-the-world");
		
		System.out.println(subject.getAddress());
		
		//System.out.println(MnoApiConnecClient.GSON.toJson(subject.getChangedAttributes()));
		//System.out.println(subject.getAddress()._parentPcs);
		
		assertTrue(subject.isChanged());
	}
}
