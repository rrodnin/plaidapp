package com.aradata.plaidapp.model.payloads;

public class ErrorResponse {

	private ErrorObject error;

	public ErrorResponse(ErrorObject errorObject) {
		this.error = errorObject;
	}

	public ErrorObject getErrorObject() {
		return error;
	}

	public void setErrorObject(ErrorObject errorObject) {
		this.error = errorObject;
	}
}
