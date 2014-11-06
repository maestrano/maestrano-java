package com.maestrano.connec;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.maestrano.net.MnoApiConnecClient;

class CnObject {
	protected String id;
	protected Date createdAt;
	protected Date updatedAt;
	protected String groupId;
	
	public Map<String,Object> changedAttributes;
	public Map<String,Object> orginalAttributes;
	
	public CnObject() {
		changedAttributes = new HashMap<String,Object>();
		orginalAttributes = new HashMap<String,Object>();
	}
	
	public String toString() {
		return MnoApiConnecClient.GSON.toJson(this);
	}
	
	protected void changeAttribute(String attrName, Object value) {
		try {
			
			Field f = this.getClass().getDeclaredField(attrName);
			Object currentVal = f.get(this);
			f.set(this,value);
			
			if (this.orginalAttributes.get(attrName) == null) {
				this.orginalAttributes.put(attrName, currentVal);
			}
			
			if (this.orginalAttributes.get(attrName) != null 
					&& this.orginalAttributes.get(attrName).equals(value)) {
				this.changedAttributes.remove(attrName);
				this.orginalAttributes.remove(attrName);
			} else {
				this.changedAttributes.put(attrName, value);
			}
		
		} catch (Exception e) {}
	}
	
	protected void merge(Object obj) {
		Field[] fs = this.getClass().getDeclaredFields();
		
		for (Field f : fs) {
			try {
				f.set(this,f.get(obj));
			} catch (Exception e) {}
		}
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}
