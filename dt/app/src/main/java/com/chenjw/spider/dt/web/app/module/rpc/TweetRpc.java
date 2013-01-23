/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.spider.dt.web.app.module.rpc;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.nonda.databind.annotation.RequestParam;
import com.chenjw.spider.dt.dao.DeletedTweetDAO;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.web.app.constants.DtConstants;

public class TweetRpc {
	@Autowired
	private DeletedTweetDAO deletedTweetDAO;

	@ResourceMapping
	public HashMap<String, Object> deleteTweet(
			@RequestParam(name = "tid") String tid, HttpSession session) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isBlank(tid)) {
			result.put("errorCode", "tid is null");
			result.put("success", false);
			return result;
		}
		TokenModel userToken = (TokenModel) session
				.getAttribute(DtConstants.USER_SESSION_KEY);
		if (userToken == null) {
			result.put("errorCode", "not login");
			result.put("success", false);
			return result;
		}
		deletedTweetDAO.deleteByTidAndMemberUserId(tid, userToken.getUserId());
		result.put("success", true);
		return result;
	}
}
