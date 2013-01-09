package com.chenjw.spider.dt.web.app.module.screen;

import java.util.Map;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;

public class Env {

	public void execute(Context context, Navigator navigator) {
		Map<String, String> map = System.getenv();
		context.put("env", map);
	}

}
