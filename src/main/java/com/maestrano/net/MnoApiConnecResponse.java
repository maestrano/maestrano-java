package com.maestrano.net;

import java.util.List;
import java.util.Map;

public class MnoApiConnecResponse<T> {
	private T entity;
	private List<T> entities;
	private Map<String,String> metadata;
	
	/**
	 * Count the number of record returned
	 * @return number of records in the response
	 */
	public Integer getCount() {
		if (metadata != null && metadata.get("count") != null) {
			return Integer.parseInt(metadata.get("count"));
		}
		
		if (entities == null && entity !=null) {
			return 1;
		}
		
		return 0;
	}
	
	public T getEntity() {
		return entity;
	}
	public void setEntity(T entity) {
		this.entity = entity;
	}
	public List<T> getEntities() {
		return entities;
	}
	public void setEntities(List<T> entities) {
		this.entities = entities;
	}
}
