package com.maestrano.account;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.maestrano.Maestrano;
import com.maestrano.account.MnoGroup.MnoGroupClient;
import com.maestrano.helpers.MnoDateHelper;

public class MnoGroupIntegrationTest {
	private Properties props = new Properties();
	private MnoGroupClient mnoGroupClient;

	@Before
	public void beforeEach() {
		props.setProperty("environment", "test");
		props.setProperty("api.id", "app-1");
		props.setProperty("api.key", "gfcmbu8269wyi0hjazk4t7o1sndpvrqxl53e1");
		Maestrano.reloadConfiguration(props);
		mnoGroupClient = MnoGroup.client();
	}

	@Test
	public void all_itRetrievesAllGroups() throws Exception {
		List<MnoGroup> list = mnoGroupClient.all();
		MnoGroup entity = null;
		for (MnoGroup elem : list) {
			if (elem.getId().equals("cld-3")) entity = elem;
		}
		
		assertEquals("cld-3",entity.getId());
		assertEquals("2014-05-21T00:31:26Z",MnoDateHelper.toIso8601(entity.getCreatedAt()));
	}
	
	@Test
	public void all_withFilters_itRetrievesSelectedGroups() throws Exception {
		Map<String,Object> filters = new HashMap<String,Object>();
		Date d = MnoDateHelper.fromIso8601("2014-06-21T00:31:26Z");
		filters.put("freeTrialEndAtAfter", d);
		filters.put("freeTrialEndAtBefore", d);
		
		List<MnoGroup> billList = mnoGroupClient.all(filters);
		
		assertEquals(1,billList.size());
	}

	@Test 
	public void retrieve_itRetrievesASingleGroup() throws Exception {
		MnoGroup entity = mnoGroupClient.retrieve("cld-3");

		assertEquals("cld-3",entity.getId());
		assertEquals("2014-05-21T00:31:26Z",MnoDateHelper.toIso8601(entity.getCreatedAt()));
	}
}
