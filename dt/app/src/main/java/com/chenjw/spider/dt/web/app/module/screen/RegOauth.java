package com.chenjw.spider.dt.web.app.module.screen;

import javax.servlet.http.HttpSession;

import weibo4j.util.WeiboConfig;

import com.alibaba.citrus.turbine.Context;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.web.app.constants.DtConstants;

//3221292113
/**
 * 
 * @author chenjw
 * 
 */
public class RegOauth {

	public void execute(Context context,HttpSession session) {
		// redirect_URI
		// client_ID
		TokenModel register=(TokenModel) session.getAttribute(DtConstants.REGISTER_USER_SESSION_KEY);
		context.put("register",register);
	}
}