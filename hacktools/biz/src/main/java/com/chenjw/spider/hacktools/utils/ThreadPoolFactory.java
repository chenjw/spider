package com.chenjw.spider.hacktools.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolFactory {
	private static ExecutorService pool = Executors.newFixedThreadPool(10);

	public static ExecutorService getDefaultThreadPoolExecutor() {
		return pool;
	}
}
