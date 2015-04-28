package com.maestrano.connec;

import java.util.List;
import java.util.Map;

import com.maestrano.helpers.MnoMapHelper;
import com.maestrano.net.ConnecClient;

public class CnTaxCode extends ConnecResource {
	private String name;
	private String description;
	private double saleTaxRate;
	private double purchaseTaxRate;
	private List<CnTaxRate> saleTaxes;
	private List<CnTaxRate> purchaseTaxes;
	
	public String getEntityName() {
        return "tax_codes";
    }
    
    public static Class<?> getEntityClass() {
        return CnTaxCode.class;
    }
	
	/**
	 * Instantiate a new TaxRate from a JSON string
	 * @param jsonStr
	 * @return new tax code object
	 */
	public static CnTaxCode fromJson(String jsonStr) {
		CnTaxCode obj = ConnecClient.GSON.fromJson(jsonStr, CnTaxCode.class);
		return obj;
	}
	
	/**
	 * Instantiate a new TaxRate entity from a map of attributes
	 * @param map of attributes
	 * @return new tax code object
	 */
	public static CnTaxCode fromMap(Map<String,Object> map) {
		String jsonStr = ConnecClient.GSON.toJson(MnoMapHelper.toUnderscoreHash(map));
		CnTaxCode obj = fromJson(jsonStr);
		return obj;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getSaleTaxRate() {
		return saleTaxRate;
	}
	public void setSaleTaxRate(double saleTaxRate) {
		this.saleTaxRate = saleTaxRate;
	}
	public double getPurchaseTaxRate() {
		return purchaseTaxRate;
	}
	public void setPurchaseTaxRate(double purchaseTaxRate) {
		this.purchaseTaxRate = purchaseTaxRate;
	}
	public List<CnTaxRate> getSaleTaxes() {
		return saleTaxes;
	}
	public void setSaleTaxes(List<CnTaxRate> saleTaxes) {
		this.saleTaxes = saleTaxes;
	}
	public List<CnTaxRate> getPurchaseTaxes() {
		return purchaseTaxes;
	}
	public void setPurchaseTaxes(List<CnTaxRate> purchaseTaxes) {
		this.purchaseTaxes = purchaseTaxes;
	}
	
	
}
