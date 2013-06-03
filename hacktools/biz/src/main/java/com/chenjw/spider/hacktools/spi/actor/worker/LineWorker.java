package com.chenjw.spider.hacktools.spi.actor.worker;

import com.chenjw.spider.hacktools.spi.ResourceVisitor;
import com.chenjw.spider.hacktools.spi.actor.message.LineFinishMessage;
import com.chenjw.spider.hacktools.spi.actor.message.LineMessage;

public class LineWorker extends AbstractWorker {
	private ResourceVisitor visitor;

	public LineWorker(ResourceVisitor visitor) {
		this.visitor = visitor;
	}

	public void doReceive(Object message) {
		if (message instanceof LineMessage) {
			String line = ((LineMessage) message).line;
			visitor.enterLine(line);
			getSender().tell(new LineFinishMessage(line));

		} else {
			unhandled(message);
		}
	}
}