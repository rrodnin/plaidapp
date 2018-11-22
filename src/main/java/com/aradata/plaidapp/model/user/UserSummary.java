package com.aradata.plaidapp.model.user;

public class UserSummary {

	private String id;
	private String username;
	private String name;

	public UserSummary(String id, String username, String name) {
		this.id = id;
		this.username = username;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
