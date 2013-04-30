package com.chenjw.spider.hacktools.service;

import java.util.List;

import com.chenjw.spider.hacktools.model.AccountModel;

public interface AccountSearchService {

	public List<AccountModel> find(String key);
}
