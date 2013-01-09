package com.chenjw.spider.dt.constants;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.FactoryBean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CloudFoundryEnv implements FactoryBean {
	// private static final Map<String, Map<String, Map<String, String>>>
	// properties = new HashMap<String, Map<String, Map<String, String>>>();
	private static final Properties properties = new Properties();
	private static final String VCAP_SERVICES_KEY = "VCAP_SERVICES";

	static {
		init();
		for (Entry<Object, Object> e : properties.entrySet()) {
			Constants.LOGGER.info(e.getKey() + "=" + e.getValue());
		}
	}

	@SuppressWarnings("unchecked")
	private static void init() {
		String services = System.getenv(VCAP_SERVICES_KEY);
		if (services == null) {
			try {
				services = IOUtils.toString(CloudFoundryEnv.class
						.getClassLoader().getResourceAsStream("VCAP_SERVICES"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Map<String, Object> map = JSON.parseObject(services, Map.class);
		String key = "";
		for (Entry<String, Object> e : map.entrySet()) {
			key = e.getKey();
			JSONArray array = (JSONArray) e.getValue();
			for (Object obj : array) {

				Map<String, Object> map1 = (Map<String, Object>) JSON
						.toJavaObject((JSONObject) obj, Map.class);
				String name = (String) map1.get("name");
				String key1 = key + "_" + name;
				Map<String, Object> map2 = JSON.toJavaObject(
						(JSONObject) map1.get("credentials"), Map.class);
				for (Entry<String, Object> entry : map2.entrySet()) {
					String key2 = key1 + "_" + entry.getKey();
					properties.put(key2, entry.getValue().toString());
				}
			}

		}

	}

	private static String getProperty1(
			Map<String, Map<String, String>> properties1, String mysqlName,
			String key) {
		Map<String, String> m = properties1.get(mysqlName);
		if (m == null) {
			return null;
		} else {
			return m.get(key);
		}
	}

	// public static String getMySQLProperty(String mysqlName, String key) {
	// Map<String, Map<String, String>> properties1 = properties
	// .get("mysql-5.1");
	// if (properties1 == null) {
	// return null;
	// } else {
	// return getProperty1(properties1, mysqlName, key);
	// }
	// }

	@Override
	public Object getObject() throws Exception {
		return properties;
	}

	@Override
	public Class getObjectType() {
		return Properties.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
