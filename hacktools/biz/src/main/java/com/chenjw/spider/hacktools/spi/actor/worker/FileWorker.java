package com.chenjw.spider.hacktools.spi.actor.worker;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

import com.chenjw.spider.hacktools.spi.ResourceVisitor;
import com.chenjw.spider.hacktools.spi.actor.message.EncodingMessage;
import com.chenjw.spider.hacktools.spi.actor.message.FileFinishMessage;
import com.chenjw.spider.hacktools.spi.actor.message.FileMessage;
import com.chenjw.spider.hacktools.spi.actor.message.LineFinishMessage;
import com.chenjw.spider.hacktools.spi.actor.message.LineMessage;

public class FileWorker extends AbstractWorker {
	private ResourceVisitor visitor;

	private int doingNum;
	private int finishNum;
	private ActorRef listener;
	private boolean isAllSend = false;

	private FileMessage fileMessage;

	public FileWorker(final ResourceVisitor visitor, ActorRef listener) {
		this.visitor = visitor;
		this.listener = listener;
	}

	public void doReceive(Object message) {
		if (message instanceof FileMessage) {
			fileMessage = (FileMessage) message;
			File file = ((FileMessage) message).file;
			visitor.enterFile(file);
			this.getContext().actorOf(new Props(new UntypedActorFactory() {
				public UntypedActor create() {
					return new GetFileEncodingWorker(self());
				}
			})).tell(new FileMessage(file), self());

		} else if (message instanceof EncodingMessage) {
			String encoding = ((EncodingMessage) message).encoding;
			try {
				System.out.println(fileMessage.file.getAbsolutePath() + " "
						+ encoding);
				LineIterator iter = FileUtils.lineIterator(fileMessage.file,
						encoding);
				while (iter.hasNext()) {
					String line = iter.nextLine();
					doingNum++;
					if (!iter.hasNext()) {
						isAllSend = true;
					}
					this.getContext()
							.actorOf(new Props(new UntypedActorFactory() {
								public UntypedActor create() {
									return new LineWorker(visitor);
								}
							})).tell(new LineMessage(line));
					// visitor.enterLine(line);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				visitor.leaveFile(fileMessage.file);
			}
		} else if (message instanceof LineFinishMessage) {
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