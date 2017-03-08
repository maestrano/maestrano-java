package com.maestrano.helpers;

import java.util.Properties;

import org.junit.Test;

import com.google.gson.Gson;

import junit.framework.Assert;

public class MnoPropertiesHelperTest {

	@Test
	public void fromJson_itReturnsTheRightProperty() {
		String json = "{'a_field':'value','b':'78', 'nested':{'a_type':'value'}}";
		Object object = new Gson().fromJson(json, Object.class);
		Properties properties = new Properties();
		properties.setProperty("aField", "value");
		properties.setProperty("b", "78");
		properties.setProperty("nested.aType", "value");

		Assert.assertEquals(properties, MnoPropertiesHelper.fromJson(object));
	}

}
