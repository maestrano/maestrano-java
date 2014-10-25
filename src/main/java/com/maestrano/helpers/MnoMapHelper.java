package com.maestrano.helpers;

import java.util.HashMap;
import java.util.Map;

public class MnoMapHelper {
	
	/**
	 * Convert all keys to snake case style
	 * @param hash
	 * @return
	 */
	public static Map<String,Object> toUnderscoreHash(Map<String,Object> hash) {
		Map<String,Object> newHash = new HashMap<String,Object>();
		
		for (Map.Entry<String, Object> entry : hash.entrySet())
		{
			newHash.put(MnoStringHelper.toSnakeCase(entry.getKey()), entry.getValue());
		}
		
		return newHash;
	}
}
