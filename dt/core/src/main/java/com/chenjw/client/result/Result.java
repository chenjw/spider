package com.chenjw.client.result;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import com.chenjw.client.exception.ErrorCodeEnum;

public class Result implements Serializable {

	private static final long serialVersionUID = -2888683855996568070L;
	private boolean success;
	private int responseCode;
	private int methodStatusCode;
	private ErrorCodeEnum errorCode;
	private String message;
	private Throwable cause;
	private String resultEncoding;
	private String resultString;
	private byte[] resultBytes;

	public int getMethodStatusCode() {
		return methodStatusCode;
	}

	public void setMethodStatusCode(int methodStatusCode) {
		this.methodStatusCode = methodStatusCode;
	}

	public String getResultEncoding() {
		return resultEncoding;
	}

	public void setResultEncoding(String resultEncoding) {
		this.resultEncoding = resultEncoding;
	}

	public String getResultString() {
		if (resultString != null) {
			return resultString;
		}
		if (resultBytes != null) {
			if (resultEncoding != null) {
				try {
					resultString = new String(resultBytes, resultEncoding);
				} catch (UnsupportedEncodingException e) {
				}
			} else {
				resultString = new String(resultBytes);
			}
		}
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public byte[] getResultBytes() {
		if (resultBytes != null) {
			return resultBytes;
		}
		if (resultString != null) {
			if (resultEncoding != null) {
				try {
					resultBytes = resultString.getBytes(resultEncoding);
				} catch (UnsupportedEncodingException e) {
				}
			} else {
				resultBytes = resultString.getBytes();
			}
		}
		return resultBytes;
	}

	public void setResultBytes(byte[] resultBytes) {
		this.resultBytes = resultBytes;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public ErrorCodeEnum getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCodeEnum errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Throwable getCause() {
		return cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}

}
