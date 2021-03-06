package com.chenjw.spider.pagefetcher;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.chenjw.client.HttpClient;
import com.chenjw.client.exception.ErrorCodeEnum;
import com.chenjw.client.result.Result;
import com.chenjw.parser.HtmlParser;
import com.chenjw.parser.impl.SimpleHtmlParser;
import com.chenjw.spider.Page;
import com.chenjw.spider.PageFetcher;
import com.chenjw.spider.config.PageConfig;
import com.chenjw.spider.config.SpiderConfig;
import com.chenjw.spider.location.HttpLocation;

public class HttpPageFetcher implements PageFetcher {
	private Map<String, HtmlParser> htmlParserMap = new HashMap<String, HtmlParser>();
	private HttpClient httpClient;
	private SpiderConfig spiderConfig;

	public void init() {
		for (Entry<String, PageConfig> entry : spiderConfig.getPages()
				.entrySet()) {
			String pageId = entry.getKey();
			PageConfig pageConfig = entry.getValue();
			SimpleHtmlParser htmlParser = new SimpleHtmlParser();
			String[] htmls = pageConfig.getTrainHtmls();
			for (String html : htmls) {
				htmlParser.train(html);
			}
			htmlParserMap.put(pageId, htmlParser);
		}
	}

	@Override
	public Page fetch(HttpLocation loc) {
		Page r = new Page();
		String html = null;

		Result result = httpClient.get(loc, loc.getEncoding(), null, null);
		if (!result.isSuccess()) {
			r.setSuccess(false);
			r.setErrorCode(result.getErrorCode());
			return r;
		}
		html = result.getResultString();
		if (html == null) {
			r.setSuccess(false);
			r.setErrorCode(ErrorCodeEnum.SYSTEM_ERROR);
			return r;
		}
		HtmlParser htmlParser = htmlParserMap.get(loc.getPageId());

		Map<String, String> context = htmlParser.read(html);
		r.setContext(context);
		r.setSuccess(true);
		return r;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public void setSpiderConfig(SpiderConfig spiderConfig) {
		this.spiderConfig = spiderConfig;
	}

}
