package com.chenjw.spider.dt.dao.ibatis;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.chenjw.spider.dt.constants.Constants;
import com.chenjw.spider.dt.dao.TweetDAO;
import com.chenjw.spider.dt.dataobject.TweetDO;

public class IbatisTweetDAO extends SqlMapClientDaoSupport implements TweetDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<TweetDO> findByUserId(String userId) {
		return this.getSqlMapClientTemplate().queryForList(
				"MS-SELECT-TWEET-IDS-BY-USER-ID", userId);
	}

	@Override
	public TweetDO findById(String tId) {
		return (TweetDO) this.getSqlMapClientTemplate().queryForObject(
				"MS-SELECT-TWEET-BY-TID", tId);
	}

	@Override
	public void addTweet(TweetDO tweet) {
		try {
			String str = "add tweet " + new String(tweet.getContent(), "UTF-8");
			Constants.LOGGER.info(str);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		this.getSqlMapClientTemplate().insert("MS-INSERT-TWEET", tweet);
	}

	@Override
	public void deleteById(String id) {
		this.getSqlMapClientTemplate().delete("MS-DELETE-TWEET", id);
	}

}
