package com.chenjw.spider.dt.env;

import java.io.IOException;
import java.util.Properties;

import weibo4j.util.WeiboConfig;

public class BaeProvider implements EnvProvider {
	private Properties properties;

	public Properties getProperties() {
		return properties;
	}

	@Override
	public void init() {
		initDb();
		initWeibo();
		
	}
	
	private void initDb() {
		if (properties != null) {
			return;
		}
		try {
			properties = new Properties();
			properties.load(LocalProvider.class.getClassLoader()
					.getResourceAsStream("env/bae/db.properties"));
		} catch (IOException e) {
		}
	}
	
	private void initWeibo() {
		WeiboConfig.load("env/bae/weibo.properties");
	}

	public boolean isEnable() {
		return "bae".equals(System.getenv("USER"));
	}

	@Override
	public String getName() {
		return "bae";
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
