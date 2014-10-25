package com.maestrano.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.maestrano.Maestrano;

public class MnoBillTest {
	private Properties props = new Properties();
	
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
		List<String> l = new ArrayList<String>();
		l.add("bla");
		System.out.println(l);
	}
}
