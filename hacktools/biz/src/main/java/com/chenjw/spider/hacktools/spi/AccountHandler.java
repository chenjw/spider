package com.chenjw.spider.hacktools.spi;

import com.chenjw.spider.hacktools.model.AccountModel;

public interface AccountHandler {
	public void startAccountGroup(String groupName);
	
	public void handleAccount(AccountModel account);
	
	public void endAccountGroup(String groupName);
}
