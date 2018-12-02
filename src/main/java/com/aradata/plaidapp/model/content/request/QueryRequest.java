package com.aradata.plaidapp.model.content.request;

public class QueryRequest {

	private String query;
	private String[] topics;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String[] getTopics() {
		return topics;
	}

	public void setTopics(String[] topics) {
		this.topics = topics;
	}
}
