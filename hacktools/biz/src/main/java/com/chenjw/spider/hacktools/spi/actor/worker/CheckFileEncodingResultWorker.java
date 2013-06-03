package com.chenjw.spider.hacktools.spi.actor.worker;

import akka.actor.ActorRef;

import com.chenjw.spider.hacktools.spi.actor.message.AllStartedMessage;
import com.chenjw.spider.hacktools.spi.actor.message.FailMessage;
import com.chenjw.spider.hacktools.spi.actor.message.FileEncodingCheckResultMessage;
import com.chenjw.spider.hacktools.spi.actor.message.FileEncodingMessage;
import com.chenjw.spider.hacktools.spi.actor.message.StartMessage;
import com.chenjw.spider.hacktools.spi.actor.message.SuccessMessage;

public class CheckFileEncodingResultWorker extends AbstractWorker {

	private ActorRef listener;
	private int doingNum = 0;
	private int finishNum = 0;

	private boolean isAllSend = false;
	//
	private FileEncodingMessage fileEncodingMessage = null;

	public CheckFileEncodingResultWorker(
			FileEncodingMessage fileEncodingMessage, ActorRef listener) {
		this.listener = listener;
		this.fileEncodingMessage = fileEncodingMessage;
	}

	public void doReceive(Object message) {
		if (message instanceof StartMessage) {
			doingNum++;
		} else if (message instanceof SuccessMessage) {
			finishNum++;
			if (isAllSend && doingNum == finishNum) {
				listener.tell(new FileEncodingCheckResultMessage(
						fileEncodingMessage.file, fileEncodingMessage.index,
						fileEncodingMessage.encoding, true));
				// getContext().stop(getSelf());
			}
		} else if (message instanceof FailMessage) {
			listener.tell(new FileEncodingCheckResultMessage(
					fileEncodingMessage.file, fileEncodingMessage.index,
					fileEncodingMessage.encoding, false));
			// getContext().stop(getSelf());
		} else if (message instanceof AllStartedMessage) {
			isAllSend = true;
		} else {
			unhandled(message);
		}
	}
}
