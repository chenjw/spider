package com.chenjw.spider.hacktools.mapper;

import org.apache.commons.lang.StringUtils;

import com.chenjw.spider.hacktools.model.AccountModel;

public class AccountMapper {
	private static final String SEPARATOR="|";
	public static String modelToString(AccountModel account){
		return StringUtils.defaultString(account.getLoginId())+SEPARATOR+StringUtils.defaultString(account.getNick())+SEPARATOR+StringUtils.defaultString(account.getEmail())+SEPARATOR+StringUtils.defaultString(account.getPassword())+SEPARATOR+StringUtils.defaultString(account.getMd5());
	}
	
	public static AccountModel stringToModel(String str){
		if (StringUtils.isBlank(str)) {
			return null;
		}
		String[] strs=StringUtils.split(str,SEPARATOR);
		AccountModel model=new AccountModel();
		model.setLoginId(get(strs,0));
		model.setNick(get(strs,1));
		model.setEmail(get(strs,2));
		model.setPassword(get(strs,3));
		model.setMd5(get(strs,4));
		return model;
	}
	
	private static String get(String[] strs,int num){
		if(num>=strs.length){
			return null;
		}
		return strs[num];
	}
}
