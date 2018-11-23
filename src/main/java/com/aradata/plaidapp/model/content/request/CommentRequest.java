package com.aradata.plaidapp.model.content.request;

import com.aradata.plaidapp.model.content.Type;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CommentRequest {

	@NotNull
	@NotBlank
	private String text;

	@NotNull
	private String contentId;

	@NotNull
	private Type contentType;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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
}
