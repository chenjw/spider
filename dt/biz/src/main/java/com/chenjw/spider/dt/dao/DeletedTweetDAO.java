package com.chenjw.spider.dt.dao;

import java.util.List;

import com.chenjw.spider.dt.dataobject.TweetDO;
import com.chenjw.spider.dt.utils.Page;

public interface DeletedTweetDAO {
	public void addTweet(TweetDO tweet);

	public void deleteByTidAndMemberUserId(String tid, String memberUserId);

	public TweetDO findByTidAndMemberUserId(String tid, String memberUserId);

	public List<TweetDO> findByMemberUserId(String memberUserId, Page page);

	public int countByMemberUserId(String memberUserId, String minSort,
			String maxSort);

	public List<TweetDO> findTopReposts();
}
