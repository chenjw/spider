package com.chenjw.spider.hacktools.env;

import java.util.Properties;

import org.springframework.beans.factory.FactoryBean;

import com.chenjw.spider.hacktools.constants.EnvConstants;

public class EnvFactoryBean implements FactoryBean {

	@Override
	public Object getObject() throws Exception {

		return EnvConstants.getEnvProvider().getProperties();
	}

	@Override
	public Class<?> getObjectType() {
		return Properties.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
