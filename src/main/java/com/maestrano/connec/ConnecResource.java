package com.maestrano.connec;

import java.util.Date;

import com.maestrano.net.ConnecClient;

class ConnecResource extends ConnecObject {
	protected String id;
	protected Date createdAt;
	protected Date updatedAt;
	protected String groupId;
	
	public ConnecResource() {
		super();
	}
	
	/**
	 * Return a JSON representation of the entity
	 * @return
	 */
	public String toJson() {
		return ConnecClient.GSON.toJson(this);
	}
	
	/**
	 * Return a String representation of the entity (JSON representation)
	 */
	public String toString() {
		return this.toJson();
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
