package com.maestrano.connec;

import java.util.Map;

import com.maestrano.helpers.MnoMapHelper;
import com.maestrano.net.ConnecClient;

public class CnPrice extends ConnecObject {
	private double totalAmount;
	private double netAmount;
	private double taxAmount;
	private double taxRate;
	private String currency;
	
	/**
	 * Instantiate a new Price from a JSON string
	 * @param jsonStr
	 * @return new price object
	 */
	public static CnPrice fromJson(String jsonStr) {
		CnPrice obj = ConnecClient.GSON.fromJson(jsonStr, CnPrice.class);
		return obj;
	}
	
	/**
	 * Instantiate a new Price object from a map of attributes
	 * @param map of attributes
	 * @return new price object
	 */
	public static CnPrice fromMap(Map<String,Object> map) {
		String jsonStr = ConnecClient.GSON.toJson(MnoMapHelper.toUnderscoreHash(map));
		CnPrice obj = fromJson(jsonStr);
		return obj;
	}
	
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public double getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}
	public double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}
	public double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
