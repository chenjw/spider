package com.chenjw.spider.dt.dao;

import java.util.List;

import com.chenjw.spider.dt.dataobject.TweetDO;

public interface TweetDAO {
	public List<TweetDO> findByUserId(String uId);

	public TweetDO findById(String id);

	public void deleteById(String id);

	public void addTweet(TweetDO tweet);
}
