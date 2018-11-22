package com.aradata.plaidapp.model.content;

import org.springframework.data.annotation.Id;

public class Like {

	@Id
	private String id;

	private String contentId;
	private Type contentType;
	private String ownerId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public Type getContentType() {
		return contentType;
	}

	public void setContentType(Type contentType) {
		this.contentType = contentType;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
}
