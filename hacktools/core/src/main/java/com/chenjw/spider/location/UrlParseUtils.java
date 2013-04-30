package com.chenjw.spider.location;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

public class UrlParseUtils {
	public static HttpUrl parseUrl(String url) {
		HttpUrl httpUrl = new HttpUrl();
		String address = null;
		Map<String, String> result = new HashMap<String, String>();
		if (!StringUtils.isBlank(url)) {

			if (url.indexOf("?") != -1) {
				address = StringUtils.substringBefore(url, "?");
				String params = StringUtils.substringAfter(url, "?");
				for (String keyValue : params.split("&")) {
					result.put(StringUtils.substringBefore(keyValue, "="),
							StringUtils.substringAfter(keyValue, "="));
				}
			}
			else{
				address = url;
			}
		}
		httpUrl.setUrl(address);
		httpUrl.setQueryParam(result);
		return httpUrl;
	}


}
