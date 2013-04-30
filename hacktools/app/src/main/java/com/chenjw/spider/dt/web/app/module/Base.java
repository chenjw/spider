/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.spider.dt.web.app.module;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.TurbineRunDataInternal;
import com.alibaba.citrus.turbine.util.TurbineUtil;
import com.chenjw.spider.dt.web.app.constants.DtConstants;
import com.chenjw.spider.hacktools.constants.SearchTypeEnum;
import com.chenjw.spider.hacktools.model.SearchInfo;
import com.chenjw.spider.hacktools.model.TokenModel;
import com.chenjw.spider.hacktools.service.UserService;
import com.chenjw.spider.hacktools.utils.Page;

public class Base {

	@Autowired
	protected HttpServletRequest request;
	@Autowired
	private UserService userService;

	protected SearchInfo findSearchInfo(HttpSession session) {

		TurbineRunDataInternal runData = (TurbineRunDataInternal) TurbineUtil
				.getTurbineRunData(request);
		SearchInfo searchInfo = new SearchInfo();
		searchInfo.setUseFollower(runData.getParameters().getBoolean(
				"useFollower", true));
		searchInfo.setDemo(runData.getParameters().getBoolean("demo", false));
		if (searchInfo.isUseFollower()) {
			if (searchInfo.isDemo()) {
				TokenModel userToken = userService
						.findWatchedUserById("3221292113");
				searchInfo.setFollowerUserId(userToken.getUserId());
				searchInfo.setFollowerUserName("公共帐号");
			} else {
				TokenModel userToken = (TokenModel) session
						.getAttribute(DtConstants.USER_SESSION_KEY);
				searchInfo.setFollowerUserId(userToken.getUserId());
				searchInfo.setFollowerUserName(userToken.getScreenName());
			}

		}
		searchInfo.setSenderUserId(runData.getParameters().getString(
				"senderUserId"));
		searchInfo.setSenderUserName(runData.getParameters().getString(
				"senderUserName"));
		searchInfo.setType(SearchTypeEnum.parse(runData.getParameters()
				.getString("type")));

		Page page = new Page();
		page.setPageSize(DtConstants.DEFAULT_PAGE_SIZE);
		page.setMaxSort(runData.getParameters().getString("maxSort"));
		page.setMinSort(runData.getParameters().getString("minSort"));

		searchInfo.setPage(page);

		return searchInfo;
	}
}
