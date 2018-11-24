package com.aradata.plaidapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LikeAlreadyExistsException extends RuntimeException {

	private String contentId;
	private String ownerId;

	public LikeAlreadyExistsException(String contentId, String ownerId) {
		super(String.format("Like at content %s already exists", contentId));
		this.contentId = contentId;
		this.ownerId = ownerId;
	}

}
