package com.maestrano.account;

import com.maestrano.account.RecurringBill.RecurringBillClient;
import com.maestrano.configuration.Preset;
import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.exception.InvalidRequestException;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.helpers.ResourcesHelper;
import com.maestrano.testhelpers.DefaultPropertiesHelper;
import net.jadler.stubbing.server.jdk.JdkStubHttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static net.jadler.Jadler.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

	private static final String RECURRING_BILLS_JSON = ResourcesHelper.getResource("/com/maestrano/account/recurring-bills.json");
	private static final String RECURRING_BILL_JSON = ResourcesHelper.getResource("/com/maestrano/account/recurring-bill.json");

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

	private static void assertBill(RecurringBill bill) {
		assertEquals("rbill-g8ur", bill.getId());
		assertEquals("cld-9axx", bill.getGroupId());
		assertEquals(ResourcesHelper.parseDate("2016-08-01T15:30:20.000Z"), bill.getCreatedAt());
		assertEquals(ResourcesHelper.parseDate("2017-03-27T16:11:22.000Z"), bill.getUpdatedAt());
		assertEquals("active", bill.getStatus());
		assertEquals("AUD", bill.getCurrency());
		assertEquals("[Test Bill] XDE", bill.getDescription());
		assertEquals(ResourcesHelper.parseDate("2016-08-01T15:30:20.000Z"), bill.getStartDate());
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
