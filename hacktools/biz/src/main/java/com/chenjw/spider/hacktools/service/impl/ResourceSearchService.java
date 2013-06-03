package com.chenjw.spider.hacktools.service.impl;

import java.io.File;

import com.chenjw.spider.hacktools.ActorResourceParser;
import com.chenjw.spider.hacktools.spi.convertor.formated.FormatedConvertor;
import com.chenjw.spider.hacktools.spi.handler.SearchHandler;
import com.chenjw.spider.hacktools.spi.visitor.AccountVisitor;

public class ResourceSearchService {

	public void search(String str){
		long start=System.currentTimeMillis();
		File f=new File("C:\\test\\");
		SearchHandler handler=new SearchHandler(str);
		FormatedConvertor convertor=new FormatedConvertor();
		AccountVisitor visitor=new AccountVisitor(convertor,handler);
		ActorResourceParser.visit(f, visitor);
		System.out.println("use "+(System.currentTimeMillis()-start)+"ms");
	}
	
	public static void main(String[] args){
		ResourceSearchService t=new ResourceSearchService();
		t.search("cjw1983@gmail.com");
	}
}
