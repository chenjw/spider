package com.chenjw.spider.hacktools.test.spi;


public interface Provider {
	public Tester createTest();

	public Style[] getStyles();

}
