package com.chenjw.spider.hacktools.spi.convertor;

import org.apache.commons.lang.StringUtils;

import com.chenjw.spider.hacktools.model.AccountModel;
import com.chenjw.spider.hacktools.spi.Convertor;

//#145 人人网500W_16610
public class DuowanConvertor implements Convertor {
	private int i = 0;
	private int i1 = 0;

	@Override
	public AccountModel convert(String line) {

		if (StringUtils.isBlank(line)) {
			return null;
		}
		i1++;
		line = StringUtils.replace(line, "\r", "");
		line = StringUtils.replace(line, "\t\t", " ");
		line = StringUtils.replace(line, "\t", " ");
		line = StringUtils.replace(line, "            ", " ");
		line = StringUtils.replace(line, "  ", " ");
		AccountModel account = null;

		String[] rr = line.split(" ");
		account = new AccountModel();

		if (rr.length == 5) {
			if (rr[3].indexOf("@") != -1 ) {
				if( rr[2].length()==32){
					account.setEmail(rr[3]);
					account.setMd5(rr[2]);
					account.setNick(rr[1]);
					account.setLoginId(rr[4]);
				}
				else{
					account.setEmail(rr[3]);
					account.setPassword(rr[2]);
					account.setNick(rr[1]);
					account.setLoginId(rr[4]);
				}

			} else {
				account.setPassword(rr[3]);
				account.setNick(rr[1]);
				account.setLoginId(rr[4]);
			}
		}
		else if (rr.length == 4) {
			if (rr[2].length()==32) {
				account.setMd5(rr[2]);
				account.setNick(rr[1]);
				account.setLoginId(rr[3]);
			} else {
				account.setPassword(rr[2]);
				account.setNick(rr[1]);
				account.setLoginId(rr[3]);
			}
		}
		else {
			i++;
			System.out.println(i + "/"+ i1 + " " + line);
			return null;
		}
		account.trim();
		return account;
	}

}
