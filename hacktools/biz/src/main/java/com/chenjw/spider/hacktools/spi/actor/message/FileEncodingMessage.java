package com.chenjw.spider.hacktools.spi.actor.message;

import java.io.File;

public class FileEncodingMessage {
	public int index;
	public File file;
	public String encoding;

	public FileEncodingMessage(File file, int index,String encoding) {
		this.file = file;
		this.index = index;
		this.encoding = encoding;
	}

	@Override
	public String toString() {
		return "FileEncodingMessage [index=" + index + ", file=" + file
				+ ", encoding=" + encoding + "]";
	}
	
	
}