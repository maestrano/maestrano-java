package com.maestrano.connec;

import java.util.Map;

import com.maestrano.helpers.MnoMapHelper;
import com.maestrano.net.ConnecClient;

public class CnItem extends ConnecResource {
	private String name;
	private String code;
	private String description;
	private String status;
	private String type;
	private String unit;
	private String parentItemId;
	private CnPrice salePrice;
	private CnPrice purchasePrice;
	
	public String getEntityName() {
        return "items";
    }
    
    public static Class<?> getEntityClass() {
        return CnItem.class;
    }
	
	/**
	 * Instantiate a new Item from a JSON string
	 * @param jsonStr
	 * @return new tax code object
	 */
	public static CnItem fromJson(String jsonStr) {
		CnItem obj = ConnecClient.GSON.fromJson(jsonStr, CnItem.class);
		return obj;
	}
	
	/**
	 * Instantiate a new Item entity from a map of attributes
	 * @param map of attributes
	 * @return new tax code object
	 */
	public static CnItem fromMap(Map<String,Object> map) {
		String jsonStr = ConnecClient.GSON.toJson(MnoMapHelper.toUnderscoreHash(map));
		CnItem obj = fromJson(jsonStr);
		return obj;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public CnPrice getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(CnPrice salePrice) {
		this.salePrice = salePrice;
	}
	public CnPrice getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(CnPrice purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public String getParentItemId() {
		return parentItemId;
	}
	public void setParentItemId(String parentItemId) {
		this.parentItemId = parentItemId;
	}
}
