package com.chenjw.client.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;

import com.baidu.bae.api.factory.BaeFactory;
import com.baidu.bae.api.fetchurl.BaeFetchurl;
import com.baidu.bae.api.fetchurl.BasicNameValuePair;
import com.baidu.bae.api.fetchurl.BinaryNameValuePair;
import com.baidu.bae.api.fetchurl.NameValuePair;
import com.chenjw.client.HttpClient;
import com.chenjw.client.result.Result;
import com.chenjw.spider.location.HttpUrl;

public class BaiduHttpClient implements HttpClient {

	@Override
	public Result get(HttpUrl url, String encoding, String cookie,
			Map<String, String> headers) {
		Result result = new Result();
		BaeFetchurl fetch = BaeFactory.getBaeFetchurl();
		// 设置自定义头部
		if (headers != null) {
			for (Entry<String, String> entry : headers.entrySet()) {
				fetch.setHeader(entry.getKey(), entry.getValue());
			}
		}
		// 打开允许重定向开关,默认
		fetch.setAllowRedirect(true);
		// 设置cookie
		Map<String, String> cookieMap = parseCookie(cookie);
		if (cookieMap != null) {
			fetch.setCookies(cookieMap);
		}
		// 发起一次get请求
		String content = fetch.get(url.toUrlString());
		result.setResultEncoding(encoding);
		result.setResultString(content);
		// 获取http code
		int httpcode = fetch.getHttpCode();
		result.setResponseCode(httpcode);
		if (httpcode == HttpStatus.SC_OK) {
			result.setSuccess(true);
		} else {
			result.setSuccess(false);
			result.setMessage(fetch.getErrmsg());
		}
		return result;
	}

	@Override
	public Result post(HttpUrl url, String encoding, String cookie,
			Map<String, String> headers) {
		Result result = new Result();
		BaeFetchurl fetch = BaeFactory.getBaeFetchurl();
		// 设置自定义头部
		if (headers != null) {
			for (Entry<String, String> entry : headers.entrySet()) {
				fetch.setHeader(entry.getKey(), entry.getValue());
			}
		}
		// 打开允许重定向开关,默认
		fetch.setAllowRedirect(true);
		// 设置cookie
		Map<String, String> cookieMap = parseCookie(cookie);
		if (cookieMap != null) {
			fetch.setCookies(cookieMap);
		}
		// post参数
		List<NameValuePair> pairs=new ArrayList<NameValuePair>();
		if(url.getQueryParam()!=null){
			for(Entry<String,String> entry:url.getQueryParam().entrySet()){
				pairs.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
			}
		}
		fetch.setPostData(pairs);
		// 发起一次get请求
		String content = fetch.post(url.getRenderedUrl());
		result.setResultEncoding(encoding);
		result.setResultString(content);
		// 获取http code
		int httpcode = fetch.getHttpCode();
		result.setResponseCode(httpcode);
		if (httpcode == HttpStatus.SC_OK) {
			result.setSuccess(true);
		} else {
			result.setSuccess(false);
			result.setMessage(fetch.getErrmsg());
		}
		return result;
	}

	@Override
	public Result multPartPost(HttpUrl url, String encoding, String cookie,
			Map<String, String> headers, String fileParamName, String fileName,
			byte[] fileBytes) {
		Result result = new Result();
		BaeFetchurl fetch = BaeFactory.getBaeFetchurl();
		// 设置自定义头部
		if (headers != null) {
			for (Entry<String, String> entry : headers.entrySet()) {
				fetch.setHeader(entry.getKey(), entry.getValue());
			}
		}
		// 打开允许重定向开关,默认
		fetch.setAllowRedirect(true);
		// 设置cookie
		Map<String, String> cookieMap = parseCookie(cookie);
		if (cookieMap != null) {
			fetch.setCookies(cookieMap);
		}
		// post参数
		List<NameValuePair> pairs=new ArrayList<NameValuePair>();
		if(url.getQueryParam()!=null){
			for(Entry<String,String> entry:url.getQueryParam().entrySet()){
				pairs.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
			}
		}
		try {
			pairs.add(new BinaryNameValuePair(fileParamName,fileName,fileBytes));
		} catch (IOException e) {
			e.printStackTrace();
		}
		fetch.setPostData(pairs);
		// 发起一次get请求
		String content = fetch.post(url.getRenderedUrl());
		result.setResultEncoding(encoding);
		result.setResultString(content);
		// 获取http code
		int httpcode = fetch.getHttpCode();
		result.setResponseCode(httpcode);
		if (httpcode == HttpStatus.SC_OK) {
			result.setSuccess(true);
		} else {
			result.setSuccess(false);
			result.setMessage(fetch.getErrmsg());
		}
		return result;
	}

	@Override
	public void init() {
		// do nothing

	}

	private static Map<String, String> parseCookie(String cookieStr) {
		if (cookieStr == null) {
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		String[] pairs = cookieStr.split(";");
		for (String pair : pairs) {
			String key = StringUtils.substringBefore(pair, "=").trim();
			String value = StringUtils.substringAfter(pair, "=").trim();
			result.put(key, value);
		}
		return result;
	}
}
