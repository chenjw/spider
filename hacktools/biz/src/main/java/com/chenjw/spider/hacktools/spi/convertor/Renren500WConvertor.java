package com.chenjw.spider.hacktools.spi.convertor;

import org.apache.commons.lang.StringUtils;

import com.chenjw.spider.hacktools.model.AccountModel;
import com.chenjw.spider.hacktools.spi.Convertor;
//#145 人人网500W_16610
public class Renren500WConvertor implements Convertor {
	private int i=0;
	private int i1=0;
	@Override
	public AccountModel convert(String line) {
		
		if (StringUtils.isBlank(line)) {
			return null;
		}
		i1++;
		line = StringUtils.replace(line, "\r", "");
		if(line.indexOf("\t")!=-1){
			line = StringUtils.replace(line, " ", "");
			line = StringUtils.replace(line, "\t", " ");
		}
		if (line.indexOf("@") == -1) {
			return null;
		}
		String[] strs = line.split(" ");
		AccountModel account = new AccountModel();
		String b = StringUtils.substringBefore(line, "@");
		String a = StringUtils.substringAfter(line, "@");
		String emailL=null;
		if (b.indexOf(" ") != -1) {
			emailL= StringUtils.substringAfter(b, " ");
		} else {
			emailL= b;
		}
		String emailR=StringUtils.substringBefore(a, " ");
		emailR=StringUtils.replace(emailR, ",", ".");
		emailR=StringUtils.replace(emailR, ">", ".");
		if(emailR.indexOf(".")==-1){
			if(StringUtils.startsWithIgnoreCase(emailR,"qq")){
				emailR="qq.com";
			}
			else if(StringUtils.startsWithIgnoreCase(emailR,"yahoo")){
				emailR="yahoo.com";
			}
			else if(StringUtils.startsWithIgnoreCase(emailR,"163")){
				emailR="163.com";
			}
			else if(StringUtils.startsWithIgnoreCase(emailR,"126")){
				emailR="126.com";
			}
			else if(StringUtils.startsWithIgnoreCase(emailR,"hotmail")){
				emailR="hotmail.com";
			}
			else if(StringUtils.startsWithIgnoreCase(emailR,"gmail")){
				emailR="gmail.com";
			}
			else{
				System.out.println(i+++"/"+i1+" "+line);
				return null;
			}

		}
		String email = emailL+"@" + emailR;
		email=StringUtils.replace(email, "\"", " ");
		String password = StringUtils.substringAfter(a, " ");
		password=password.trim();
		if (password.indexOf(" ") != -1) {
			password=StringUtils.substringAfterLast(password, " ");
		}
		account.setEmail(email);
		account.setPassword(password);

		account.trim();
		if(account.getPassword().indexOf(" ")!=-1){
			System.out.println(line);
		}
		return account;
	}

}
