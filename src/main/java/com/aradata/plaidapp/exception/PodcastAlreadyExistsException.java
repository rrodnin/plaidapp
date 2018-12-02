package com.aradata.plaidapp.exception;

public class PodcastAlreadyExistsException extends RuntimeException {

	public PodcastAlreadyExistsException() {
		super("Podcast feed already exists");
	}
}
