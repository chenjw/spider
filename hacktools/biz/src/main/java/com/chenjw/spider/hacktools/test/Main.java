package com.chenjw.spider.hacktools.test;

import java.util.concurrent.CountDownLatch;

public class Main {
	
	public static void startOne(Test test,ResultCallback callback){
		test.start(callback);
	}
	
	public static void start(Test test,int times){
		final CountDownLatch countDownLatch=new CountDownLatch(times);
		for(int i=0;i<times;i++){
			startOne(test,new ResultCallback(){
				@Override
				public void finished() {
					countDownLatch.countDown();
				}
			});
		}

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		//Test test=new SerialTest();
		//Test test=new ParallelTest();//1821ms(500)
		Test test=new ActorTest();// 42193ms(tpe8/64) 42205ms(tpe40/64)
		long start=System.currentTimeMillis();
		start(test,2);
		System.out.println(test.getClass().getSimpleName()+" use "+(System.currentTimeMillis()-start)+"ms");
		test.shutdown();
	}
	// 10000请求
	// 并行50线程 10066ms
	// actor 42142ms
}
