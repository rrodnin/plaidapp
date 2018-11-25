package com.aradata.plaidapp.exception;

public class ContentAlreadyHasPodcastException extends RuntimeException {

	public ContentAlreadyHasPodcastException() {
		super("Content already has a podcast");
	}
}
