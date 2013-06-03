package com.chenjw.spider.hacktools.test.actor.worker;

import akka.actor.ActorRef;

import com.chenjw.spider.hacktools.test.actor.message.HandleFinishMessage;
import com.chenjw.spider.hacktools.test.actor.message.JobFinishMessage;

public class ResultWorker extends AbstractWorker {
	private ActorRef listener;
	private int num;
	private int finishedNum;

	public ResultWorker(ActorRef listener, int num) {
		this.listener = listener;
		this.num = num;
	}

	@Override
	void doReceive(Object message) {
		if (message instanceof JobFinishMessage) {
			finishedNum++;
			if (finishedNum >= num) {
				listener.tell(new HandleFinishMessage());
			}
		}
	}

}
