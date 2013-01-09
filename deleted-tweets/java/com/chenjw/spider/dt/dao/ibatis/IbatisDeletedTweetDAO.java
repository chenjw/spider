package com.chenjw.spider.dt.dao.ibatis;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.chenjw.spider.dt.constants.Constants;
import com.chenjw.spider.dt.dao.DeletedTweetDAO;
import com.chenjw.spider.dt.dataobject.TweetDO;

public class IbatisDeletedTweetDAO extends SqlMapClientDaoSupport implements
		DeletedTweetDAO {

	@Override
	public TweetDO findById(String tId) {
		return (TweetDO) this.getSqlMapClientTemplate().queryForObject(
				"MS-SELECT-DELETED-TWEET-BY-TID", tId);
	}

	public List<TweetDO> findByUserId(String userId) {
		return this.getSqlMapClientTemplate().queryForList(
				"MS-SELECT-DELETED-TWEET-BY-USER-ID", userId);

	}

	@Override
	public void addTweet(TweetDO tweet) {
		try {
			String str = "add deleted tweet "
					+ new String(tweet.getContent(), "UTF-8");
			Constants.LOGGER.info(str);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		this.getSqlMapClientTemplate().insert("MS-INSERT-DELETED-TWEET", tweet);
	}

}
