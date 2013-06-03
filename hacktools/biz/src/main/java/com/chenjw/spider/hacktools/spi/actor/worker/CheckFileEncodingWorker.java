package com.chenjw.spider.hacktools.spi.actor.worker;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

import com.chenjw.spider.hacktools.spi.actor.message.AllStartedMessage;
import com.chenjw.spider.hacktools.spi.actor.message.FileEncodingMessage;
import com.chenjw.spider.hacktools.spi.actor.message.LineEncodingMessage;
import com.chenjw.spider.hacktools.spi.actor.message.StartMessage;

public class CheckFileEncodingWorker extends AbstractWorker {

	private ActorRef listener;

	//
	public CheckFileEncodingWorker(ActorRef listener) {
		this.listener = listener;
	}

	public void doReceive(Object message) {
		if (message instanceof FileEncodingMessage) {

			final FileEncodingMessage fileEncodingMessage = (FileEncodingMessage) message;
			File file = fileEncodingMessage.file;
			String encoding = fileEncodingMessage.encoding;

			final ActorRef resultWorker = this.getContext().actorOf(
					new Props(new UntypedActorFactory() {
						public UntypedActor create() {
							return new CheckFileEncodingResultWorker( null,listener);
						}
					}));
			
			LineIterator iter = null;
			try {
				iter = FileUtils.lineIterator(file, encoding);
				while (iter.hasNext()) {
					String line = iter.nextLine();
					
					//System.out.println(resultWorker);
					
					//resultWorker.tell(new StartMessage());
	
					if (!iter.hasNext()) {
						resultWorker.tell(new AllStartedMessage());
					}
					ActorRef checkLineEncodingWorker = this.getContext()
							.actorOf(new Props(new UntypedActorFactory() {
								public UntypedActor create() {
									return new CheckLineEncodingWorker(
											resultWorker);
								}
							}));
					
					checkLineEncodingWorker.tell(new LineEncodingMessage(line,
							encoding));
					

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			unhandled(message);
		}
	}
}
