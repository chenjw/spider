package com.chenjw.spider.hacktools;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import com.chenjw.spider.hacktools.spi.ResourceVisitor;

public class ResourceParser {
	public static void visit(File file,ResourceVisitor visitor){
		
		if(file.isDirectory()){
			visitFolder(file,visitor);
		}
		else if(file.isFile()){
			visitFile(file,visitor);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void visitFolder(File folder,ResourceVisitor visitor){
		visitor.enterFolder(folder);
		try{
			Iterator<File> iter=FileUtils.iterateFiles(folder, null, false);
			while(iter.hasNext()){
				File f=iter.next();
				if(f.isDirectory()){
					visitFolder(f,visitor);
				}
				else if(f.isFile()){
					visitFile(f,visitor);
				}
			
			}
		}
		finally{
			visitor.leaveFolder(folder);
		}
		
	}
	
	private static void visitFile(File file,ResourceVisitor visitor){
		visitor.enterFile(file);
		try{
	
			LineIterator iter=FileUtils.lineIterator(file,"GBK");
			while(iter.hasNext()){
				String line=iter.nextLine();
				visitor.enterLine(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			visitor.leaveFile(file);
		}
		
	}
}
