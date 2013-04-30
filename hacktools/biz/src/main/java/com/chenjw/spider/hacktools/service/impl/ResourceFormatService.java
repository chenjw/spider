package com.chenjw.spider.hacktools.service.impl;

import java.io.File;

import com.chenjw.spider.hacktools.ResourceParser;
import com.chenjw.spider.hacktools.spi.ResourceConfig;
import com.chenjw.spider.hacktools.spi.convertor.Aipai1156Convertor;
import com.chenjw.spider.hacktools.spi.convertor.Renren500WConvertor;
import com.chenjw.spider.hacktools.spi.convertor.ExpressionConvertor;
import com.chenjw.spider.hacktools.spi.handler.WriteFileHandler;
import com.chenjw.spider.hacktools.spi.helper.ResourceConfigHelper;
import com.chenjw.spider.hacktools.spi.visitor.AccountVisitor;

public class ResourceFormatService {

	public void formatResource(){
		ResourceConfig config=ResourceConfigHelper.parse("com/chenjw/spider/hacktools/spi/resource/r.properties");
		File f=new File("C:\\hack\\#968 aipai_1156\\");
		WriteFileHandler handler=new WriteFileHandler(new File("c://test/"));
		//ExpressionConvertor convertor=new ExpressionConvertor(config.getLineExpression());
		Aipai1156Convertor convertor=new Aipai1156Convertor();
		AccountVisitor visitor=new AccountVisitor(convertor,handler);
		
		ResourceParser.visit(f, visitor);
	}
	
	public static void main(String[] args){
		ResourceFormatService t=new ResourceFormatService();
		t.formatResource();
	}
}
