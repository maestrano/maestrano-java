package com.maestrano.helpers;

public class MnoStringHelper {

	/**
	 * Convert a string to snake_case
	 * 
	 * @param str
	 * @return
	 */
	public static String toSnakeCase(String str) {
		return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
	}

	/**
	 * Convert a string to CamelCase
	 * 
	 * @param str
	 * @return
	 */
	public static String toCamelCase(String str) {
		String[] wordList = str.toLowerCase().split("_");
		String finalStr = "";
		for (String word : wordList) {
			finalStr += capitalize(word);
		}
		return finalStr;
	}

	public static String capitalize(String line) {
		return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}

}
