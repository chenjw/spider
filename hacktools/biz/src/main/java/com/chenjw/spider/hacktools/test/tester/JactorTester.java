package com.chenjw.spider.hacktools.test.tester;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

import com.chenjw.spider.hacktools.test.actor.message.HandleMessage;
import com.chenjw.spider.hacktools.test.actor.worker.HandleWorker;
import com.chenjw.spider.hacktools.test.job.TestJob;
import com.chenjw.spider.hacktools.test.spi.ResultCallback;
import com.chenjw.spider.hacktools.test.spi.Tester;

/** 10000
 * <p>42479ms</p>
 * @author Administrator
 *
 */
public class JactorTester implements Tester {
	private  ActorSystem system = ActorSystem.create("PiSystem");

	@Override
	public void start(final ResultCallback callback) {
		final TestJob handler = new TestJob();
		final ActorRef handleWorker = system.actorOf(new Props(
				new UntypedActorFactory() {
					private static final long serialVersionUID = -3332591400918928965L;

					public UntypedActor create() {
						return new HandleWorker(callback);
					}
				}));

		handleWorker.tell(new HandleMessage(handler),handleWorker);
	}

	@Override
	public void shutdown() {
		system.shutdown();
	}

}
