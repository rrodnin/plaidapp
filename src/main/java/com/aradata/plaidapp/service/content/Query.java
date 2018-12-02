package com.aradata.plaidapp.service.content;

import org.springframework.data.annotation.Id;

public class Query {

	@Id
	private String id;

	private String query;

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

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
}
