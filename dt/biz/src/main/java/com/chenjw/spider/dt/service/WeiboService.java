package com.chenjw.spider.dt.service;

import java.util.List;

import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.model.UserModel;
import com.chenjw.spider.dt.model.UserTokenModel;

public interface WeiboService {
	public String findUserIdByToken(String token);

	public String findAuthorizeUrl();

	public String findAccessTokenByCode(String code);

	public List<TweetModel> findUserTimelineByUserId(String userId,
			String token, long sinceId);

	public List<TweetModel> findFriendsTimeline(UserTokenModel user,
			long sinceId);

	public UserModel findUserByName(String name, String token);

	public UserModel findUserByUserId(String userId, String token);

	public String[] findFriendIdsByUserId(String userId, String token);

	public String findOriginStatusUrl(UserTokenModel user, String tid);
}
