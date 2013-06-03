package com.chenjw.spider.hacktools.spi.actor.message;

import java.io.File;

public class FileEncodingCheckResultMessage {
	public int index;
	public File file;
	public String encoding;
	public boolean isSuccess;

	public FileEncodingCheckResultMessage(File file,int index,String encoding,boolean isSuccess) {
		this.file = file;
		this.index = index;
		this.encoding = encoding;
		this.isSuccess = isSuccess;
	}

	@Override
	public String toString() {
		return "FileEncodingCheckResultMessage [index=" + index + ", file="
				+ file + ", encoding=" + encoding + ", isSuccess=" + isSuccess
				+ "]";
	}
	
	
}