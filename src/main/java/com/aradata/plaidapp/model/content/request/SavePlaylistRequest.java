package com.aradata.plaidapp.model.content.request;

import javax.validation.constraints.NotNull;

public class SavePlaylistRequest {

	@NotNull
	private String id;

	@NotNull
	private String[] topics;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getTopics() {
		return topics;
	}

	public void setTopics(String[] topics) {
		this.topics = topics;
	}
}
