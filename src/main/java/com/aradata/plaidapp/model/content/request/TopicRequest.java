package com.aradata.plaidapp.model.content.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TopicRequest {

	@NotBlank
	@NotNull
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
