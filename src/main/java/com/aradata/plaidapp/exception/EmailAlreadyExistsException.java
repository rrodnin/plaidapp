package com.aradata.plaidapp.exception;

public class EmailAlreadyExistsException extends RuntimeException {

	public EmailAlreadyExistsException() {
		super("Email already taken");
	}
}
