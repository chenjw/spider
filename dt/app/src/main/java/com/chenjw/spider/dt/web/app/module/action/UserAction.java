package com.chenjw.spider.dt.web.app.module.action;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.service.DeletedTweetCheckService;
import com.chenjw.spider.dt.service.UserService;
import com.chenjw.spider.dt.service.WeiboService;
import com.chenjw.spider.dt.web.app.constants.DtConstants;

public class UserAction {

	@Autowired
	private UserService userService;
	@Autowired
	private WeiboService weiboService;

	public void doAddUser(@Param(name = "screenName") String screenName,@Param(name = "clientId") String clientId,@Param(name = "clientSecret") String clientSecret, Context context,
			Navigator navigator,HttpSession session) {
		TokenModel register=new TokenModel();
		register.setScreenName(screenName);
		register.setClientId(clientId);
		register.setClientSecret(clientSecret);
		session.setAttribute(DtConstants.REGISTER_USER_SESSION_KEY,register);
		navigator.forwardTo("regOauth.vm");
	}

}
