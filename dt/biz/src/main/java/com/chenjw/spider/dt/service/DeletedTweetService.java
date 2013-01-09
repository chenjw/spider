package com.chenjw.spider.dt.service;

import java.util.List;

import com.chenjw.spider.dt.model.TweetModel;

public interface DeletedTweetService {
	public String findUserIdByName(String name);

	public void watchFriendsByUserId(String userId);

	public void check(String userId);

	public void checkAll();

	public void checkByName(String name);

	public void addWatchedUser(String userId);

	public List<TweetModel> findDeletedTweets();
}
