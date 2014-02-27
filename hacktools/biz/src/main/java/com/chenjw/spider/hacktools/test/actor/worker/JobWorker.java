package com.chenjw.spider.hacktools.test.actor.worker;

import akka.actor.ActorRef;

import com.chenjw.spider.hacktools.test.actor.message.JobFinishMessage;
import com.chenjw.spider.hacktools.test.actor.message.JobMessage;

public class JobWorker extends AbstractWorker {

	@Override
	void doReceive(Object message) {
		if (message instanceof JobMessage) {
			JobMessage msg = (JobMessage) message;
			msg.testHandler.doJob(msg.index);
			ActorRef sender=this.sender();
			sender.tell(new JobFinishMessage(msg.index),sender);
		}
	}

}
