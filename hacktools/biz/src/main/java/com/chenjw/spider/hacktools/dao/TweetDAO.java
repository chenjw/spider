package com.chenjw.spider.hacktools.dao;

import java.util.List;

import com.chenjw.spider.hacktools.dataobject.TweetDO;

public interface TweetDAO {
	public List<TweetDO> findByMemberUserId(String memberUserId);

	public TweetDO findByTidAndMemberUserId(String tid, String memberUserId);

	public void deleteByTidAndMemberUserId(String tid, String memberUserId);

	public void addTweet(TweetDO tweet);
}
