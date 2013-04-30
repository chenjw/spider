package com.chenjw.spider.hacktools.spi.helper;

import java.io.IOException;
import java.util.Properties;

import com.chenjw.spider.hacktools.spi.ResourceConfig;
import com.chenjw.tools.BeanCopyUtils;

public class ResourceConfigHelper {
	public static ResourceConfig parse(String path){
		Properties p=new Properties();
		try {
			p.load(ResourceConfigHelper.class.getClassLoader().getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ResourceConfig config=new ResourceConfig();
		BeanCopyUtils.copyProperties(config, p);
		return config;
	} 
}
