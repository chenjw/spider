package com.chenjw.spider.hacktools.spi.actor.message;

public class EncodingMessage {

	public String encoding;

	public EncodingMessage(String encoding) {
		this.encoding = encoding;
	}

	@Override
	public String toString() {
		return "EncodingMessage [encoding=" + encoding + "]";
	}
	
	
}