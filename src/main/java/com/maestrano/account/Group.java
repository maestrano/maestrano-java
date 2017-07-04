package com.maestrano.account;

import com.maestrano.configuration.Preset;
import com.maestrano.net.AccountClient;

public class Group extends MnoObject {
	private boolean hasCreditCard;
	private String status;


	public static GroupClient client(Preset preset) {
		return new GroupClient(preset);
	}

	public static class GroupClient extends AccountClient<Group> {
		public GroupClient(Preset preset) {
			super(Group.class, preset);
		}
	}

	public boolean getHasCreditCard() {
		return hasCreditCard;
	}

	public String getStatus() {
		return status;
	}
}
