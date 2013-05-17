package com.chenjw.spider.dt.web.app.module.screen;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.chenjw.spider.dt.service.DeletedTweetCheckService;


public class Command {
	@Autowired
	private DeletedTweetCheckService deletedTweetCheckService;
	
	public void execute(Context context,@Param(name = "command") String command,
			Navigator navigator) {
		if(command==null){
			return;
		}
		String commandName=StringUtils.substringBefore(command, " ");
		String commandArgs=StringUtils.substringAfter(command, " ");
		if("check".equals(commandName)){
			check(commandArgs);
		}
		context.put("message", "success");
		navigator.forwardTo("message.vm");
	}
	
	private void check(String args){
		deletedTweetCheckService.checkAll();
	}
}