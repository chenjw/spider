package com.chenjw.spider.hacktools.test.tester;

import java.net.URL;

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
import com.typesafe.config.ConfigFactory;

/**
 * 10000
 * <p>
 * 42479ms
 * </p>
 * 
 * @author Administrator
 * 
 */
public class AkkaTester implements Tester {
	public static URL configFile = createURL("application.conf");

	private ActorSystem system = ActorSystem.create("PiSystem",
			ConfigFactory.parseURL(configFile));

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

		handleWorker.tell(new HandleMessage(handler));
	}

	@Override
	public void shutdown() {
		system.shutdown();
	}

	private static URL createURL(String str) {
		return AkkaTester.class.getClassLoader().getResource(str);
	}
}
