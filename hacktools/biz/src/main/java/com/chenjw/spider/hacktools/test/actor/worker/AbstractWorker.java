package com.chenjw.spider.hacktools.test.actor.worker;

import akka.actor.UntypedActor;

public abstract class AbstractWorker extends UntypedActor {

	abstract void doReceive(Object message);

	public void onReceive(Object message) {
//		System.out.println("["+Thread.currentThread().getName()+"]("+Thread.currentThread().getThreadGroup().activeCount()+")[" + this.getClass().getSimpleName() + "] "
//				+ message);
		doReceive(message);
	}
}
