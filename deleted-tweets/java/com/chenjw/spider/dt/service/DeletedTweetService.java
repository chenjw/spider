package com.chenjw.spider.dt.service;

import java.util.List;

import com.chenjw.spider.dt.model.TweetModel;

public interface DeletedTweetService {
	public void addWatchedUser(String userId);

	public void check(String userId);

	public void checkAll();

	public void checkByName(String name);

	public List<TweetModel> findDeletedTweets();
}
