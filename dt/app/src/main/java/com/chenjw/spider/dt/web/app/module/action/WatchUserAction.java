package com.chenjw.spider.dt.web.app.module.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.chenjw.spider.dt.service.DeletedTweetCheckService;

public class WatchUserAction {

	@Autowired
	private DeletedTweetCheckService deletedTweetService;

	public void doWatchFriends(@Param(name = "userName") String userName,
			Context context, Navigator navigator) {
		// String userId = deletedTweetService.findUserIdByName("陈俊文V");
		// deletedTweetService.addWatchedUser(null);
		context.put("message", "未实现！");
		navigator.forwardTo("message.vm");
	}

}
