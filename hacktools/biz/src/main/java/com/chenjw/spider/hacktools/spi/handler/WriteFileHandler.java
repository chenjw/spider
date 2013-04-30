package com.chenjw.spider.hacktools.spi.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.lang.StringUtils;

import com.chenjw.spider.hacktools.model.AccountModel;
import com.chenjw.spider.hacktools.spi.AccountHandler;

public class WriteFileHandler implements AccountHandler {
	private File folder;
	private Writer fileWriter;
	
	public WriteFileHandler(File folder){
		this.folder=folder;
	}
	
	@Override
	public void startAccountGroup(String groupName) {
		System.out.println("start "+groupName);
		File f=new File(folder,groupName);
		try {
			fileWriter=new OutputStreamWriter(new FileOutputStream(f),"UTF-8");
		} catch (IOException e) {
		}
	}

	@Override
	public void handleAccount(AccountModel account) {
		if(account==null){
			return;
		}
		if(fileWriter!=null){
			try {
				fileWriter.write(StringUtils.defaultString(account.getLoginId())+"|"+StringUtils.defaultString(account.getNick())+"|"+StringUtils.defaultString(account.getEmail())+"|"+StringUtils.defaultString(account.getPassword())+"|"+StringUtils.defaultString(account.getMd5()));
				fileWriter.write("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void endAccountGroup(String groupName) {
		System.out.println("end "+groupName);
		if(fileWriter!=null){
			try {
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		fileWriter=null;
		
	}

}
