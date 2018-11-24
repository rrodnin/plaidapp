package com.aradata.plaidapp.model.content;

import org.springframework.data.annotation.Id;

public class Image {

	@Id
	private String id;

	private String url;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
