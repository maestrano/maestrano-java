package com.maestrano.account;

import static net.jadler.Jadler.closeJadler;
import static net.jadler.Jadler.initJadlerUsing;
import static net.jadler.Jadler.onRequest;
import static net.jadler.Jadler.port;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.maestrano.account.Bill.BillClient;
import com.maestrano.configuration.Preset;
import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.exception.InvalidRequestException;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.helpers.ResourcesHelper;
import com.maestrano.testhelpers.DefaultPropertiesHelper;

import net.jadler.stubbing.server.jdk.JdkStubHttpServer;

public class BillTest {

	private Preset preset;
	private BillClient subject;

	@Before
	public void beforeEach() throws MnoConfigurationException {
		initJadlerUsing(new JdkStubHttpServer());
		Properties properties = DefaultPropertiesHelper.loadDefaultProperties();
		properties.setProperty("api.id", "someid");
		properties.setProperty("api.id", "someid");
		properties.setProperty("api.host", "http://localhost:" + port());
		preset = new Preset("test", properties);
		subject = Bill.client(preset);
	}

	private static final String BILL_JSON = ResourcesHelper.getResource("/com/maestrano/account/bill.json");
	private static final String BILLS_JSON = ResourcesHelper.getResource("/com/maestrano/account/bills.json");

	@Test
	public void test_all() throws MnoConfigurationException, AuthenticationException, ApiException, InvalidRequestException, IOException {

		onRequest().havingMethodEqualTo("GET").havingPathEqualTo("/api/v1/account/bills").respond().withBody(BILLS_JSON).withStatus(200).withEncoding(Charset.forName("UTF-8"))
				.withContentType("application/json; charset=UTF-8");

		List<Bill> bills = subject.all();

		assertEquals(1, bills.size());
		Bill bill = bills.get(0);

		assertBill(bill);
	}

	@Test
	public void test_retrieve() throws AuthenticationException, ApiException, InvalidRequestException {

		onRequest().havingMethodEqualTo("GET").havingPathEqualTo("/api/v1/account/bills/usr-001").respond().withBody(BILL_JSON).withStatus(200)
				.withEncoding(Charset.forName("UTF-8")).withContentType("application/json; charset=UTF-8");

		Bill bill = subject.retrieve("usr-001");

		assertBill(bill);

	}

	@Test
	public void test_create() throws IOException, AuthenticationException, ApiException, InvalidRequestException {

		onRequest().havingMethodEqualTo("POST").havingPathEqualTo("/api/v1/account/bills").respond().withBody(BILL_JSON).withStatus(200).withEncoding(Charset.forName("UTF-8"))
				.withContentType("application/json; charset=UTF-8");

		Map<String, Object> hash = new HashMap<String, Object>();
		hash.put("name", "John");
		hash.put("surname", "Smith");
		hash.put("email", "john.smith@gmail.com");

		Bill bill = subject.create(hash);

		assertBill(bill);

	}

	@Test
	public void test_delete() throws AuthenticationException, ApiException, IOException {
		onRequest().havingMethodEqualTo("DELETE").havingPathEqualTo("/api/v1/account/bills/usr-001").respond().withBody(BILL_JSON).withStatus(200)
				.withEncoding(Charset.forName("UTF-8")).withContentType("application/json; charset=UTF-8");

		Bill bill = subject.delete("usr-001");

		assertBill(bill);
	}

	private static void assertBill(Bill bill) {
		assertEquals("bill-001", bill.getId());
		assertEquals("submitted", bill.getStatus());
		assertEquals("AUD", bill.getCurrency());
		assertEquals(ResourcesHelper.parseDate("2015-06-03T05:00:33Z"), bill.getCreatedAt());
		assertEquals(ResourcesHelper.parseDate("2015-06-03T05:00:33Z"), bill.getUpdatedAt());
		assertEquals(null, bill.getPeriodStartedAt());
		assertEquals(null, bill.getPeriodEndedAt());
		assertEquals("cld-001", bill.getGroupId());
		assertEquals(new Integer(2000), bill.getPriceCents());
		assertEquals("A bill", bill.getDescription());
	}

	@After
	public void tearDown() {
		closeJadler();
	}
}



