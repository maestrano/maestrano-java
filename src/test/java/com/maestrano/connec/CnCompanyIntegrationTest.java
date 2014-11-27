package com.maestrano.connec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.maestrano.Maestrano;
import com.maestrano.account.MnoBill;
import com.maestrano.helpers.MnoDateHelper;

public class CnCompanyIntegrationTest {
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
	public void retrieve_itRetrievesAGroupCompany() throws Exception {
		CnCompany entity = CnCompany.retrieve(groupId);
		
		assertTrue(entity.getId() != null);
		assertEquals("cld-3",entity.getGroupId());
	}
	
	@Test
	public void save_itSavesTheCompanyDetails() throws Exception {
		Map<String,Object> hash = new HashMap<String,Object>();
		hash.put("groupId", groupId);
		hash.put("name", "Doe Pty Ltd");
		CnCompany entity = CnCompany.fromMap(hash);
		entity.save();
		
		// Check that remote attributes were merged
		assertTrue(entity.getId() != null);
		
		// Check that remote entity was updated
		entity = CnCompany.retrieve(groupId);
		assertEquals("Doe Pty Ltd",entity.getName());
	}
}
