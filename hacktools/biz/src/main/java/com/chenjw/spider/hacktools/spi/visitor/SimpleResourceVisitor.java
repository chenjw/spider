package com.chenjw.spider.hacktools.spi.visitor;

import java.io.File;

import com.chenjw.spider.hacktools.spi.ResourceVisitor;
import com.chenjw.spider.hacktools.spi.constants.VisitorActionEnum;

public class  SimpleResourceVisitor implements ResourceVisitor {

	@Override
	public VisitorActionEnum enterFolder(File folder) {
		return VisitorActionEnum.CONTINUE;
	}

	@Override
	public void leaveFolder(File folder) {
	}

	@Override
	public VisitorActionEnum enterFile(File file) {
		return VisitorActionEnum.CONTINUE;
	}

	@Override
	public void leaveFile(File file) {
	}

	@Override
	public VisitorActionEnum enterLine(String line) {
		return VisitorActionEnum.CONTINUE;
	}



	
}
