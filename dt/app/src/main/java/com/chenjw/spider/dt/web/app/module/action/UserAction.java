package com.chenjw.spider.dt.web.app.module.action;

import javax.servlet.http.HttpSession;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.web.app.constants.DtConstants;

public class UserAction {

	public void doAddUser(@Param(name = "clientId") String clientId,
			@Param(name = "clientSecret") String clientSecret, Context context,
			Navigator navigator, HttpSession session) {
		TokenModel register = new TokenModel();
		register.setClientId(clientId);
		register.setClientSecret(clientSecret);
		session.setAttribute(DtConstants.REGISTER_USER_SESSION_KEY, register);
		navigator.forwardTo("regOauth.vm");
	}

}
