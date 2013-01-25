package com.chenjw.spider.dt.web.app.module.screen;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.service.DeletedTweetReadService;
import com.chenjw.spider.dt.utils.Page;
import com.chenjw.spider.dt.web.app.constants.DtConstants;

/**
 * 查找最新的微博
 * 
 * @author chenjw
 * 
 */
public class CountNew {
	@Autowired
	private DeletedTweetReadService deletedTweetReadService;

	public void execute(@Param(name = "minSort") String minSort,
			Context context, Navigator navigator, HttpSession session) {

		TokenModel userToken = (TokenModel) session
				.getAttribute(DtConstants.USER_SESSION_KEY);
		Page page = new Page();
		page.setMinSort(minSort);
		int count = deletedTweetReadService.countDeletedTweetsByUserId(
				userToken.getUserId(), page);
		context.put("count", count);
		return;
	}

}