/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.spider.dt.web.app.module.rpc;

import org.apache.xerces.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.nonda.databind.annotation.RequestParam;
import com.chenjw.client.HttpClient;
import com.chenjw.client.result.Result;
import com.chenjw.spider.dt.web.app.module.Base;
import com.chenjw.spider.location.HttpUrl;

public class ImgRpc extends Base {

	@Autowired
	private HttpClient httpClient;

	@ResourceMapping
	public String proxy(@RequestParam(name = "url") String url) {
		byte[] bytes = null;

		Result result = httpClient
				.get(new HttpUrl(url, null), null, null, null);
		bytes = result.getResultBytes();
		String s = "data:image/jpeg;base64,";
		return s + Base64.encode(bytes);
	}
}
