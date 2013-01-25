package com.chenjw.spider.dt.web.app.module.screen;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.service.DeletedTweetReadService;
import com.chenjw.spider.dt.utils.Page;
import com.chenjw.spider.dt.utils.PagedResult;
import com.chenjw.spider.dt.web.app.constants.DtConstants;

//3221292113
public class View {
	@Autowired
	private DeletedTweetReadService deletedTweetReadService;

	public void execute(@Param(name = "maxSort") String maxSort,
			Context context, Navigator navigator, HttpSession session) {

		TokenModel userToken = (TokenModel) session
				.getAttribute(DtConstants.USER_SESSION_KEY);
		checkAndView(userToken, maxSort, context, navigator, session);
		return;

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

	private void errorPage(Context context, Navigator navigator,
			String errorMessage) {
		context.put("errorMessage", errorMessage);
		navigator.forwardTo("error.vm");
	}
}