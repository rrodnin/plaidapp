package com.aradata.plaidapp.model.content.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PodcastUrl {

	@NotNull
	@NotBlank
	private String url;

	@NotNull
	private String[] topicId;

	public String[] getTopicId() {
		return topicId;
	}

	public void setTopicId(String[] topicId) {
		this.topicId = topicId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
