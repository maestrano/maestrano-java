package com.maestrano.connec;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Type;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.reflect.TypeToken;
import com.maestrano.helpers.MnoDateHelper;
import com.maestrano.net.ConnecClient;

public class CnCompanyTest {
	private String jsonStr;
	private CnCompany subject;
	private Map<String,Object> hash;
	
	@Before
	public void beforeEach() {
		jsonStr = "{\"created_at\":\"2014-11-06T09:26:40Z\",\"updated_at\":\"2014-11-06T09:26:40Z\",\"id\":\"f1d541e1-47c4-0132-421e-600308937d74\",\"name\":\"My Company\",\"currency\":\"USD\",\"note\":\"This is my Own company profile\",\"timezone\":\"America/New_York\",\"email\":{\"address\":\"john12@maestrano.com\",\"address2\":\"jack12@example.com\"},\"address\":{\"billing\":{\"line1\":\"68 Elizabeth Street\",\"line2\":\"\",\"city\":\"Sydney\",\"region\":\"NSW\",\"postal_code\":\"2068\",\"country\":\"AU\"},\"billing2\":{\"line1\":\"69 Elizabeth Street\",\"line2\":\"\",\"city\":\"Sydney\",\"region\":\"NSW\",\"postal_code\":\"2069\",\"country\":\"AU\"},\"shipping\":{\"line1\":\"70 Elizabeth Street\",\"line2\":\"\",\"city\":\"Sydney\",\"region\":\"NSW\",\"postal_code\":\"2070\",\"country\":\"AU\"},\"shipping2\":{\"line1\":\"71 Elizabeth Street\",\"line2\":\"\",\"city\":\"Sydney\",\"region\":\"NSW\",\"postal_code\":\"2071\",\"country\":\"AU\"}},\"website\":{\"url\":\"www.website12.com\",\"url2\":\"www.mywebsite12.com\"},\"phone\":{\"landline\":\"+61 2 8574 1215\",\"landline2\":\"+1 2 8574 1215\",\"mobile\":\"+61 449 785 115\",\"mobile2\":\"+1 449 785 115\",\"fax\":\"+61 2 9974 1215\",\"fax2\":\"+1 2 9974 1215\",\"pager\":\"+61 440 785 115\",\"pager2\":\"+1 440 785 115\"},\"logo\":{\"logo\":\"http://s3.images/logo.png\",\"thumb\":\"http://s3.images/thumb.png\",\"mini_thumb\":\"http://s3.images/mini_thumb.png\"}}";
		
		// Get hash version of the json string
		Type stringStringMap = new TypeToken<Map<String, Object>>(){}.getType();
		hash = ConnecClient.GSON.fromJson(jsonStr, stringStringMap);
		subject = CnCompany.fromJson(jsonStr);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void gsonCasting_itBuildsTheRightObject() {
		assertEquals(hash.get("id"),subject.getId()); 
		assertEquals(hash.get("created_at"), MnoDateHelper.toIso8601(subject.getCreatedAt()));
		assertEquals(hash.get("updated_at"), MnoDateHelper.toIso8601(subject.getUpdatedAt()));
		assertEquals(hash.get("name"),subject.getName());
		
		// Timezone
		assertEquals(hash.get("timezone"),subject.getTimezone().getID());
		
		// Address
		Map<String,Object> addressGroup = (Map<String,Object>) hash.get("address");
		Map<String,String> billing = (Map<String,String>) addressGroup.get("billing");
		assertEquals(billing.get("line1"),subject.getAddress().getBilling().getLine1());
		assertEquals(billing.get("city"),subject.getAddress().getBilling().getCity());
		assertEquals(billing.get("region"),subject.getAddress().getBilling().getRegion());
		assertEquals(billing.get("postal_code"),subject.getAddress().getBilling().getPostalCode());
		
		// Phone
		Map<String,Object> phone = (Map<String,Object>) hash.get("phone");
		assertEquals(phone.get("landline"),subject.getPhone().getLandline());
		assertEquals(phone.get("mobile"),subject.getPhone().getMobile());
		assertEquals(phone.get("fax"),subject.getPhone().getFax());
		
		// Logo
		Map<String,String> logo = (Map<String,String>) hash.get("logo");
		assertEquals(logo.get("logo"),subject.getLogo().getLogo());
		assertEquals(logo.get("thumb"),subject.getLogo().getThumb());
		assertEquals(logo.get("mini_thumb"),subject.getLogo().getMiniThumb());
	}
}
