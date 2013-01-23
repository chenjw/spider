package com.chenjw.spider.dt.env;

import java.io.IOException;
import java.util.Properties;

public class LocalProvider implements EnvProvider {
	private static final Properties PROPERTIES = new Properties();

	public Properties getProperties() {
		return PROPERTIES;
	}

	static {
		init();
	}

	private static void init() {
		try {
			PROPERTIES.load(LocalProvider.class.getClassLoader()
					.getResourceAsStream("env/local/db.properties"));
		} catch (IOException e) {
		}
	}

	public boolean isEnable() {
		return true;

	}

	@Override
	public String getName() {
		return "local";
	}
}
