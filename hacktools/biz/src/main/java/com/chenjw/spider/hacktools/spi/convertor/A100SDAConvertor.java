package com.chenjw.spider.hacktools.spi.convertor;

import org.apache.commons.lang.StringUtils;

import com.chenjw.spider.hacktools.model.AccountModel;
import com.chenjw.spider.hacktools.spi.Convertor;

//#145 人人网500W_16610
public class A100SDAConvertor implements Convertor {
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
		AccountModel account = null;

		String[] rr = line.split(" ");
		account = new AccountModel();
		if (rr.length == 3) {
			if (rr[rr.length - 1].indexOf("@") != -1) {
				account.setEmail(rr[rr.length - 1]);
				account.setPassword(rr[1]);
				account.setNick(rr[0]);
			} else {
				account.setPassword(rr[1]);
				account.setNick(rr[0]);
			}
		} else {
			i++;
			System.out.println(i + "/" + i1 + " " + line);
			return null;
		}
		account.trim();
		return account;
	}

}
