package com.chenjw.spider.hacktools.test.style;

import com.chenjw.spider.hacktools.test.spi.Style;
import com.chenjw.spider.hacktools.test.tester.ParallelTester;

public class ParallelStyle implements Style {

	private int threadNum;
	private int threadNumBak;

	public ParallelStyle(int threadNum) {
		this.threadNum = threadNum;
	}

	@Override
	public void set() {
		this.threadNumBak = ParallelTester.threadNum;
		ParallelTester.threadNum = threadNum;
	}

	@Override
	public void reset() {
		ParallelTester.threadNum = threadNumBak;
	}

	@Override
	public String getDesc() {
		return "thread(" + threadNum + ")";
	}

}
