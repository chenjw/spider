package com.chenjw.spider.dt.dao;

import java.util.List;

import com.chenjw.spider.dt.dataobject.TweetDO;

public interface DeletedTweetDAO {
	public void addTweet(TweetDO tweet);

	public TweetDO findById(String tId);

	public List<TweetDO> findByUserId(String userId);
}
