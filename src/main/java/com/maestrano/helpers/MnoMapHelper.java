package com.maestrano.helpers;

import java.util.HashMap;
import java.util.Map;

public class MnoMapHelper {
	
	/**
	 * Convert all keys to snake case style
	 * @param <V>
	 * @param hash
	 * @return
	 */
	public static <V> Map<String,V> toUnderscoreHash(Map<String,V> hash) {
		if (hash == null) return null;
		
		Map<String,V> newHash = new HashMap<String,V>();
		
		for (Map.Entry<String, V> entry : hash.entrySet())
		{
			newHash.put(MnoStringHelper.toSnakeCase(entry.getKey()), entry.getValue());
		}
		
		return newHash;
	}
	
//	public static Map<String,Object> toUnderscoreHash(Map<String,String> hash) {
//		
//	}
}
