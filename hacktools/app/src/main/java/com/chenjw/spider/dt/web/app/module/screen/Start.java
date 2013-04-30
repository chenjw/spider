package com.chenjw.spider.dt.web.app.module.screen;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.chenjw.spider.hacktools.service.AccountSearchService;

public class Start {
	@Autowired
	private AccountSearchService deletedTweetService;

	public void execute(Context context, Navigator navigator) {
		deletedTweetService.start();
		context.put("message", "已启动抓取任务！");
		navigator.forwardTo("message.vm");
	}

}
