package com.chenjw.client;

import java.util.Map;

import com.chenjw.client.exception.HttpClientException;

public interface TheHttpClient {
	public String get(String sessionId, String url, Map<String, String> params, String encoding) throws HttpClientException;

	public String post(String sessionId, String url, Map<String, String> params, String encoding) throws HttpClientException;
}
