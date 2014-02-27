package com.chenjw.spider.hacktools.spi.actor.worker;

import java.io.UnsupportedEncodingException;

import akka.actor.ActorRef;

import com.chenjw.spider.hacktools.spi.actor.message.FailMessage;
import com.chenjw.spider.hacktools.spi.actor.message.LineEncodingMessage;
import com.chenjw.spider.hacktools.spi.actor.message.SuccessMessage;

public class CheckLineEncodingWorker extends AbstractWorker {
	private ActorRef listener;

	public CheckLineEncodingWorker(ActorRef listener) {
		this.listener = listener;
	}

	public void doReceive(Object message) {
		///System.out.println(message);
		if (message instanceof LineEncodingMessage) {
			String line = ((LineEncodingMessage) message).line;
			String encoding = ((LineEncodingMessage) message).encoding;
			try {
				if (line != null
						&& !line.equals(new String(line.getBytes(encoding),
								encoding))) {

					listener.tell(new FailMessage(),listener);
					return;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				listener.tell(new FailMessage(),listener);
				return;
			}
			listener.tell(new SuccessMessage(),listener);
		} else {
			unhandled(message);
		}
	}
}