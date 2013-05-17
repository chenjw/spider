package com.chenjw.spider.dt.constants;

import com.chenjw.spider.dt.env.BaeProvider;
import com.chenjw.spider.dt.env.CloudfoundryProvider;
import com.chenjw.spider.dt.env.EnvProvider;
import com.chenjw.spider.dt.env.LocalProvider;
import com.chenjw.spider.dt.env.SaeProvider;

public class EnvConstants {

	private static EnvProvider envProvider;

	static {
		EnvProvider[] envs = new EnvProvider[] { new CloudfoundryProvider(),
				 new BaeProvider(), new LocalProvider() };
		for (EnvProvider env : envs) {
			if (env.isEnable()) {
				envProvider = env;
				break;
			}
		}
		// 初始化
		envProvider.init();
	}

	public static EnvProvider getEnvProvider() {
		return envProvider;
	}

	public static boolean isProductMode() {
		// return true;
		return !"local".equals(envProvider.getName());
	}

}
