package com.chenjw.spider.hacktools.test.tester;

import com.chenjw.spider.hacktools.test.job.TestJob;
import com.chenjw.spider.hacktools.test.spi.ResultCallback;
import com.chenjw.spider.hacktools.test.spi.Tester;

public class SerialTester implements Tester {

	@Override
	public void start(ResultCallback callback) {
		TestJob handler=new TestJob();
		for(int i=0;i<handler.getJobNum();i++){
			handler.doJob(i);
		}
		callback.finished();
	}

	@Override
	public void shutdown() {

		
	}

}
