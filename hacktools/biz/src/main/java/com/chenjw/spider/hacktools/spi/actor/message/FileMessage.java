package com.chenjw.spider.hacktools.spi.actor.message;

import java.io.File;

public class FileMessage {
	public File file;

	public FileMessage(File file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "FileMessage [file=" + file + "]";
	}
	
	
}