package com.chenjw.spider.dt.listener;

import java.util.TimeZone;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DtConfigListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8");
		TimeZone.setDefault(tz);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

}
