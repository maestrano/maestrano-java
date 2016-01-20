package com.maestrano;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class WebhookServiceTest {
	private Properties props = new Properties();
	private WebhookService subject;
	@Before
	public void beforeEach() {
		props.setProperty("app.environment", "production");
		reloadConfiguration();
	}

	private void reloadConfiguration() {
		Maestrano maestrano = Maestrano.reloadConfiguration(props);
		subject = maestrano.webhookService();
	}

	@Test
	public void getAccountGroupsPath_itReturnsTheRightDefaultValue() {
		assertEquals("/maestrano/account/groups/:id", subject.getAccountGroupsPath());
	}
	
	@Test
	public void getAccountGroupsPath_itReturnsTheRightConfigValue() {
		props.setProperty("webhook.account.groupsPath", "/bla/:id");
		
		WebhookService webhookService = Maestrano.reloadConfiguration(props).webhookService();
		assertEquals("/bla/:id", webhookService.getAccountGroupsPath());
	}
	
	@Test
    public void getAccountGroupUsersPath_itReturnsTheRightDefaultValue() {
        props.setProperty("webhook.account.groupUsersPath", "/bla/:id");
        assertEquals("/bla/:id", Maestrano.reloadConfiguration(props).webhookService().getAccountGroupUsersPath());
    }
	
	@Test
    public void getConnecSubscriptions_itReturnsTheRightDefaultValue() {
        props.setProperty("webhook.connec.subscriptions.accounts","true");
        props.setProperty("webhook.connec.subscriptions.items","false");
        
        reloadConfiguration();
        assertEquals(true, subject.getConnecSubscriptions().get("accounts"));
        assertEquals(false, subject.getConnecSubscriptions().get("items"));
    }
	
	@Test
    public void getNotificationsPath_itReturnsTheRightDefaultValue() {
        props.setProperty("webhook.connec.notificationsPath","/path/to/notifications");
        
        reloadConfiguration();
        assertEquals("/path/to/notifications", subject.getNotificationsPath());
    }

	@Test
	public void toMetadataHash_itReturnsTheRightValue() {
		Map<String,Object> hash = subject.toMetadataHash();
		
		Map<String,Object> accountHash = (Map<String, Object>) hash.get("account");
		assertEquals(subject.getAccountGroupsPath(), accountHash.get("groups_path"));
		assertEquals(subject.getAccountGroupUsersPath(), accountHash.get("group_users_path"));
		
		Map<String,Object> connecHash = (Map<String, Object>) hash.get("connec");
		assertEquals(subject.getNotificationsPath(), connecHash.get("notifications_path"));
		assertEquals(subject.getConnecSubscriptions(), connecHash.get("subscriptions"));
	}
}
