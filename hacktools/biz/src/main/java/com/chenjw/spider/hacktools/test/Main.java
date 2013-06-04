package com.chenjw.spider.hacktools.test;

import java.util.concurrent.CountDownLatch;

import com.chenjw.spider.hacktools.test.provider.AkkaProvider;
import com.chenjw.spider.hacktools.test.provider.ParallelProvider;
import com.chenjw.spider.hacktools.test.spi.Provider;
import com.chenjw.spider.hacktools.test.spi.ResultCallback;
import com.chenjw.spider.hacktools.test.spi.Style;
import com.chenjw.spider.hacktools.test.spi.Tester;

public class Main {

	private static void startOne(Tester test, ResultCallback callback) {
		test.start(callback);
	}

	private static void start(Tester test, int times) {
		final CountDownLatch countDownLatch = new CountDownLatch(times);
		for (int i = 0; i < times; i++) {
			startOne(test, new ResultCallback() {
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

	private static long testStyle(Style style, Tester test, int times) {

		long start = System.currentTimeMillis();
		start(test, times);
		long timeUsed = System.currentTimeMillis() - start;
		System.out.println(test.getClass().getSimpleName() + "["
				+ style.getDesc() + "] use " + timeUsed + "ms");
		test.shutdown();

		return timeUsed;
	}

	private static void testAll(Provider provider, int times) {
		for (Style style : provider.getStyles()) {
			style.set();
			Tester test = provider.createTest();
			testStyle(style, test, times);
			style.reset();
		}
	}
	


	public static void main(String[] args) {
		testAll(new AkkaProvider(), 1000);
		testAll(new ParallelProvider(), 1000);
	}

	// 10000请求
	// 并行50线程 10066ms
	// actor 42142ms
}
