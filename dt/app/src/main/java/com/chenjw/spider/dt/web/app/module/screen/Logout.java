package com.chenjw.spider.dt.web.app.module.screen;

import javax.servlet.http.HttpSession;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.chenjw.spider.dt.web.app.constants.DtConstants;

//3221292113
public class Logout {

	public void execute(Context context, Navigator navigator,
			HttpSession session) {
		session.removeAttribute(DtConstants.USER_SESSION_KEY);
		session.removeAttribute(DtConstants.LOGIN_USER_SESSION_KEY);
		navigator.redirectToLocation("/");
		return;
	}
}