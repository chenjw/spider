package com.chenjw.spider.dt.web.app.module.screen;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.model.UserTokenModel;
import com.chenjw.spider.dt.service.DeletedTweetReadService;
import com.chenjw.spider.dt.service.UserService;
import com.chenjw.spider.dt.service.WeiboService;
import com.chenjw.spider.dt.utils.Page;
import com.chenjw.spider.dt.utils.PagedResult;
import com.chenjw.spider.dt.web.app.constants.DtConstants;

public class View {
	@Autowired
	private DeletedTweetReadService deletedTweetReadService;

	@Autowired
	private WeiboService weiboService;
	@Autowired
	private UserService userService;

	private void forceLogin(String force, HttpSession session) {
		if (StringUtils.isBlank(force)) {
			UserTokenModel userToken = (UserTokenModel) session
					.getAttribute(DtConstants.USER_SESSION_KEY);
			if (userToken == null) {
				userToken = userService.findWatchedUserById(force);
				session.setAttribute(DtConstants.USER_SESSION_KEY, userToken);
			}
		}
	}

	public void execute(@Param(name = "code") String code,
			@Param(name = "force") String force,
			@Param(name = "maxId") Long maxId, Context context,
			Navigator navigator, HttpSession session) {
		forceLogin(force, session);
		UserTokenModel userToken = (UserTokenModel) session
				.getAttribute(DtConstants.USER_SESSION_KEY);
		if (userToken != null) {
			checkAndView(userToken, maxId, context);
			return;
		}
		if (!StringUtils.isBlank(code)) {
			String token = weiboService.findAccessTokenByCode(code);
			if (!StringUtils.isBlank(token)) {
				UserTokenModel model = userService
						.findWatchedUserByToken(token);
				// 未开通权限
				if (model == null) {
					// 添加一个未开通权限的用户
					model = userService.addInvalidUser(token);
				}
				// 已开通权限
				session.setAttribute(DtConstants.USER_SESSION_KEY, model);
				checkAndView(model, maxId, context);
				return;
			}
		}

		// 重定向到登录页面
		String redirectUrl = weiboService.findAuthorizeUrl();
		navigator.redirectToLocation(redirectUrl);
		return;
	}

	private void checkAndView(UserTokenModel model, Long maxId, Context context) {
		if (!model.isValid()) {
			context.put("errorMessage",
					model.getScreenName() + " (" + model.getUserId() + "/"
							+ model.getToken() + ") 未开通权限");
		} else {
			Page page = new Page();
			page.setPageSize(DtConstants.DEFAULT_PAGE_SIZE);
			page.setMaxId(maxId);
			PagedResult<TweetModel> result = deletedTweetReadService
					.findDeletedTweetsByUserId(model.getUserId(), page);
			context.put("detailList", result.getList());
			context.put("moreNum", result.getMoreNum());
			if (result.getList().size() > 0) {
				context.put("minId",
						result.getList().get(result.getList().size() - 1)
								.getId());
			}

		}
	}
}