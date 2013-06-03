package com.chenjw.spider.hacktools.spi.visitor;

import java.io.File;

import com.chenjw.spider.hacktools.model.AccountModel;
import com.chenjw.spider.hacktools.spi.AccountHandler;
import com.chenjw.spider.hacktools.spi.Convertor;
import com.chenjw.spider.hacktools.spi.constants.VisitorActionEnum;
import com.chenjw.spider.hacktools.utils.ThreadPoolFactory;


/**
 * 最外面文件夹的名字作为groupName
 * 
 * @author Administrator
 *
 */
public class  PooledAccountVisitor extends AccountVisitor {

	public PooledAccountVisitor(Convertor convertor, AccountHandler handler) {
		super(convertor, handler);
	}
	
	public VisitorActionEnum enterLine(final String line) {
		ThreadPoolFactory.getDefaultThreadPoolExecutor().execute(new Runnable(){
			@Override
			public void run() {
				PooledAccountVisitor.super.enterLine(line);
			}
			
		});
		return VisitorActionEnum.CONTINUE;
	}
}
