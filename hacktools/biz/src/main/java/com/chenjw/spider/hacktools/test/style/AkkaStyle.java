package com.chenjw.spider.hacktools.test.style;

import java.net.URL;

import com.chenjw.spider.hacktools.test.spi.Style;
import com.chenjw.spider.hacktools.test.tester.AkkaTester;

public class AkkaStyle implements Style {
	private URL bak = AkkaTester.configFile;

	public static String fd = "com/chenjw/spider/hacktools/test/style/akka/conf/";

	private String fileName;

	public AkkaStyle(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void set() {
		this.setFile(fileName);

	}

	@Override
	public void reset() {
		this.recover();
	}

	@Override
	public String getDesc() {
		return fileName;
	}

	protected void setFile(String fileName) {
		AkkaTester.configFile = AkkaStyle.class.getClassLoader().getResource(
				fd + fileName);
	}

	protected void recover() {
		AkkaTester.configFile = bak;

	}
}
