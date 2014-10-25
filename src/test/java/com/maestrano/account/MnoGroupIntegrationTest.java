package com.maestrano.account;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.maestrano.Maestrano;
import com.maestrano.helpers.MnoDateHelper;

public class MnoGroupIntegrationTest {
	private Properties props = new Properties();

	@Before
	public void beforeEach() {
		props.setProperty("app.environment", "test");
		props.setProperty("api.id", "app-1");
		props.setProperty("api.key", "gfcmbu8269wyi0hjazk4t7o1sndpvrqxl53e1");
		Maestrano.configure(props);
	}

	@Test
	public void all_itRetrievesAllGroups() throws Exception {
		List<MnoGroup> list = MnoGroup.all();
		MnoGroup entity = null;
		for (MnoGroup elem : list) {
			if (elem.getId().equals("cld-3")) entity = elem;
		}
		
		assertEquals("cld-3",entity.getId());
		assertEquals("2014-05-21T00:31:26Z",MnoDateHelper.toIso8601(entity.getCreatedAt()));
	}

	@Test 
	public void retrieve_itRetrievesASingleGroup() throws Exception {
		MnoGroup entity = MnoGroup.retrieve("cld-3");

		assertEquals("cld-3",entity.getId());
		assertEquals("2014-05-21T00:31:26Z",MnoDateHelper.toIso8601(entity.getCreatedAt()));
	}
}
