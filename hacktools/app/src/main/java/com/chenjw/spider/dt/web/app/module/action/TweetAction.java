package com.chenjw.spider.dt.web.app.module.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.chenjw.spider.hacktools.service.AccountSearchService;

public class TweetAction {

	@Autowired
	private AccountSearchService deletedTweetService;

	public void doDelete(@Param(name = "tid") String tid, Context context,
			Navigator navigator) {
		// String userId = deletedTweetService.findUserIdByName("陈俊文V");
		// deletedTweetService.addWatchedUser(null);
		context.put("message", "未实现！");
		navigator.forwardTo("message.vm");
	}

}
