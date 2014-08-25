package com.chenjw.ocr;

import java.io.File;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		OCR ocr=new OCR();
		 try {
			String maybe2 = new OCR().recognizeText(new  File("/home/chenjw/test/img/output/o.jpg"), "jpg");
			System.out.println(maybe2);
			System.out.println("**********");
			//MyString str=new MyString();
			//System.out.println(str.getString(maybe2));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//		SoundServer s=new SoundServer();
//		s.playSound("E:\\111\\HOOK1.wav");
	}

}
