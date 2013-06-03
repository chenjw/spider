package com.chenjw.spider.hacktools.spi.actor.worker;

import akka.actor.UntypedActor;

public abstract class AbstractWorker extends UntypedActor {

	abstract void doReceive(Object message);

	public void onReceive(Object message) {
		System.out.println("[" + this.getClass().getSimpleName() + "] "
				+ message);
		doReceive(message);
	}
}
