package com.maestrano.connec;

import java.util.Date;

import com.maestrano.net.MnoApiConnecClient;

class CnObject extends CnBaseObject {
	protected String id;
	protected Date createdAt;
	protected Date updatedAt;
	protected String groupId;
	
	public CnObject() {
		super();
	}
	
	public String toString() {
		return MnoApiConnecClient.GSON.toJson(this);
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
