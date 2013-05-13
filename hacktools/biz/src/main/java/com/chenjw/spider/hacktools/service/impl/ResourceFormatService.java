package com.chenjw.spider.hacktools.service.impl;

import java.io.File;

import com.chenjw.spider.hacktools.ResourceParser;
import com.chenjw.spider.hacktools.spi.ResourceConfig;
import com.chenjw.spider.hacktools.spi.convertor.A1000W_IS2_16436Convertor;
import com.chenjw.spider.hacktools.spi.convertor.A100SDAConvertor;
import com.chenjw.spider.hacktools.spi.convertor.A113WConvertor;
import com.chenjw.spider.hacktools.spi.convertor.A1781000w3087Convertor;
import com.chenjw.spider.hacktools.spi.convertor.A2Convertor;
import com.chenjw.spider.hacktools.spi.convertor.A5000WConvertor;
import com.chenjw.spider.hacktools.spi.convertor.A500WConvertor;
import com.chenjw.spider.hacktools.spi.convertor.A766_16368Convertor;
import com.chenjw.spider.hacktools.spi.convertor.A766_kaixinConvertor;
import com.chenjw.spider.hacktools.spi.convertor.A7k7k2000w2047Convertor;
import com.chenjw.spider.hacktools.spi.convertor.A96wConvertor;
import com.chenjw.spider.hacktools.spi.convertor.CSDN600wConvertor;
import com.chenjw.spider.hacktools.spi.convertor.DuowanConvertor;
import com.chenjw.spider.hacktools.spi.convertor.Format30WclConvertor;
import com.chenjw.spider.hacktools.spi.convertor.HConvertor;
import com.chenjw.spider.hacktools.spi.convertor.Ha1Convertor;
import com.chenjw.spider.hacktools.spi.convertor.ZawConvertor;
import com.chenjw.spider.hacktools.spi.handler.WriteFileHandler;
import com.chenjw.spider.hacktools.spi.helper.ResourceConfigHelper;
import com.chenjw.spider.hacktools.spi.visitor.AccountVisitor;

public class ResourceFormatService {

	public void formatResource(){
		ResourceConfig config=ResourceConfigHelper.parse("com/chenjw/spider/hacktools/spi/resource/r.properties");
		File f=new File("C:\\hack\\珍爱网200万\\");
		WriteFileHandler handler=new WriteFileHandler(new File("c://test/"));
		//ExpressionConvertor convertor=new ExpressionConvertor(config.getLineExpression());
		ZawConvertor convertor=new ZawConvertor();
		AccountVisitor visitor=new AccountVisitor(convertor,handler);
		
		ResourceParser.visit(f, visitor);
	}
	
	public static void main(String[] args){
		ResourceFormatService t=new ResourceFormatService();
		t.formatResource();
	}
}
