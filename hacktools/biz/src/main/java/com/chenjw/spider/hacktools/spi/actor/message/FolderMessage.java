package com.chenjw.spider.hacktools.spi.actor.message;

import java.io.File;

public class FolderMessage {
	public File file;

	public FolderMessage(File file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "FolderMessage [file=" + file + "]";
	}
	
	
}