package com.chenjw.spider.hacktools.spi.visitor;

import java.io.File;

import com.chenjw.spider.hacktools.model.AccountModel;
import com.chenjw.spider.hacktools.spi.AccountHandler;
import com.chenjw.spider.hacktools.spi.Convertor;
import com.chenjw.spider.hacktools.spi.constants.VisitorActionEnum;


/**
 * 最外面文件夹的名字作为groupName
 * 
 * @author Administrator
 *
 */
public class  AccountVisitor extends SimpleResourceVisitor {
	private Convertor convertor;
	private AccountHandler handler;

	private int depth=0;
	
	
	public AccountVisitor(Convertor convertor,AccountHandler handler){
		this.convertor=convertor;
		this.handler=handler;
	}
	
	private void start(File folder){
		if(depth==0){
			String groupName=folder.getName();
			handler.startAccountGroup(groupName);
		}
		depth++;
	}
	
	private void end(File folder){
		depth--;
		if(depth==0){
			String groupName=folder.getName();
			handler.endAccountGroup(groupName);
		}
	}
	
	@Override
	public VisitorActionEnum enterFolder(File folder) {
		start(folder);
		return VisitorActionEnum.CONTINUE;
	}

	@Override
	public void leaveFolder(File folder) {
		end(folder);
	}

	@Override
	public VisitorActionEnum enterFile(File file) {
		start(file);
		return super.enterFile(file);
	}

	@Override
	public void leaveFile(File file) {
		end(file);
		super.leaveFile(file);
	}

	@Override
	public VisitorActionEnum enterLine(String line) {
		AccountModel account=convertor.convert(line);
		handler.handleAccount(account);
		return VisitorActionEnum.CONTINUE;
	}
	
}
