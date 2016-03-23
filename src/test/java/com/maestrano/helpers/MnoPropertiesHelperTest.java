package com.maestrano.helpers;

import java.util.Properties;

import org.junit.Test;

import junit.framework.Assert;

public class MnoPropertiesHelperTest {
	@Test
	public void getPropertyOrDefault_itReturnsTheRightGivenProperty() {
		Properties properties = new Properties();
		properties.setProperty("environment", "overloaded");
		properties.setProperty("api.id", "app-1");
		properties.setProperty("api.key", "gfcmbu8269wyi0hjazk4t7o1sndpvrqxl53e1");

		Assert.assertEquals("overloaded", MnoPropertiesHelper.getPropertyOrDefault(properties, "environment"));
		Assert.assertEquals("app-1", MnoPropertiesHelper.getPropertyOrDefault(properties, "api.id"));
		Assert.assertEquals("gfcmbu8269wyi0hjazk4t7o1sndpvrqxl53e1", MnoPropertiesHelper.getPropertyOrDefault(properties, "api.key"));

	}

	@Test
	public void getPropertyOrDefault_itReturnsTheRightDefaultProperty() {
		Properties properties = new Properties();

		Assert.assertEquals("test", MnoPropertiesHelper.getPropertyOrDefault(properties, "environment"));
		Assert.assertEquals("/maestrano/account/groups/:id", MnoPropertiesHelper.getPropertyOrDefault(properties, "webhook.account.groupsPath"));
		Assert.assertEquals("/maestrano/account/groups/:group_id/users/:id", MnoPropertiesHelper.getPropertyOrDefault(properties, "webhook.account.groupUsersPath"));
		Assert.assertEquals("/maestrano/connec/notifications", MnoPropertiesHelper.getPropertyOrDefault(properties, "webhook.connec.notificationsPath"));

	}

	@Test
	public void getPropertyOrDefault_itReturnsTheRightLegacyProperty() {
		Properties properties = new Properties();
		properties.setProperty("legacyPropertyKey", "value");

		Assert.assertEquals("value", MnoPropertiesHelper.getPropertyOrDefault(properties, "propertyKey", "legacyPropertyKey"));
	}

}
