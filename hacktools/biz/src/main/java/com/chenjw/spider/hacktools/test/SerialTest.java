package com.chenjw.spider.hacktools.test;

public class SerialTest implements Test {

	@Override
	public void start(ResultCallback callback) {
		TestHandler handler=new TestHandler();
		for(int i=0;i<handler.getJobNum();i++){
			handler.doJob(i);
		}
		callback.finished();
	}

	@Override
	public void shutdown() {

		
	}

}
