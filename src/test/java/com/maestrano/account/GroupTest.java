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

import com.maestrano.account.Group.GroupClient;
import com.maestrano.configuration.Preset;
import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.exception.InvalidRequestException;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.helpers.ResourcesHelper;
import com.maestrano.testhelpers.DefaultPropertiesHelper;

import net.jadler.stubbing.server.jdk.JdkStubHttpServer;

public class GroupTest {

	private Preset preset;
	private GroupClient subject;

	@Before
	public void beforeEach() throws MnoConfigurationException {
		initJadlerUsing(new JdkStubHttpServer());
		Properties properties = DefaultPropertiesHelper.loadDefaultProperties();
		properties.setProperty("api.id", "someid");
		properties.setProperty("api.id", "someid");
		properties.setProperty("api.host", "http://localhost:" + port());
		preset = new Preset("test", properties);
		subject = Group.client(preset);
	}

	private static final String GROUP_JSON = ResourcesHelper.getResource("/com/maestrano/account/group.json");
	private static final String GROUPS_JSON = ResourcesHelper.getResource("/com/maestrano/account/groups.json");

	@Test
	public void test_all() throws MnoConfigurationException, AuthenticationException, ApiException, InvalidRequestException, IOException {

		onRequest().havingMethodEqualTo("GET").havingPathEqualTo("/api/v1/account/groups").respond().withBody(GROUPS_JSON).withStatus(200).withEncoding(Charset.forName("UTF-8"))
				.withContentType("application/json; charset=UTF-8");

		List<Group> groups = subject.all();

		assertEquals(1, groups.size());
		Group group = groups.get(0);

		assertGroup(group);
	}

	@Test
	public void test_retrieve() throws AuthenticationException, ApiException, InvalidRequestException {

		onRequest().havingMethodEqualTo("GET").havingPathEqualTo("/api/v1/account/groups/cld-001").respond().withBody(GROUP_JSON).withStatus(200)
				.withEncoding(Charset.forName("UTF-8")).withContentType("application/json; charset=UTF-8");

		Group group = subject.retrieve("cld-001");

		assertGroup(group);

	}

	@Test
	public void test_create() throws IOException, AuthenticationException, ApiException, InvalidRequestException {

		onRequest().havingMethodEqualTo("POST").havingPathEqualTo("/api/v1/account/groups").respond().withBody(GROUP_JSON).withStatus(200).withEncoding(Charset.forName("UTF-8"))
				.withContentType("application/json; charset=UTF-8");

		Map<String, Object> hash = new HashMap<String, Object>();
		hash.put("name", "John");
		hash.put("surname", "Smith");
		hash.put("email", "john.smith@gmail.com");

		Group group = subject.create(hash);

		assertGroup(group);

	}

	@Test
	public void test_delete() throws AuthenticationException, ApiException, IOException {
		onRequest().havingMethodEqualTo("DELETE").havingPathEqualTo("/api/v1/account/groups/cld-001").respond().withBody(GROUP_JSON).withStatus(200)
				.withEncoding(Charset.forName("UTF-8")).withContentType("application/json; charset=UTF-8");

		Group group = subject.delete("cld-001");

		assertGroup(group);
	}

	private static void assertGroup(Group group) {
		assertEquals("cld-001", group.getId());
		assertEquals(ResourcesHelper.parseDate("2014-05-21T00:37:34Z"), group.getCreatedAt());
		assertEquals(ResourcesHelper.parseDate("2015-03-09T06:37:28Z"), group.getUpdatedAt());
		assertEquals("terminated", group.getStatus());
	}

	@After
	public void tearDown() {
		closeJadler();
	}
}



