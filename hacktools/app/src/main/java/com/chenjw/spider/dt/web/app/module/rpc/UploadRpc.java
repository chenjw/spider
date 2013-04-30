package com.chenjw.spider.dt.web.app.module.rpc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;


import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;

import com.alibaba.nonda.databind.annotation.RequestParam;
import com.chenjw.spider.dt.web.app.constants.DtConstants;
import com.chenjw.spider.hacktools.model.TokenModel;
import com.chenjw.spider.hacktools.service.WeiboService;


public class UploadRpc {

	@Autowired
	private WeiboService weiboService;
	

	@ResourceMapping
	public Map<String, Object> upload(@RequestParam(name = "pic") String pic,
			@RequestParam(name = "status") String status, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();

		pic = StringUtils.substringAfter(pic, ",");
		TokenModel userToken = (TokenModel) session
				.getAttribute(DtConstants.USER_SESSION_KEY);

		return result;
	}
}
