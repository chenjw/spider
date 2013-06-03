package com.chenjw.spider.hacktools.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/** 10000
 * <p>5:100462ms</p>
 * <p>10:51042ms</p>
 * <p>50:10141ms</p>
 * <p>100:5180ms</p>
 * <p>500:1678ms</p>
 * <p>1000:2460ms</p>
 * <p>5000:8482ms</p>
 * @author Administrator
 *
 */
public class ParallelTest implements Test {
	private  ExecutorService executorService=Executors.newScheduledThreadPool(500);
	@Override
	public void start(final ResultCallback callback) {
		

		final TestHandler handler=new TestHandler();
		final AtomicInteger countDownLatch=new AtomicInteger(handler.getJobNum());
		for(int i=0;i<handler.getJobNum();i++){
			final int index=i;
			executorService.execute(new Runnable(){
				@Override
				public void run() {
					try{
						handler.doJob(index);
					}
					finally{
						if(countDownLatch.decrementAndGet()==0){
							callback.finished();
						}
					}
				}
			});
		}
	}
	@Override
	public void shutdown() {
		executorService.shutdown();
	}

}
