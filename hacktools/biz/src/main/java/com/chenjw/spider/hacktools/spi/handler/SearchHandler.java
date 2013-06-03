package com.chenjw.spider.hacktools.spi.handler;

import org.apache.commons.lang.StringUtils;

import com.chenjw.spider.hacktools.mapper.AccountMapper;
import com.chenjw.spider.hacktools.model.AccountModel;
import com.chenjw.spider.hacktools.spi.AccountHandler;

public class SearchHandler implements AccountHandler {
	private String searchStr;
	private String fileName;
	
	public SearchHandler(String searchStr){
		this.searchStr=searchStr;
	}
	
	@Override
	public void startAccountGroup(String groupName) {
		System.out.println("start "+groupName);
		fileName=groupName;
	}

	@Override
	public void handleAccount(AccountModel account) {
		if(account==null){
			return;
		}
		if(find(new String[]{account.getLoginId(),account.getEmail(),account.getPassword(),account.getNick()},searchStr)){
			System.out.println("["+fileName+"]found "+AccountMapper.modelToString(account));
		}
		
	}
	
	private boolean find(String[] strs,String findStr){
		for(String str:strs){
			if(find(str,findStr)){
				return true;
			}
		}
		return false;
	}
	
	private boolean find(String str,String findStr){
		if(StringUtils.isBlank(str)){
			return false;
		}
		return StringUtils.indexOf(str, findStr)!=-1;
	}

	@Override
	public void endAccountGroup(String groupName) {
		System.out.println("end "+groupName);
		fileName=null;
	}

}
