package com.chenjw.spider.hacktools.spi.convertor;

import org.apache.commons.lang.StringUtils;

import com.chenjw.spider.hacktools.model.AccountModel;
import com.chenjw.spider.hacktools.spi.Convertor;

//#145 人人网500W_16610
public class A766_16368Convertor implements Convertor {
	private int i = 0;
	private int i1 = 0;

	@Override
	public AccountModel convert(String line) {

		if (StringUtils.isBlank(line)) {
			return null;
		}
		i1++;
		line = StringUtils.replace(line, "\r", "");
		if (line.indexOf("\t") != -1) {
			line = StringUtils.replace(line, " ", "");
			line = StringUtils.replace(line, "\t", " ");
		}
		line = StringUtils.replace(line, "  ", " ");
		AccountModel account = null;

		String[] rr = line.split(" ");
		account = new AccountModel();
		if (rr.length == 2) {
			if (rr[0].indexOf("@") != -1) {
				account.setEmail(rr[0]);
				account.setPassword(rr[1]);
			} else {
				account.setNick(rr[0]);
				account.setPassword(rr[1]);
			}
		}
		else if (rr.length == 3) {
			if (rr[1].indexOf("@") != -1) {
				account.setNick(rr[0]);
				account.setEmail(rr[1]);
				account.setPassword(rr[2]);
			} else {
				i++;
				System.out.println(i + "/" + i1 + " " + line);
				return null;
			}
		}else {
			i++;
			System.out.println(i + "/" + i1 + " " + line);
			return null;
		}
		account.trim();
		return account;
	}

}
