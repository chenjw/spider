package com.chenjw.spider.hacktools;

import java.io.File;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

import com.chenjw.spider.hacktools.spi.ResourceVisitor;
import com.chenjw.spider.hacktools.spi.actor.message.FileFinishMessage;
import com.chenjw.spider.hacktools.spi.actor.message.FolderMessage;
import com.chenjw.spider.hacktools.spi.actor.worker.FileWorker;
import com.chenjw.spider.hacktools.spi.actor.worker.FolderWorker;

public class ActorResourceParser {
	public static void visit(File file, final ResourceVisitor visitor) {
		// Create an Akka system
		ActorSystem system = ActorSystem.create("PiSystem");
		final Object lock = new Object();
		
		// create the master
		final ActorRef listener = system.actorOf(new Props(
				new UntypedActorFactory() {

					public UntypedActor create() {
						return new UntypedActor() {
							@Override
							public void onReceive(Object message)
									throws Exception {
								if (message instanceof FileFinishMessage) {
									 getContext().system().shutdown();
									lock.notifyAll();
								}

							}

						};
					}
				}), "listener");
		if (file.isDirectory()) {
			// create the master
			ActorRef master = system.actorOf(new Props(
					new UntypedActorFactory() {

						public UntypedActor create() {
							return new FolderWorker(visitor, listener);
						}
					}));
			master.tell(new FolderMessage(file),master);
			// visitFolder(file, visitor);
		} else if (file.isFile()) {
			ActorRef master = system.actorOf(new Props(
					new UntypedActorFactory() {
						public UntypedActor create() {
							return new FileWorker(visitor, listener);
						}
					}));
			master.tell(new FolderMessage(file),master);
			// visitFile(file, visitor);
		}
		synchronized(lock){
			try {
				lock.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
