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

public class CnPersonTest {
	private String jsonStr;
	private CnPerson subject;
	private Map<String,Object> hash;
	
	@Before
	public void beforeEach() {
		jsonStr = "{\"created_at\":\"2014-11-06T08:37:58Z\",\"updated_at\":\"2014-11-06T08:37:58Z\",\"id\":\"224b4791-47be-0132-4048-600308937d74\",\"title\":\"Mr\",\"first_name\":\"John31\",\"last_name\":\"Beauregard31\",\"birth_date\":\"1986-04-02T00:00:00Z\",\"organization_id\":\"224b6ea1-47be-0132-4049-600308937d74\",\"is_customer\":true,\"is_supplier\":true,\"is_lead\":true,\"address_work\":{\"billing\":{\"line1\":\"516 Elizabeth Street\",\"line2\":\"\",\"city\":\"Sydney\",\"region\":\"NSW\",\"postal_code\":\"2518\",\"country\":\"AU\"},\"billing2\":{\"line1\":\"517 Elizabeth Street\",\"line2\":\"\",\"city\":\"Sydney\",\"region\":\"NSW\",\"postal_code\":\"2519\",\"country\":\"AU\"},\"shipping\":{\"line1\":\"518 Elizabeth Street\",\"line2\":\"\",\"city\":\"Sydney\",\"region\":\"NSW\",\"postal_code\":\"2520\",\"country\":\"AU\"},\"shipping2\":{\"line1\":\"519 Elizabeth Street\",\"line2\":\"\",\"city\":\"Sydney\",\"region\":\"NSW\",\"postal_code\":\"2521\",\"country\":\"AU\"}},\"address_home\":{\"billing\":{\"line1\":\"520 Elizabeth Street\",\"line2\":\"\",\"city\":\"Sydney\",\"region\":\"NSW\",\"postal_code\":\"2522\",\"country\":\"AU\"},\"billing2\":{\"line1\":\"521 Elizabeth Street\",\"line2\":\"\",\"city\":\"Sydney\",\"region\":\"NSW\",\"postal_code\":\"2523\",\"country\":\"AU\"},\"shipping\":{\"line1\":\"522 Elizabeth Street\",\"line2\":\"\",\"city\":\"Sydney\",\"region\":\"NSW\",\"postal_code\":\"2524\",\"country\":\"AU\"},\"shipping2\":{\"line1\":\"523 Elizabeth Street\",\"line2\":\"\",\"city\":\"Sydney\",\"region\":\"NSW\",\"postal_code\":\"2525\",\"country\":\"AU\"}},\"email\":{\"address\":\"john99@maestrano.com\",\"address2\":\"jack99@example.com\"},\"website\":{\"url\":\"www.website97.com\",\"url2\":\"www.mywebsite97.com\"},\"phone_work\":{\"landline\":\"+61 2 8574 12127\",\"landline2\":\"+1 2 8574 12127\",\"mobile\":\"+61 449 785 1127\",\"mobile2\":\"+1 449 785 1127\",\"fax\":\"+61 2 9974 12127\",\"fax2\":\"+1 2 9974 12127\",\"pager\":\"+61 440 785 1127\",\"pager2\":\"+1 440 785 1127\"},\"phone_home\":{\"landline\":\"+61 2 8574 12128\",\"landline2\":\"+1 2 8574 12128\",\"mobile\":\"+61 449 785 1128\",\"mobile2\":\"+1 449 785 1128\",\"fax\":\"+61 2 9974 12128\",\"fax2\":\"+1 2 9974 12128\",\"pager\":\"+61 440 785 1128\",\"pager2\":\"+1 440 785 1128\"},\"notes\":{\"545b3366ebe3908d33000599\":{\"description\":\"Something to be aware of!\",\"tag\":\"123\",\"value\":\"456\"}},\"tasks\":{}}";
		
		// Get hash version of the json string
		Type stringStringMap = new TypeToken<Map<String, Object>>(){}.getType();
		hash = ConnecClient.GSON.fromJson(jsonStr, stringStringMap);
		
		subject = CnPerson.fromJson(jsonStr);
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
		assertEquals(hash.get("first_name"),subject.getFirstName());
		assertEquals(hash.get("last_name"),subject.getLastName());
		
		Map<String,Object> addressGroup = (Map<String,Object>) hash.get("address_work");
		Map<String,String> billing = (Map<String,String>) addressGroup.get("billing");
		assertEquals(billing.get("line1"),subject.getAddressWork().getBilling().getLine1());
		assertEquals(billing.get("city"),subject.getAddressWork().getBilling().getCity());
		assertEquals(billing.get("region"),subject.getAddressWork().getBilling().getRegion());
		assertEquals(billing.get("postal_code"),subject.getAddressWork().getBilling().getPostalCode());
		
		Map<String,Object> phone = (Map<String,Object>) hash.get("phone_work");
		assertEquals(phone.get("landline"),subject.getPhoneWork().getLandline());
		assertEquals(phone.get("mobile"),subject.getPhoneWork().getMobile());
		assertEquals(phone.get("fax"),subject.getPhoneWork().getFax());
	}
	
	@Test
	public void fromHash_itBuildsTheRightObjectWhenHashMapProvided() {
		Map<String,Object> camelCaseHash = new HashMap<String,Object>();
		camelCaseHash.put("name", "Doe Pty Ltd");
		
		Map<String,Object> addressGroup = new HashMap<String,Object>();
		Map<String,Object> billing = new HashMap<String,Object>();
		billing.put("line1", "some street address");
		billing.put("city", "some city");
		billing.put("region", "some region");
		billing.put("postalCode", "some postal code");
		addressGroup.put("billing", billing);
		camelCaseHash.put("address_work", addressGroup);
		
		subject = CnPerson.fromMap(camelCaseHash);
		
		assertEquals(camelCaseHash.get("first_name"),subject.getFirstName());
		
		// Address
		assertEquals(billing.get("line1"),subject.getAddressWork().getBilling().getLine1());
		assertEquals(billing.get("city"),subject.getAddressWork().getBilling().getCity());
		assertEquals(billing.get("region"),subject.getAddressWork().getBilling().getRegion());
		assertEquals(billing.get("postalCode"),subject.getAddressWork().getBilling().getPostalCode());
		
	}
}
