package com.chenjw.spider.hacktools.dao;

import java.util.List;

import com.chenjw.spider.hacktools.dataobject.WatchedUserDO;

public interface WatchedUserDAO {
	public void addWatchedUser(WatchedUserDO watchedUser);

	public void updateWatchedUser(WatchedUserDO watchedUser);

	public WatchedUserDO findWatchedUser(String userId);

	public WatchedUserDO findWatchedUserByToken(String token);
	
	public WatchedUserDO findWatchedUserByScreenName(String screenName);
	

	public List<WatchedUserDO> getAllWatchedUsers();
}
