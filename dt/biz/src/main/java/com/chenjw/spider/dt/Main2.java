package com.chenjw.spider.dt;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.service.WeiboService;

public class Main2 {
	public static void main(String[] args) throws UnsupportedEncodingException {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath*:context.xml");
		WeiboService weiboService = (WeiboService) context
				.getBean("weiboService");
		// 1783831337
		// 1925238912
		String userId = "1925238912";//
		String token = "2.00WaGSGCnpP1DBdf51a94343llFD7C";
		long sinceId = 100000;
		int success = 0;
		long start = System.currentTimeMillis();
		TokenModel user = new TokenModel();
		user.setUserId(userId);
		user.setToken(token);
		while (true) {
			List<TweetModel> list = weiboService.findFriendsTimeline(user,
					sinceId);
			if (list != null) {
				for (TweetModel t : list) {
					System.out.println(t.getId() + " " + t.getText());
				}
				success++;
			}
			System.out.println(success + " "
					+ (System.currentTimeMillis() - start) / 1000 + " s");

		}

		// System.out.println("finished!");
	}
}
