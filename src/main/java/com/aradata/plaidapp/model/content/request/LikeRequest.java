package com.aradata.plaidapp.model.content.request;

import com.aradata.plaidapp.model.content.Type;

import javax.validation.constraints.NotNull;

public class LikeRequest {

	@NotNull
	private String contentid;

	@NotNull
	private Type type;

	public String getContentid() {
		return contentid;
	}

	public void setContentid(String contentid) {
		this.contentid = contentid;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
