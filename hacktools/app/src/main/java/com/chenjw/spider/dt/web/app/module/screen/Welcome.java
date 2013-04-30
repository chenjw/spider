package com.chenjw.spider.dt.web.app.module.screen;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.chenjw.spider.dt.web.app.constants.DtConstants;
import com.chenjw.spider.hacktools.model.TokenModel;
import com.chenjw.spider.hacktools.service.UserService;

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