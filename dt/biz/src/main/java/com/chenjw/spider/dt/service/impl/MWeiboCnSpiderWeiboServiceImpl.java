package com.chenjw.spider.dt.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.chenjw.client.HttpClient;
import com.chenjw.client.result.Result;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.location.HttpUrl;
import com.chenjw.spider.location.UrlParseUtils;

public class MWeiboCnSpiderWeiboServiceImpl extends OpenWeiboServiceImpl {
	private HttpClient httpClient;

	// 个人时间轴
	String url2 = "http://m.weibo.cn/home/homeData?hideAvanta=1&u=1007343817&page=1&&_=1357915804962";
	String cookie = "USER_LAST_LOGIN_NAME=cjw1983%40gmail.com; gsid_CTandWM=4ug5CpOz1GpTrZEsPBcbt84QeeI; login=true";
	String url1 = "http://weibo.cn/pub/topmblog";

	@Override
	public List<TweetModel> findTopTimeline() {

		int page = 1;
		HttpUrl url = UrlParseUtils.parseUrl(url1);
		url.getQueryParam().put("page", String.valueOf(page));
		Result result = httpClient.get(url, "UTF-8", null, null);
		if (!result.isSuccess()) {
			return null;
		}
		String str = result.getResultString();
		System.out.println(str);
		return new ArrayList<TweetModel>();

	}

	@Override
	public List<TweetModel> findUserTimelineByUserId(String userId,
			String token, long sinceId) {

		int page = 1;
		HttpUrl url = UrlParseUtils.parseUrl(url2);
		url.getQueryParam().put("u", userId);
		url.getQueryParam().put("page", String.valueOf(page));
		Result result = httpClient.get(url, "UTF-8", null, null);
		if (!result.isSuccess()) {
			return null;
		}
		String str = result.getResultString();
		System.out.println(str);
		return new ArrayList<TweetModel>();

	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

}
