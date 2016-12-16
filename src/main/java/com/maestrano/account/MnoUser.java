package com.maestrano.account;

import java.util.Date;

import com.maestrano.Maestrano;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.net.MnoAccountClient;

public class MnoUser extends MnoObject {
	private String id;
	private String name;
	private String surname;
	private String email;
	private String companyName;
	private String country;
	private String ssoSession;
	private Date createdAt;
	private Date updatedAt;

	/**
	 * @deprecated use {@link #client(Maestrano)} instead
	 * @return
	 */
	public static MnoUserClient client() {
		return new MnoUserClient();
	}

	public static MnoUserClient client(String marketplace) throws MnoConfigurationException {
		return new MnoUserClient(marketplace);
	}

	public static MnoUserClient client(Maestrano maestrano) {
		return new MnoUserClient(maestrano);
	}

	public static class MnoUserClient extends MnoAccountClient<MnoUser> {
		public MnoUserClient(Maestrano maestrano) {
			super(MnoUser.class, maestrano);
		}

		public MnoUserClient(String marketplace) throws MnoConfigurationException {
			super(MnoUser.class, Maestrano.get(marketplace));
		}

		public MnoUserClient() {
			super(MnoUser.class);
		}
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getEmail() {
		return email;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getCountry() {
		return country;
	}

	public String getSsoSession() {
		return ssoSession;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

}
