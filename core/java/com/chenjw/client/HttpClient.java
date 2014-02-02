package com.chenjw.client;

import java.util.Map;

import com.chenjw.client.exception.HttpClientException;

public interface HttpClient {
    /**
     * 
     * 根据url获得内容，不会执行js和加载关联资源
     * 
     * @param sessionId
     * @param url
     * @param params
     * @param encoding
     * @return
     * @throws HttpClientException
     */
    public String get(String sessionId, String url, Map<String, String> params, String encoding) throws HttpClientException;
    
	/**
	 * 
	 * 根据url获得内容，同时会执行页面js并加载关联资源
	 * 
	 * @param sessionId
	 * @param url
	 * @param params
	 * @param encoding
	 * @return
	 * @throws HttpClientException
	 */
	public String getInPage(String sessionId, String url, Map<String, String> params, String encoding) throws HttpClientException;

	/**
	 * 
	 * 根据url获得内容，同时会执行页面js并加载关联资源
	 * 
	 * @param sessionId
	 * @param url
	 * @param params
	 * @param encoding
	 * @return
	 * @throws HttpClientException
	 */
	public String postInPage(String sessionId, String url, Map<String, String> params, String encoding) throws HttpClientException;
	
	/**
	 * 
	 * 根据url获得内容，不会执行js和加载关联资源
	 * 
	 * @param sessionId
	 * @param url
	 * @param params
	 * @param encoding
	 * @return
	 * @throws HttpClientException
	 */
	public String post(String sessionId, String url, Map<String, String> params, String encoding) throws HttpClientException;
}
