package com.maestrano.account;

import java.util.Date;

import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.net.MnoAccountClient;

public class MnoGroup extends MnoObject {
	private String id;
	private Date createdAt;
	private Date updatedAt;
	private Boolean hasCreditCard;
	private String status;

	public static MnoGroupClient client() {
		return new MnoGroupClient();
	}

	public static MnoGroupClient client(String presetId) throws MnoConfigurationException {
		return new MnoGroupClient(presetId);
	}

	public static class MnoGroupClient extends MnoAccountClient<MnoGroup> {
		public MnoGroupClient(String preset) throws MnoConfigurationException {
			super(MnoGroup.class, preset);
		}

		public MnoGroupClient() {
			super(MnoGroup.class);
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
