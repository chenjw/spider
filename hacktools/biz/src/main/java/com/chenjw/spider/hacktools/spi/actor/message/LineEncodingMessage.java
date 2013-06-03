package com.chenjw.spider.hacktools.spi.actor.message;
public class LineEncodingMessage {
	public String line;
	public String encoding;

	public LineEncodingMessage(String line,String encoding) {
		this.line = line;
		this.encoding = encoding;
	}

	@Override
	public String toString() {
		return "LineEncodingMessage [line=" + line + ", encoding=" + encoding
				+ "]";
	}
	
	
}
