package com.aradata.plaidapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ContentIsNotPodcastException extends RuntimeException {

	public ContentIsNotPodcastException() {
		super("This content is not podcast");
	}
}
