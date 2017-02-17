package com.maestrano.account;

import java.util.Date;

import com.maestrano.configuration.Preset;
import com.maestrano.net.AccountClient;

public class Group extends MnoObject {
	private String id;
	private Date createdAt;
	private Date updatedAt;
	private Boolean hasCreditCard;
	private String status;


	public static GroupClient client(Preset preset) {
		return new GroupClient(preset);
	}

	public static class GroupClient extends AccountClient<Group> {
		public GroupClient(Preset preset) {
			super(Group.class, preset);
		}
	}

	public String getId() {
		return id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public Boolean getHasCreditCard() {
		return hasCreditCard;
	}

	public String getStatus() {
		return status;
	}
}
