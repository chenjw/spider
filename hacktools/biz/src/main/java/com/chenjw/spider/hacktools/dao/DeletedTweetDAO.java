package com.chenjw.spider.hacktools.dao;

import com.chenjw.spider.hacktools.dataobject.TweetDO;

public interface DeletedTweetDAO {
	public void addTweet(TweetDO tweet);

	public void deleteByTidAndMemberUserId(String tid, String memberUserId);

	public TweetDO findByTidAndMemberUserId(String tid, String memberUserId);


}
