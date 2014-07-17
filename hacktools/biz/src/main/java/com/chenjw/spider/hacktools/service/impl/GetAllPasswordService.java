package com.chenjw.spider.hacktools.service.impl;

import java.io.File;

import com.chenjw.spider.hacktools.ActorResourceParser;
import com.chenjw.spider.hacktools.ResourceParser;
import com.chenjw.spider.hacktools.spi.convertor.formated.FormatedConvertor;
import com.chenjw.spider.hacktools.spi.handler.GetAllPasswordHandler;
import com.chenjw.spider.hacktools.spi.visitor.AccountVisitor;

public class GetAllPasswordService {

	public void search(String path){
		long start=System.currentTimeMillis();
		File f=new File("/home/chenjw/flareGet/Compressed/hack/password");
		GetAllPasswordHandler handler=new GetAllPasswordHandler();
		FormatedConvertor convertor=new FormatedConvertor();
		AccountVisitor visitor=new AccountVisitor(convertor,handler);
		ResourceParser.visit(f, visitor);
		handler.writeFile(path);
		System.out.println("use "+(System.currentTimeMillis()-start)+"ms");
	}
	
	public static void main(String[] args){
		GetAllPasswordService t=new GetAllPasswordService();
		t.search("/home/chenjw/flareGet/Compressed/output.txt");
	}
}
