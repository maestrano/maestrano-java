package com.maestrano.account;

import com.maestrano.configuration.Preset;
import com.maestrano.net.AccountClient;

public class User extends MnoObject {

	private String name;
	private String surname;
	private String email;
	private String companyName;
	private String country;
	private String ssoSession;

	public static UserClient client(Preset preset) {
		return new UserClient(preset);
	}

	public static class UserClient extends AccountClient<User> {
		public UserClient(Preset preset) {
			super(User.class, preset);
		}
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

}
