package com.chenjw.spider.dt.env;

import java.io.IOException;
import java.util.Properties;

import weibo4j.util.WeiboConfig;

public class SaeProvider implements EnvProvider {
	private static Properties properties;

	@Override
	public void init() {
		initDb();
		initWeibo();
	}

	
	public Properties getProperties() {
		return properties;
	}

	private  void initDb() {
		if (properties != null) {
			return;
		}
		try {
			properties = new Properties();
			properties.load(LocalProvider.class.getClassLoader()
					.getResourceAsStream("env/sae/db.properties"));
		} catch (IOException e) {
		}
	}
	
	private void initWeibo() {
		WeiboConfig.load("env/sae/weibo.properties");
	}

	public boolean isEnable() {
		return true;

	}

	@Override
	public String getName() {
		return "sae";
	}

	@Override
	public int getInstanceCount() {
		return 1;
	}

	@Override
	public int getInstanceIndex() {
		return 0;
	}
}
