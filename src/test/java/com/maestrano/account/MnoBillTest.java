package com.maestrano.account;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.maestrano.Maestrano;
import com.maestrano.testhelpers.MnoHttpClientStub;

public class MnoBillTest {
	private Properties props = new Properties();
	private MnoHttpClientStub httpClient = new MnoHttpClientStub();
	
	@Before
	public void beforeEach() {
		props.setProperty("app.environment", "test");
		props.setProperty("api.id", "someid");
		props.setProperty("api.key", "somekey");
		Maestrano.configure(props);
	}
	
	@Test 
	public void retrieve_itRetrievesASingleBill() {
		
	}
	
	@Test
	public void create_itCreatesANewBill() {
		
	}
	
	@Test
	public void all_itRetrievesAllBills() {
		
	}
	
	@Test
	public void cancel_itCancelsABill() {
		MnoBill bill = new MnoBill();
		bill.setDescription("bla");
		MnoBill newBill = new MnoBill();
		newBill.merge(bill);
	}
}
