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
	@SuppressWarnings("unchecked")
	public static <V> Map<String,V> toUnderscoreHash(Map<String,V> hash) {
		if (hash == null) return null;
		
		Map<String,V> newHash = new HashMap<String,V>();
		
		for (Map.Entry<String, V> entry : hash.entrySet())
		{
			V value = entry.getValue();
			
			// Apply toUnderscoreHash recursively on sub-hashes
			if (value instanceof Map) {
				value = (V) toUnderscoreHash((Map<String,Object>) value);
			}
			newHash.put(MnoStringHelper.toSnakeCase(entry.getKey()), value);
			
		}
		
		return newHash;
	}
}
