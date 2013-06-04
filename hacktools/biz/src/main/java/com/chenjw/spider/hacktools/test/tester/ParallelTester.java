package com.chenjw.spider.hacktools.test.tester;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.chenjw.spider.hacktools.test.job.TestJob;
import com.chenjw.spider.hacktools.test.spi.ResultCallback;
import com.chenjw.spider.hacktools.test.spi.Tester;

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
public class ParallelTester implements Tester {
	public static int threadNum=500;
	private  ExecutorService executorService=Executors.newScheduledThreadPool(threadNum);
	@Override
	public void start(final ResultCallback callback) {
		

		final TestJob handler=new TestJob();
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
