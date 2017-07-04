package com.maestrano.account;

import static net.jadler.Jadler.closeJadler;
import static net.jadler.Jadler.initJadlerUsing;
import static net.jadler.Jadler.onRequest;
import static net.jadler.Jadler.port;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.maestrano.account.RecurringBill.RecurringBillClient;
import com.maestrano.configuration.Preset;
import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.exception.InvalidRequestException;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.helpers.MnoDateHelper;
import com.maestrano.testhelpers.DefaultPropertiesHelper;

import net.jadler.stubbing.server.jdk.JdkStubHttpServer;

public class RecurringBillTest {

	private Preset preset;
	private RecurringBillClient subject;

	@Before
	public void beforeEach() throws MnoConfigurationException {
		initJadlerUsing(new JdkStubHttpServer());
		Properties properties = DefaultPropertiesHelper.loadDefaultProperties();
		properties.setProperty("api.id", "someid");
		properties.setProperty("api.id", "someid");
		properties.setProperty("api.host", "http://localhost:" + port());
		preset = new Preset("test", properties);
		subject = RecurringBill.client(preset);
	}

	private static final String RECURRING_BILLS_JSON = getResource("recurring-bills.json");
	private static final String RECURRING_BILL_JSON = getResource("recurring-bill.json");

	@Test
	public void test_all() throws MnoConfigurationException, AuthenticationException, ApiException, InvalidRequestException, IOException {

		onRequest().havingMethodEqualTo("GET").havingPathEqualTo("/api/v1/account/recurring_bills").respond().withBody(RECURRING_BILLS_JSON).withStatus(200).withEncoding(Charset.forName("UTF-8"))
				.withContentType("application/json; charset=UTF-8");

		List<RecurringBill> bills = subject.all();

		assertEquals(1, bills.size());
		RecurringBill bill = bills.get(0);

		assertBill(bill);
	}

	@Test
	public void test_retrieve() throws IOException, AuthenticationException, ApiException, InvalidRequestException {

		onRequest().havingMethodEqualTo("GET").havingPathEqualTo("/api/v1/account/recurring_bills/rbill-g8ur").respond().withBody(RECURRING_BILL_JSON).withStatus(200)
				.withEncoding(Charset.forName("UTF-8")).withContentType("application/json; charset=UTF-8");

		RecurringBill bill = subject.retrieve("rbill-g8ur");

		assertBill(bill);

	}

	@Test
	public void test_create() throws IOException, AuthenticationException, ApiException, InvalidRequestException {

		onRequest().havingMethodEqualTo("POST").havingPathEqualTo("/api/v1/account/recurring_bills").respond().withBody(RECURRING_BILL_JSON).withStatus(200).withEncoding(Charset.forName("UTF-8"))
				.withContentType("application/json; charset=UTF-8");

		Map<String, Object> hash = new HashMap<String, Object>();
		hash.put("groupId", "cld-9axx");
		hash.put("priceCents", 1000);
		hash.put("description", "Product purchase");

		RecurringBill bill = subject.create(hash);

		assertBill(bill);

	}

	@Test
	public void test_delete() throws AuthenticationException, ApiException, IOException {
		onRequest().havingMethodEqualTo("DELETE").havingPathEqualTo("/api/v1/account/recurring_bills/rbill-g8ur").respond().withBody(RECURRING_BILL_JSON).withStatus(200)
				.withEncoding(Charset.forName("UTF-8")).withContentType("application/json; charset=UTF-8");

		subject.delete("rbill-g8ur");
	}

	private static Date parseDate(String string) { // TODO Auto-generated method stub
		try {
			return MnoDateHelper.fromIso8601(string);
		} catch (ParseException e) {
			throw new RuntimeException("Could not parse " + string, e);
		}
	}

	private static String getResource(String path) {
		try {
			return Resources.toString(RecurringBillTest.class.getResource(path), Charsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Could not get resource " + path, e);
		}
	}

	private static void assertBill(RecurringBill bill) {
		assertEquals("rbill-g8ur", bill.getId());
		assertEquals("cld-9axx", bill.getGroupId());
		assertEquals(parseDate("2016-08-01T15:30:20.000Z"), bill.getCreatedAt());
		assertEquals(parseDate("2017-03-27T16:11:22.000Z"), bill.getUpdatedAt());
		assertEquals("active", bill.getStatus());
		assertEquals("AUD", bill.getCurrency());
		assertEquals("[Test Bill] XDE", bill.getDescription());
		assertEquals(parseDate("2016-08-01T15:30:20.000Z"), bill.getStartDate());
		assertEquals("day", bill.getPeriod());
		assertEquals(Integer.valueOf(1), bill.getFrequency());
		assertNull(bill.getCycles());
		assertEquals(Integer.valueOf(10000), bill.getInitialCents());
	}

	@After
	public void tearDown() {
		closeJadler();
	}

}
