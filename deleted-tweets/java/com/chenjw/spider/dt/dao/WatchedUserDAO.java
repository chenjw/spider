package com.chenjw.spider.dt.dao;

import java.util.List;

import com.chenjw.spider.dt.dataobject.WatchedUserDO;

public interface WatchedUserDAO {
	public void addWatchedUser(WatchedUserDO watchedUser);

	public WatchedUserDO findWatchedUser(String userId);

	public List<WatchedUserDO> getAllWatchedUsers();
}
