package com.aradata.plaidapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LikeIsNotExistsException extends RuntimeException{

	private String contentId;
	private String ownerId;

	public LikeIsNotExistsException(String contentId, String ownerId) {
		super(String.format("Like at content %s doesn't exists", contentId));
		this.contentId = contentId;
		this.ownerId = ownerId;
	}
}
