package com.chenjw.spider.dt.env;

import java.io.IOException;
import java.util.Properties;

import weibo4j.util.WeiboConfig;
import weibo4j.util.WeiboInfo;

import com.chenjw.tools.BeanCopyUtils;

public abstract class BaseProvider implements EnvProvider {
	private Properties properties;

	public Properties getProperties() {
		return properties;
	}
	
	protected void initProp(String path) {
		if (properties != null) {
			return;
		}
		try {
			properties = new Properties();
			properties.load(BaseProvider.class.getClassLoader()
					.getResourceAsStream(path));
		} catch (IOException e) {
		}
	}
	
	protected void initWeibo(Properties properties) {
		WeiboInfo weiboInfo=new WeiboInfo();
		BeanCopyUtils.copyProperties(weiboInfo, properties);
		WeiboConfig.load(weiboInfo);
	}

}
