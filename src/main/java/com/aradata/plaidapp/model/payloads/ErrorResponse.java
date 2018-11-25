package com.aradata.plaidapp.model.payloads;

public class ErrorResponse {

	private ErrorObject errorObject;

	public ErrorResponse(ErrorObject errorObject) {
		this.errorObject = errorObject;
	}

	public ErrorObject getErrorObject() {
		return errorObject;
	}

	public void setErrorObject(ErrorObject errorObject) {
		this.errorObject = errorObject;
	}
}
