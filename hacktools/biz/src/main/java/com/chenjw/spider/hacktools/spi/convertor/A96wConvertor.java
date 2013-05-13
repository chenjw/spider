package com.chenjw.spider.hacktools.spi.convertor;

import org.apache.commons.lang.StringUtils;

import com.chenjw.spider.hacktools.model.AccountModel;
import com.chenjw.spider.hacktools.spi.Convertor;

//#145 人人网500W_16610
public class A96wConvertor implements Convertor {
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
		if (rr.length == 1) {
			String[] rrr = rr[0].split(",");
			if(rrr.length==3){
				account.setEmail(rrr[2]);
				account.setPassword(rrr[1]);
				account.setNick(rrr[0]);
			}
			else{
				rrr = rr[0].split("----");
				if(rrr.length==1){
					rrr = rrr[0].split(",");
					if(rrr.length>=2){
						if(rrr[rrr.length-1].indexOf("@")!=-1){
							account.setEmail(rrr[rrr.length-1]);
							account.setPassword(rrr[1]);
							account.setNick(rrr[0]);
						}
						else{
							if(rrr[1].length()!=0){
								account.setPassword(rrr[1]);
							}
							else if(rrr[2].length()!=0){
								account.setPassword(rrr[2]);
							}
							else{
								i++;
								System.out.println(i + "/" + i1 + " "+ line);
								return null;
							}
							account.setNick(rrr[0]);
						}
					}
					else{
						i++;
						System.out.println(i + "/" + i1 + " "+ line);
						return null;
					}
				}
				if(rrr.length==2){
					account.setEmail(rrr[0]);
					account.setPassword(rrr[1]);
				}
				else if(rrr.length>=3){
					if(rrr[rrr.length-1].indexOf("@")!=-1){
						account.setEmail(rrr[rrr.length-1]);
						account.setPassword(rrr[1]);
						account.setNick(rrr[0]);
					}
					else{
						if(rrr[1].length()!=0){
							account.setPassword(rrr[1]);
						}
						else if(rrr[2].length()!=0){
							account.setPassword(rrr[2]);
						}
						else{
							i++;
							System.out.println(i + "/" + i1 + " "+ line);
							return null;
						}
						account.setNick(rrr[0]);
					}
				}
				else{
					i++;
					System.out.println(i + "/" + i1 + " "+ line);
					return null;
				}
			}

		}
		else if (rr.length == 2) {
			if(rr[1].indexOf("@")!=-1){
				return null;
			}
			if(rr[0].indexOf("@")!=-1){
				account.setEmail(rr[0]);
				account.setPassword(rr[1]);
			}
			else{
				account.setNick(rr[0]);
				account.setPassword(rr[1]);
			}
		}
		else if (rr.length == 3) {
			if(rr[2].indexOf("@")!=-1){
				account.setEmail(rr[2]);
				account.setPassword(rr[1]);
				account.setNick(rr[0]);
			}
			else{
				account.setPassword(rr[1]);
				account.setNick(rr[0]);
			}
		}
		else if (rr.length >= 4) {
			if(rr[rr.length-1].indexOf("@")!=-1){
				account.setEmail(rr[rr.length-1]);
				account.setPassword(rr[1]);
				account.setNick(rr[0]);
			}
			else if(rr[1].indexOf("@")!=-1){
				account.setPassword(rr[1]);
				account.setNick(rr[0]);
			}
			else{
				i++;
				System.out.println(i + "/" + i1 + " "+ line);
				return null;
			}
		}
		else{
			i++;
			System.out.println(i + "/" + i1 + " "+ line);
			return null;
		}
		


		account.trim();

		return account;
	}

}
