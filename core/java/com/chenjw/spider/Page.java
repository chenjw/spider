package com.chenjw.spider;

import java.util.Map;

import com.chenjw.client.exception.ErrorCodeEnum;

public class Page {

	private boolean isSuccess;

	private ErrorCodeEnum errorCode;

	private Map<String, Object> context;

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public ErrorCodeEnum getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCodeEnum errorCode) {
		this.errorCode = errorCode;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}

}
