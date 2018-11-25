package com.aradata.plaidapp.model.content.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AddContentToTopicRequest {

	@NotNull
	@NotBlank
	private String contentId;

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
}
