package com.chenjw.client.exception;

public class HttpClientException extends Exception {

	private static final long serialVersionUID = 7409744286674919442L;
	private int responseCode;
	private ErrorCodeEnum errorCode;

	public HttpClientException(ErrorCodeEnum errorCode, String message,
			Throwable cause, int responseCode) {
		super(message, cause);
		this.errorCode = errorCode;
		this.responseCode = responseCode;
	}

	public HttpClientException(ErrorCodeEnum errorCode, String message,
			Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public ErrorCodeEnum getErrorCode() {
		return errorCode;
	}

	public int getResponseCode() {
		return responseCode;
	}

}
