package com.chenjw.spider.hacktools.spi.convertor;

import org.apache.commons.lang.StringUtils;

import com.chenjw.spider.hacktools.model.AccountModel;
import com.chenjw.spider.hacktools.spi.Convertor;

//#145 人人网500W_16610
public class Aipai1156Convertor implements Convertor {
	private int i = 0;
	private int i1 = 0;

	@Override
	public AccountModel convert(String line) {

		if (StringUtils.isBlank(line)) {
			return null;
		}
		i1++;
		if (line.indexOf("<div class='tilbg1l'>") != -1) {
			return null;
		}
		if (line.indexOf("<tr>") != -1) {
			return null;
		}
		if (line.indexOf("<td>") != -1) {
			return null;
		}
		if (line.indexOf("</div>") != -1) {
			return null;
		}
		if (line.indexOf("</tr>") != -1) {
			return null;
		}
		if (line.indexOf("</td>") != -1) {
			return null;
		}
		if (line.indexOf("</div class=") != -1) {
			return null;
		}
		if (line.indexOf("<h3 class='til'>") != -1) {
			return null;
		}
		if (line.indexOf("&emsp;") != -1) {
			return null;
		}
		if (line.indexOf("/span>") != -1) {
			return null;
		}

		line = StringUtils.replace(line, "\r", "");
		if (line.indexOf("\t") != -1) {
			line = StringUtils.replace(line, " ", "");
			line = StringUtils.replace(line, "\t", " ");
		}
		AccountModel account = null;

		String[] rr = line.split(" ");


		account = new AccountModel();
		for (String str : rr) {
			if (StringUtils.trimToNull(str) == null) {
				continue;
			}
			if (str.indexOf("@") != -1) {
				account.setEmail(str);
			} else if (str.length() == 32) {
				account.setMd5(str);
			} else {
				account.setNick(str);
			}

		}
	
		

		if (account.getEmail()==null&&account.getMd5()==null) {
			i++;

			System.out.println(i + "/" + i1 + " " + line);
			return null;
		}


		account.trim();

		return account;
	}

}
