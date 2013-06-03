package com.chenjw.spider.hacktools.test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

import com.chenjw.spider.hacktools.test.actor.message.HandleMessage;
import com.chenjw.spider.hacktools.test.actor.worker.HandleWorker;

/** 10000
 * <p>42479ms</p>
 * @author Administrator
 *
 */
public class ActorTest implements Test {
	private  ActorSystem system = ActorSystem.create("PiSystem");

	@Override
	public void start(final ResultCallback callback) {
		final TestHandler handler = new TestHandler();
		final ActorRef handleWorker = system.actorOf(new Props(
				new UntypedActorFactory() {
					private static final long serialVersionUID = -3332591400918928965L;

					public UntypedActor create() {
						return new HandleWorker(callback);
					}
				}));

		handleWorker.tell(new HandleMessage(handler));
	}

	@Override
	public void shutdown() {
		system.shutdown();
	}

}
