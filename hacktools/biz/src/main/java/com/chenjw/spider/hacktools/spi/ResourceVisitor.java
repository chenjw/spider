package com.chenjw.spider.hacktools.spi;

import java.io.File;

import com.chenjw.spider.hacktools.spi.constants.VisitorActionEnum;

public interface  ResourceVisitor {
	public VisitorActionEnum enterFolder(File folder);
	
	public void leaveFolder(File folder);
	
	public VisitorActionEnum enterFile(File file);
	
	public void leaveFile(File file);
	
	public VisitorActionEnum enterLine(String line);
	
}
