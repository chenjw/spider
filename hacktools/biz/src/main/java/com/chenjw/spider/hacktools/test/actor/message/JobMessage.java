package com.chenjw.spider.hacktools.test.actor.message;

import com.chenjw.spider.hacktools.test.TestHandler;

public class JobMessage {
	public int index;
	public TestHandler testHandler;

	public JobMessage(int index, TestHandler testHandler) {
		super();
		this.index = index;
		this.testHandler = testHandler;
	}

}
