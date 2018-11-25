package com.aradata.plaidapp.model.payloads;

public class ApiResponse<T> {
	private Boolean success;
	private String status;
	private String message;
	private int code;

	private T data;

	public ApiResponse(Boolean success, String message, int code) {
		this.success = success;
		this.message = message;
		if (success)
				status = "OK";
		this.code = code;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}