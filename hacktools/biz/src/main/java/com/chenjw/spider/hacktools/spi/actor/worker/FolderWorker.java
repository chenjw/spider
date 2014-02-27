package com.chenjw.spider.hacktools.spi.actor.worker;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.routing.RoundRobinRouter;

import com.chenjw.spider.hacktools.spi.ResourceVisitor;
import com.chenjw.spider.hacktools.spi.actor.message.FileFinishMessage;
import com.chenjw.spider.hacktools.spi.actor.message.FileMessage;
import com.chenjw.spider.hacktools.spi.actor.message.FolderMessage;
import com.chenjw.spider.hacktools.spi.constants.Constants;

public class FolderWorker extends AbstractWorker {
	private ResourceVisitor visitor;

	private int doingNum;
	private int finishNum;
	private ActorRef listener;
	private boolean isAllSend = false;

	public FolderWorker(final ResourceVisitor visitor, ActorRef listener) {
		this.visitor = visitor;
		this.listener = listener;
	}

	public void doReceive(Object message) {
		if (message instanceof FolderMessage) {
			File folder = ((FolderMessage) message).file;
			visitor.enterFolder(folder);
			try {
				Iterator<File> iter = FileUtils
						.iterateFiles(folder, null, true);
				while (iter.hasNext()) {
					File f = iter.next();
					doingNum++;
					if (!iter.hasNext()) {
						isAllSend = true;
					}
					if (f.isDirectory()) {
						this.getContext()
								.actorOf(new Props(new UntypedActorFactory() {
									public UntypedActor create() {
										return new FolderWorker(visitor,
												getSelf());
									}
								})).tell(new FolderMessage(f), getSelf());
					} else if (f.isFile()) {
					    ActorRef actor=	this.getContext()
								.actorOf(new Props(new UntypedActorFactory() {
									public UntypedActor create() {
										return new FileWorker(visitor,
												getSelf());
									}
								}));
					    actor.tell(new FileMessage(f),actor);
					}
				}
			} finally {
				visitor.leaveFolder(folder);
			}
		} else if (message instanceof FileFinishMessage) {
			finishNum++;
			if (isAllSend && doingNum == finishNum) {
				listener.tell(new FileFinishMessage(), getSelf());
				// getContext().stop(getSelf());
			}
		} else {
			unhandled(message);
		}
	}
}