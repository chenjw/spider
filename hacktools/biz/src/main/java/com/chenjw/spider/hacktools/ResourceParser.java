package com.chenjw.spider.hacktools;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import com.chenjw.spider.hacktools.spi.ResourceVisitor;

public class ResourceParser {
	public static void visit(File file, ResourceVisitor visitor) {

		if (file.isDirectory()) {
			visitFolder(file, visitor);
		} else if (file.isFile()) {
			visitFile(file, visitor);
		}
	}

	@SuppressWarnings("unchecked")
	private static void visitFolder(File folder, ResourceVisitor visitor) {
		visitor.enterFolder(folder);
		try {
			Iterator<File> iter = FileUtils.iterateFiles(folder, null, true);
			while (iter.hasNext()) {
				File f = iter.next();
				if (f.isDirectory()) {
					visitFolder(f, visitor);
				} else if (f.isFile()) {
					visitFile(f, visitor);
				}

			}
		} finally {
			visitor.leaveFolder(folder);
		}

	}

	private static boolean check(File file,String encode){
		LineIterator iter = null;
		try {
			iter = FileUtils.lineIterator(file, encode);

			while (iter.hasNext()) {
				String line = iter.nextLine();
				if (line != null
						&& !line.equals(new String(line.getBytes(encode),
								encode))) {
					return false;
				}
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	private static String checkEncoder(File file) {
		if(check(file,"UTF-8")){
			return "UTF-8";
		}
		else if(check(file,"GB18030")){
			return "GB18030";
		}
		else if(check(file,"UTF-16")){
			return "UTF-16";
		}
		else{
			throw new RuntimeException(file.getName());
		}
	}

	private static void visitFile(File file, ResourceVisitor visitor) {
		visitor.enterFile(file);
		try {
			String encode=checkEncoder(file);
			System.out.println(file.getAbsolutePath()+" "+encode);
			LineIterator iter = FileUtils.lineIterator(file,encode);
			while (iter.hasNext()) {
				String line = iter.nextLine();
				visitor.enterLine(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			visitor.leaveFile(file);
		}

	}
}
