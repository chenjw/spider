package com.chenjw.spider.hacktools.test.job;

public class TestJob {
	public int getJobNum() {
		return 5;
	}

	public void doJob(int index) {
		try {
			//Thread.currentThread().dumpStack();
			//System.out.println(Thread.currentThread().getName()+"("+Thread.currentThread().getThreadGroup().getName()+"["+Thread.currentThread().getThreadGroup().activeCount()+"])");
			Thread.sleep(10L);
		} catch (InterruptedException e) {
		}
	}

}
