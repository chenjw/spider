package com.chenjw.spider.dt.web.app.module.screen;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.chenjw.spider.hacktools.service.AccountSearchService;

public class Stop {
	@Autowired
	private AccountSearchService deletedTweetService;

	public void execute(Context context, Navigator navigator) {

	}

}
