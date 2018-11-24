package com.aradata.plaidapp.model.content.request;

import com.aradata.plaidapp.model.content.Type;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ContentRequest {

	@NotNull
	@NotBlank
	protected String title;

	@NotNull
	protected Type type;

	@NotNull
	@NotBlank
	protected String url;

	@NotNull
	@NotBlank
	protected String description;

	@NotNull
	@NotBlank
	protected String text;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
