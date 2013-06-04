package com.chenjw.spider.hacktools.test.actor.message;

import com.chenjw.spider.hacktools.test.job.TestJob;

public class JobMessage {
	public int index;
	public TestJob testHandler;

	public JobMessage(int index, TestJob testHandler) {
		super();
		this.index = index;
		this.testHandler = testHandler;
	}

}
