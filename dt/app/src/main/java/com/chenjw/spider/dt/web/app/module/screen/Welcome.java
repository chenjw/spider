package com.chenjw.spider.dt.web.app.module.screen;

import javax.servlet.http.HttpSession;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;

//3221292113
/**
 * 演示页面
 * 
 * @author chenjw
 * 
 */
public class Welcome {

	public void execute(Context context, Navigator navigator,
			HttpSession session) {
		System.out.println("welcome");
		return;

	}

}