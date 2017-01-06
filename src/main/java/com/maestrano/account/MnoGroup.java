package com.maestrano.account;

import java.util.Date;

import com.maestrano.Maestrano;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.net.MnoAccountClient;

public class MnoGroup extends MnoObject {
	private String id;
	private Date createdAt;
	private Date updatedAt;
	private Boolean hasCreditCard;
	private String status;

	/**
	 * @deprecated use {@link #client(Maestrano)} instead
	 * 
	 * @return
	 */
	public static MnoGroupClient client() {
		return new MnoGroupClient();
	}

	public static MnoGroupClient client(String marketplace) throws MnoConfigurationException {
		return new MnoGroupClient(marketplace);
	}

	public static MnoGroupClient client(Maestrano maestrano) {
		return new MnoGroupClient(maestrano);
	}

	public static class MnoGroupClient extends MnoAccountClient<MnoGroup> {
		public MnoGroupClient(Maestrano maestrano) {
			super(MnoGroup.class, maestrano);
		}

		public MnoGroupClient(String marketplace) throws MnoConfigurationException {
			super(MnoGroup.class, Maestrano.get(marketplace));
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
