package com.chenjw.spider.hacktools.spi.actor.message;

public class LineFinishMessage {
	public String line;

	public LineFinishMessage(String line) {
		this.line = line;
	}

	@Override
	public String toString() {
		return "LineFinishMessage [line=" + line + "]";
	}
	
	
}