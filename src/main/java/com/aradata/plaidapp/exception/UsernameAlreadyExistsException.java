package com.aradata.plaidapp.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

	public UsernameAlreadyExistsException() {
		super("Username already taken");
	}
}
