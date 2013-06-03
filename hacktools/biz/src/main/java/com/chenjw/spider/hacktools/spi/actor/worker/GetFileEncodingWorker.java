package com.chenjw.spider.hacktools.spi.actor.worker;

import java.io.File;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

import com.chenjw.spider.hacktools.spi.actor.message.EncodingMessage;
import com.chenjw.spider.hacktools.spi.actor.message.FileEncodingCheckResultMessage;
import com.chenjw.spider.hacktools.spi.actor.message.FileEncodingMessage;
import com.chenjw.spider.hacktools.spi.actor.message.FileMessage;

public class GetFileEncodingWorker extends AbstractWorker {

	private ActorRef listener;

	public GetFileEncodingWorker(ActorRef listener) {

		this.listener = listener;
	}

	private String[] ENCODINGS = new String[] { "UTF-8", "GB18030", "UTF-16" };

	public void doReceive(Object message) {
		if (message instanceof FileMessage) {
			File file = ((FileMessage) message).file;

			ActorRef checkFileEncodingWorker = this.getContext().actorOf(
					new Props(new UntypedActorFactory() {
						public UntypedActor create() {
							return new CheckFileEncodingWorker(self());
						}
					}));
			checkFileEncodingWorker.tell(new FileEncodingMessage(file, 0,
					ENCODINGS[0]), this.self());
		
		} else if (message instanceof FileEncodingCheckResultMessage) {
			FileEncodingCheckResultMessage result = (FileEncodingCheckResultMessage) message;
			File file = result.file;
			int index = result.index;
			String encoding = result.encoding;
			if (result.isSuccess) {
				listener.tell(new EncodingMessage(encoding));
				return;
			}
			index++;
			if (index >= ENCODINGS.length) {
				listener.tell(new EncodingMessage(null));
				return;
			} else {
				this.getContext()
						.actorOf(new Props(new UntypedActorFactory() {
							public UntypedActor create() {
								return new CheckFileEncodingWorker(self());
							}
						}))
						.tell(new FileEncodingMessage(file, index,
								ENCODINGS[index]));
			}
		}
	}
}
