package com.chenjw.spider.dt.service;

import java.util.List;

import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.model.UserTokenModel;

public interface WeiboService {

	public List<TweetModel> findUserTimelineByUserId(String userId, long sinceId);

	public List<TweetModel> findFriendsTimelineByUserId(UserTokenModel user,
			long sinceId);

	public String findUserIdByName(String name);

	public String[] findFriendIdsByUserId(String userId);
}
