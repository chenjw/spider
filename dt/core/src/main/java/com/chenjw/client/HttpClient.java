package com.chenjw.client;

import java.util.Map;

import com.chenjw.client.result.Result;
import com.chenjw.spider.location.HttpUrl;

public interface HttpClient {
	public Result get(HttpUrl url, String encoding, String cookie,
			Map<String, String> headers);

	public Result post(HttpUrl url, String encoding, String cookie,
			Map<String, String> headers);

	public Result multPartPost(HttpUrl url, String encoding, String cookie,
			Map<String, String> headers, String fileParamName, String fileName,
			byte[] fileBytes);

	public void init();
}
