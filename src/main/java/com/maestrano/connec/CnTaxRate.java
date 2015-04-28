package com.maestrano.connec;

import java.util.Map;

import com.maestrano.helpers.MnoMapHelper;
import com.maestrano.net.ConnecClient;

public class CnTaxRate extends ConnecResource {
	private String name;
	private double rate;
	private String taxTypeApplicable;
	
	public String getEntityName() {
        return "tax_rates";
    }
    
    public static Class<?> getEntityClass() {
        return CnTaxRate.class;
    }
	
	/**
	 * Instantiate a new TaxRate from a JSON string
	 * @param jsonStr
	 * @return new tax code object
	 */
	public static CnTaxRate fromJson(String jsonStr) {
		CnTaxRate obj = ConnecClient.GSON.fromJson(jsonStr, CnTaxRate.class);
		return obj;
	}
	
	/**
	 * Instantiate a new TaxRate entity from a map of attributes
	 * @param map of attributes
	 * @return new tax code object
	 */
	public static CnTaxRate fromMap(Map<String,Object> map) {
		String jsonStr = ConnecClient.GSON.toJson(MnoMapHelper.toUnderscoreHash(map));
		CnTaxRate obj = fromJson(jsonStr);
		return obj;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}

	public String getTaxTypeApplicable() {
		return taxTypeApplicable;
	}

	public void setTaxTypeApplicable(String taxTypeApplicable) {
		this.taxTypeApplicable = taxTypeApplicable;
	}
}
