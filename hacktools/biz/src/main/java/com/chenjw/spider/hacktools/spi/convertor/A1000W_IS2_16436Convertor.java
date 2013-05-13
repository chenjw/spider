package com.chenjw.spider.hacktools.spi.convertor;

import org.apache.commons.lang.StringUtils;

import com.chenjw.spider.hacktools.model.AccountModel;
import com.chenjw.spider.hacktools.spi.Convertor;

//#145 人人网500W_16610
public class A1000W_IS2_16436Convertor implements Convertor {
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
		line = StringUtils.replace(line, "        ", " ");
		line = StringUtils.replace(line, "   ", " ");
		line = StringUtils.replace(line, "  ", " ");
		line = StringUtils.replace(line, ",,,,,,,", ",");
		line = StringUtils.replace(line, ",,,,,,", ",");
		line = StringUtils.replace(line, ",,,,,", ",");
		line = StringUtils.replace(line, ",,,,", ",");
		line = StringUtils.replace(line, ",,,", ",");
		line = StringUtils.replace(line, ",,", ",");
		line = StringUtils.replace(line, "qq,com", "qq.com");
		line = StringUtils.replace(line, "sina,com.cn", "sina.com.cn");
		line = StringUtils.replace(line, "yahoo,com.cn", "yahoo.com.cn");
		line = StringUtils.replace(line, "126,com", "126.com");
		line = StringUtils.replace(line, "163,com", "163.com");
		line = StringUtils.replace(line, "@,", "@");
		line = StringUtils.replace(line, "@.", "@");
		AccountModel account = null;

		String[] rr = line.split(",");
		account = new AccountModel();
		if (rr.length == 1) {
			rr=rr[0].split(" ");
			if (rr.length == 3) {
				if (rr[2].indexOf("@") != -1) {
					account.setEmail(rr[2]);
					account.setPassword(rr[1]);
					account.setNick(rr[0]);
				} else {
					account.setPassword(rr[1]);
					account.setNick(rr[0]);
				}
			}
			else {
				i++;
				System.out.println(i + "/"+ i1 + " " + line);
				return null;
			}

		}
		else if (rr.length == 2) {
				account.setPassword(rr[1]);
				account.setNick(rr[0]);
		}
		else if (rr.length == 3) {
			if (rr[2].indexOf("@") != -1) {
				account.setEmail(rr[2]);
				account.setPassword(rr[1]);
				account.setNick(rr[0]);
			} else {
				account.setPassword(rr[1]);
				account.setNick(rr[0]);
			}
		}
		else if (rr.length == 4) {
			if (rr[3].indexOf("@") != -1) {
				account.setEmail(rr[3]);
				if(StringUtils.isBlank(rr[2])){
					return null;
				}
				account.setPassword(rr[2]);
				account.setNick(rr[0]);
				account.setLoginId(rr[1]);
			} else {
				i++;
				System.out.println(i + "/" + i1 + " " + line);
				return null;
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
