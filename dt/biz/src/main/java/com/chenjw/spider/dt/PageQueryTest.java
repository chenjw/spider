package com.chenjw.spider.dt;

import java.io.UnsupportedEncodingException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.service.DeletedTweetReadService;
import com.chenjw.spider.dt.utils.Page;
import com.chenjw.spider.dt.utils.PagedResult;

public class PageQueryTest {
	public static void main(String[] args) throws UnsupportedEncodingException {
		// 1783831337
		// 1925238912
		String userId = "1925238912";//
		String token = "2.00WaGSGCnpP1DBdf51a94343llFD7C";
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath*:context.xml");
		DeletedTweetReadService deletedTweetReadService = (DeletedTweetReadService) context
				.getBean("deletedTweetReadService");
		Page page = new Page();
		page.setPageSize(10);
		PagedResult<TweetModel> r = deletedTweetReadService
				.findDeletedTweetsByUserId(userId, page);
		System.out.println(r.getMoreNum());

	}
}
