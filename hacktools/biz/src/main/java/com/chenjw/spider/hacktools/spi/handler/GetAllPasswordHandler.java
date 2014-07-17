package com.chenjw.spider.hacktools.spi.handler;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.chenjw.spider.hacktools.model.AccountModel;
import com.chenjw.spider.hacktools.spi.AccountHandler;

public class GetAllPasswordHandler implements AccountHandler {

	private String fileName;
	private Set<String> map=new HashSet<String>();
	
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
		if(account.getPassword()!=null && account.getPassword().length()!=32){
		    if(map.add(account.getPassword())){
		        System.out.println("["+fileName+"]found "+account.getPassword());
		    }
		    
		}
		
	}
	
	public void writeFile(String path){
	    try {
            FileUtils.writeLines(new File(path), map);
        } catch (IOException e) {
           e.printStackTrace();
        }
	}
	


	@Override
	public void endAccountGroup(String groupName) {
		System.out.println("end "+groupName);
		fileName=null;
	}

}
