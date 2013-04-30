package com.chenjw.spider.hacktools.spi;

import com.chenjw.spider.hacktools.model.AccountModel;

public interface  Convertor {
	public AccountModel convert(String line);
}
