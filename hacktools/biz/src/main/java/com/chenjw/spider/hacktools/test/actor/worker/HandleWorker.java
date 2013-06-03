package com.chenjw.spider.hacktools.test.actor.worker;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.routing.RoundRobinRouter;

import com.chenjw.spider.hacktools.test.ResultCallback;
import com.chenjw.spider.hacktools.test.actor.message.HandleFinishMessage;
import com.chenjw.spider.hacktools.test.actor.message.HandleMessage;
import com.chenjw.spider.hacktools.test.actor.message.JobMessage;

public class HandleWorker extends AbstractWorker {
	private ResultCallback resultCallback;

	public HandleWorker(ResultCallback resultCallback) {
		this.resultCallback = resultCallback;
	}

	@Override
	void doReceive(Object message) {
		if (message instanceof HandleMessage) {
			final HandleMessage msg = (HandleMessage) message;
			ActorRef resultWorker = this.getContext().actorOf(
					new Props(new UntypedActorFactory() {
						private static final long serialVersionUID = -7679647237037885453L;

						public UntypedActor create() {
							return new ResultWorker(self(), msg.testHandler
									.getJobNum());
						}
					}));
			
			for (int i = 0; i < msg.testHandler.getJobNum(); i++) {
				ActorRef jobWorker = this.getContext().actorOf(
						new Props(JobWorker.class));
				jobWorker
						.tell(new JobMessage(i, msg.testHandler), resultWorker);
			}
		} else if (message instanceof HandleFinishMessage) {
			resultCallback.finished();
		}
	}

}
