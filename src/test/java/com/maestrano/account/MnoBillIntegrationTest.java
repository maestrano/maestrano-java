package com.maestrano.account;

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
import com.maestrano.account.MnoBill.MnoBillClient;
import com.maestrano.helpers.MnoDateHelper;

public class MnoBillIntegrationTest {
	private Properties props = new Properties();
	private MnoBillClient mnoBillClient;
	
	@Before
	public void beforeEach() {
		props.setProperty("app.environment", "test");
		props.setProperty("api.id", "app-1");
		props.setProperty("api.key", "gfcmbu8269wyi0hjazk4t7o1sndpvrqxl53e1");
		Maestrano.reloadConfiguration(props);
		mnoBillClient = MnoBill.client();
	}
	
	@Test
	public void all_itRetrievesAllBills() throws Exception {
		List<MnoBill> billList = mnoBillClient.all();
		MnoBill bill = billList.get(0);
		
		assertEquals("bill-1",bill.getId());
		assertEquals("cld-3",bill.getGroupId());
		assertEquals("2300",bill.getPriceCents().toString());
		assertEquals("2014-05-29T05:57:10Z",MnoDateHelper.toIso8601(bill.getCreatedAt()));
	}
	
	@Test
	public void all_withFilters_itRetrievesSelectBills() throws Exception {
		Map<String,String> filters = new HashMap<String,String>();
		filters.put("status", "cancelled");
		
		List<MnoBill> billList = mnoBillClient.all(filters);
		
		assertTrue(billList.size() > 0);
		
		for (MnoBill bill : billList) {
			assertEquals("cancelled",bill.getStatus());
		}
	}
	
	@Test 
	public void retrieve_itRetrievesASingleBill() throws Exception {
		MnoBill bill = mnoBillClient.retrieve("bill-1");
		
		assertEquals("bill-1",bill.getId());
		assertEquals("cld-3",bill.getGroupId());
		assertEquals("2300",bill.getPriceCents().toString());
		assertEquals("2014-05-29T05:57:10Z",MnoDateHelper.toIso8601(bill.getCreatedAt()));
	}
	
	@Test
	public void create_itCreatesANewBill() throws Exception {
		Map<String, Object> attrsMap = new HashMap<String, Object>();
		attrsMap.put("groupId", "cld-3");
		attrsMap.put("priceCents", 2000);
		attrsMap.put("description", "Product purchase");
		
		MnoBill bill = mnoBillClient.create(attrsMap);
		
		assertFalse(bill.getId() == null);
		assertEquals("cld-3",bill.getGroupId());
		assertEquals("2000",bill.getPriceCents().toString());
		assertFalse(bill.getCreatedAt() == null);
	}
	
	@Test
	public void cancel_itCancelsABill() throws Exception {
		Map<String, Object> attrsMap = new HashMap<String, Object>();
		attrsMap.put("groupId", "cld-3");
		attrsMap.put("priceCents", 2000);
		attrsMap.put("description", "Product purchase");
		MnoBill bill = mnoBillClient.create(attrsMap);
		
		assertTrue(mnoBillClient.cancel(bill));
		assertEquals("cancelled",bill.getStatus());
	}
}
