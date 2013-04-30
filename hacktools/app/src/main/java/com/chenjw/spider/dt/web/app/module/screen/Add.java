package com.chenjw.spider.dt.web.app.module.screen;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.chenjw.spider.hacktools.constants.EnvConstants;


public class Add {

	public void execute(Context context,
			Navigator navigator) {
		context.put("isProductMode", EnvConstants.isProductMode());
	}
}