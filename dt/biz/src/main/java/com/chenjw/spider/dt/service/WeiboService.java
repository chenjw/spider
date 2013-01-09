package com.chenjw.spider.dt.service;

import java.util.List;

import com.chenjw.spider.dt.model.TweetModel;

public interface WeiboService {

	public List<TweetModel> findTimelineByUserId(String userId, long sinceId);

	public String findUserIdByName(String name);

	public String[] findFriendIdsByUserId(String userId);
}
