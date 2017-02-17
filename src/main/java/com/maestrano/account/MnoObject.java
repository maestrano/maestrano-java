package com.maestrano.account;

import java.util.Date;

import com.maestrano.net.AccountClient;

public class MnoObject {

	private String id;
	private Date createdAt;
	private Date updatedAt;

	public String toString() {
		return AccountClient.GSON.toJson(this);
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

}
