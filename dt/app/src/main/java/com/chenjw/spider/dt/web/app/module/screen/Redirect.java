package com.chenjw.spider.dt.web.app.module.screen;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import weibo4j.util.WeiboConfig;

import com.alibaba.citrus.service.requestcontext.parser.ParameterParser;
import com.alibaba.citrus.service.requestcontext.rundata.RunData;
import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.chenjw.spider.location.HttpUrl;
import com.chenjw.spider.location.UrlParseUtils;

//3221292113
/**
 * 
 * @author chenjw
 * 
 */
public class Redirect {

	public void execute(@Param(name = "url") String url, Context context,
			Navigator navigator, RunData runData) {
		ParameterParser param = runData.getParameters();
		HttpUrl httpUrl=UrlParseUtils.parseUrl(url);
		for (String key : param.keySet()) {
			if (!"url".equals(key)) {
				httpUrl.getQueryParam().put(key, param.getString(key));
			}
		}
		navigator.redirectToLocation(httpUrl.toUrlString());
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(URLEncoder.encode(
				"http://127.0.0.1:8080/regOauth.htm", "UTF-8"));
		// http://deleted-tweets.cloudfoundry.com/redirect.htm?url=http%3A%2F%2F127.0.0.1%3A8080%2FregOauth.htm
	}
}