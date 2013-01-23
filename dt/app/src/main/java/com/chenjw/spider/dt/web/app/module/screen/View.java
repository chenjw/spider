package com.chenjw.spider.dt.web.app.module.screen;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.service.DeletedTweetReadService;
import com.chenjw.spider.dt.service.UserService;
import com.chenjw.spider.dt.service.WeiboService;
import com.chenjw.spider.dt.utils.Page;
import com.chenjw.spider.dt.utils.PagedResult;
import com.chenjw.spider.dt.web.app.constants.DtConstants;

//3221292113
public class View {
	@Autowired
	private DeletedTweetReadService deletedTweetReadService;

	@Autowired
	private WeiboService weiboService;
	@Autowired
	private UserService userService;

	private void forceLogin(String force, HttpSession session) {
		if (!StringUtils.isBlank(force)) {
			TokenModel userToken = (TokenModel) session
					.getAttribute(DtConstants.USER_SESSION_KEY);
			userToken = userService.findWatchedUserById(force);
			session.setAttribute(DtConstants.USER_SESSION_KEY, userToken);
		}
	}

	public void execute(@Param(name = "code") String code,
			@Param(name = "force") String force,
			@Param(name = "maxSort") String maxSort, Context context,
			Navigator navigator, HttpSession session) {
		forceLogin(force, session);
		TokenModel userToken = (TokenModel) session
				.getAttribute(DtConstants.USER_SESSION_KEY);
		if (userToken != null) {
			checkAndView(userToken, maxSort, context, navigator, session);
			return;
		}
		if (StringUtils.isBlank(code)) {
			oauth(navigator);
			return;
		}
		String token = weiboService.findAccessTokenByCode(code);
		// token不存在表示code无效
		if (StringUtils.isBlank(token)) {
			errorPage(context, navigator,
					"当前环境为测试环境，您的帐号未开通测试权限，请联系管理员把您的帐号添加到测试帐号列表。");
			return;
		}
		TokenModel model = userService.findWatchedUserByToken(token);
		// 未开通权限
		if (model == null) {
			// 添加一个未开通权限的用户
			model = userService.addInvalidUser(token);
		}

		// 已开通权限
		session.setAttribute(DtConstants.USER_SESSION_KEY, model);
		session.setAttribute(DtConstants.LOGIN_USER_SESSION_KEY, model);
		navigator.redirectToLocation("/");
		return;

	}

	private void oauth(Navigator navigator) {
		// 重定向到登录页面
		String redirectUrl = weiboService.findAuthorizeUrl();
		navigator.redirectToLocation(redirectUrl);
	}

	private void errorPage(Context context, Navigator navigator,
			String errorMessage) {
		context.put("errorMessage", errorMessage);
		navigator.forwardTo("error.vm");
	}

	private void checkAndView(TokenModel model, String maxSort,
			Context context, Navigator navigator, HttpSession session) {
		if (!model.isValid()) {
			errorPage(context, navigator,
					model.getScreenName() + " (" + model.getUserId() + "/"
							+ model.getToken() + ") 未开通权限");
			return;
		} else {
			Page page = new Page();
			page.setPageSize(DtConstants.DEFAULT_PAGE_SIZE);
			page.setMaxSort(maxSort);
			PagedResult<TweetModel> result = deletedTweetReadService
					.findDeletedTweetsByUserId(model.getUserId(), page);
			context.put("detailList", result.getList());
			context.put("moreNum", result.getMoreNum());
			if (result.getList().size() > 0) {
				context.put("minSort",
						result.getList().get(result.getList().size() - 1)
								.getDeleteSort());
			}
			context.put(DtConstants.USER_SESSION_KEY,
					session.getAttribute(DtConstants.USER_SESSION_KEY));
			context.put(DtConstants.LOGIN_USER_SESSION_KEY,
					session.getAttribute(DtConstants.LOGIN_USER_SESSION_KEY));
			return;
		}
	}
}