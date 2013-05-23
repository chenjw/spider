package com.chenjw.client.impl;

import org.springframework.beans.factory.FactoryBean;

import com.chenjw.client.HttpClient;

public class HttpClientFactoryBean implements FactoryBean{

	private String className;
	
	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public Object getObject() throws Exception {
		// 初始化httpClient
		HttpClient httpClient = null;
		try {
			httpClient= (HttpClient) Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		httpClient.init();
		return httpClient;
	}

	@Override
	public Class<HttpClient> getObjectType() {
		return HttpClient.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
