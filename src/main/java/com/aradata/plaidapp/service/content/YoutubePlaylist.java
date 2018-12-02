package com.aradata.plaidapp.service.content;

import org.springframework.data.annotation.Id;

public class YoutubePlaylist {

	@Id
	private String id;

	private String listId;

	private String[] topics;

	public String[] getTopics() {
		return topics;
	}

	public void setTopics(String[] topics) {
		this.topics = topics;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getListId() {
		return listId;
	}

	public void setListId(String listId) {
		this.listId = listId;
	}
}
