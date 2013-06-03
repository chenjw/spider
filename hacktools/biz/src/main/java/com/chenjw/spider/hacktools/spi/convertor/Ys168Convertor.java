package com.chenjw.spider.hacktools.spi.convertor;

import org.apache.commons.lang.StringUtils;

import com.chenjw.spider.hacktools.model.AccountModel;
import com.chenjw.spider.hacktools.spi.Convertor;

//#145 人人网500W_16610
public class Ys168Convertor implements Convertor {
	private int i = 0;
	private int i1 = 0;

	@Override
	public AccountModel convert(String line) {

		if (StringUtils.isBlank(line)) {
			return null;
		}
		i1++;
		String bak=line;
		line=StringUtils.replace(line, "1. ", "");
		line=StringUtils.replace(line, "	   ", " ");
		line=StringUtils.replace(line, "	  ", " ");
		line=StringUtils.replace(line, "	 ", " ");
		
		line=StringUtils.replace(line, "\t", " ");
		AccountModel account = null;

		String[] rr = line.split(" ");
		account = new AccountModel();
		if (rr.length == 3) {
			if(rr[2].indexOf("@") != -1){
				account.setNick(rr[0]);
				account.setPassword(rr[1]);
				account.setEmail(rr[2]);
				//System.out.println(StringUtils.join(rr, " ", 0, rr.length-2)+"|||"+bak);
			} 
			else {
				account.setNick(rr[0]);
				account.setPassword(rr[1]);
			}

		}
		else {
			i++;
			System.out.println(i + "cc/" + i1 + " " + bak);
			return null;
		}
		account.trim();
		return account;
	}

	public static void main(String [] args){
		String[] strs=new String[]{"a","b","c"};
		System.out.println(StringUtils.join(strs, "，", 0, 2));
	}
}
