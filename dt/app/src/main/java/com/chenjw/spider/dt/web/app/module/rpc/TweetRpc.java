/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.spider.dt.web.app.module.rpc;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.service.template.TemplateException;
import com.alibaba.citrus.service.template.TemplateService;
import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.TurbineRunDataInternal;
import com.alibaba.citrus.turbine.support.ContextAdapter;
import com.alibaba.citrus.turbine.util.TurbineUtil;
import com.alibaba.citrus.webx.WebxException;
import com.alibaba.nonda.databind.annotation.RequestParam;
import com.chenjw.spider.dt.dao.DeletedTweetDAO;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.service.DeletedTweetReadService;
import com.chenjw.spider.dt.utils.Page;
import com.chenjw.spider.dt.utils.PagedResult;
import com.chenjw.spider.dt.web.app.constants.DtConstants;

public class TweetRpc {
	@Autowired
	private DeletedTweetReadService deletedTweetReadService;
	@Autowired
	private DeletedTweetDAO deletedTweetDAO;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private HttpServletRequest request;

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
		deletedTweetDAO.deleteByTidAndMemberUserId(tid, userToken.getUserId());
		result.put("success", true);
		return result;
	}

	@ResourceMapping
	public HashMap<String, Object> nextPage(
			@RequestParam(name = "maxSort") String maxSort, HttpSession session) {
		TurbineRunDataInternal runData = (TurbineRunDataInternal) TurbineUtil
				.getTurbineRunData(request);
		Context context = runData.getContext();
		HashMap<String, Object> r = new HashMap<String, Object>();
		TokenModel userToken = (TokenModel) session
				.getAttribute(DtConstants.USER_SESSION_KEY);
		Page page = new Page();
		page.setMaxSort(maxSort);
		page.setPageSize(DtConstants.DEFAULT_PAGE_SIZE);
		PagedResult<TweetModel> result = deletedTweetReadService
				.findDeletedTweetsByUserId(userToken.getUserId(), page);
		context.put("detailList", result.getList());
		context.put("moreNum", result.getMoreNum());
		if (result.getList().size() > 0) {
			context.put("minSort",
					result.getList().get(result.getList().size() - 1)
							.getDeleteSort());
		}
		String pageHtml = "";
		try {

			pageHtml = render("control/detailList.vm", context);
		} catch (Exception e) {
			throw new WebxException(e);
		}
		r.put("page", pageHtml);
		r.put("success", true);
		return r;
	}

	private String render(String target, Context context) {
		StringWriter sw = new StringWriter();
		try {
			templateService.writeTo(target, new ContextAdapter(context), sw);
		} catch (TemplateException e) {
			throw new WebxException(e);
		} catch (IOException e) {
			throw new WebxException(e);
		}
		return sw.toString();
	}

	@ResourceMapping
	public HashMap<String, Object> firstPage(HttpSession session) {
		TurbineRunDataInternal runData = (TurbineRunDataInternal) TurbineUtil
				.getTurbineRunData(request);
		Context context = runData.getContext();
		HashMap<String, Object> r = new HashMap<String, Object>();
		TokenModel userToken = (TokenModel) session
				.getAttribute(DtConstants.USER_SESSION_KEY);
		Page page = new Page();
		page.setPageSize(DtConstants.DEFAULT_PAGE_SIZE);
		PagedResult<TweetModel> result = deletedTweetReadService
				.findDeletedTweetsByUserId(userToken.getUserId(), page);

		context.put("detailList", result.getList());
		context.put("moreNum", result.getMoreNum());
		String maxSort = "0";
		if (result.getList().size() > 0) {
			context.put("minSort",
					result.getList().get(result.getList().size() - 1)
							.getDeleteSort());
			maxSort = result.getList().get(0).getDeleteSort();
		}

		String pageHtml = "";
		try {
			pageHtml = render("control/detailList.vm", context);
		} catch (Exception e) {
			throw new WebxException(e);
		}
		r.put("page", pageHtml);
		r.put("maxSort", maxSort);
		r.put("success", true);
		return r;

	}

}
