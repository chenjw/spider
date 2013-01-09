package com.chenjw.spider.dt.web.app.module.screen;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.service.DeletedTweetService;

public class View {
	@Autowired
	private DeletedTweetService deletedTweetService;

	public void execute(Context context, Navigator navigator) {
		List<TweetModel> list = deletedTweetService.findDeletedTweets();

		context.put("detailList", list);
	}

}