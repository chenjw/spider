package com.chenjw.spider.dt.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.InitializingBean;

import com.chenjw.spider.dt.constants.Constants;
import com.chenjw.spider.dt.dao.DeletedTweetDAO;
import com.chenjw.spider.dt.dao.TweetDAO;
import com.chenjw.spider.dt.dao.WatchedUserDAO;
import com.chenjw.spider.dt.dataobject.TweetDO;
import com.chenjw.spider.dt.dataobject.WatchedUserDO;
import com.chenjw.spider.dt.mapper.TweetMapper;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.service.DeletedTweetService;
import com.chenjw.spider.dt.service.WeiboService;

public class DeletedTweetServiceImpl implements DeletedTweetService,
		InitializingBean {
	private WatchedUserDAO watchedUserDAO;
	private TweetDAO tweetDAO;
	private DeletedTweetDAO deletedTweetDAO;
	private WeiboService weiboService;

	@Override
	public void watchFriendsByUserId(String userId) {
		String[] ids = weiboService.findFriendIdsByUserId(userId);
		if (ids != null) {
			for (String id : ids) {
				addWatchedUser(id);
			}
		}
	}

	@Override
	public List<TweetModel> findDeletedTweets() {

		List<TweetModel> result = new ArrayList<TweetModel>();
		List<WatchedUserDO> ids = watchedUserDAO.getAllWatchedUsers();
		for (WatchedUserDO id : ids) {
			List<TweetDO> list = deletedTweetDAO.findByUserId(id.getUserId());
			for (TweetDO d : list) {
				TweetModel model = new TweetModel();
				TweetMapper.do2Model(d, model);
				result.add(model);
			}
		}
		return result;
	}

	@Override
	public void addWatchedUser(String userId) {
		WatchedUserDO oldUser = watchedUserDAO.findWatchedUser(userId);
		if (oldUser == null) {
			WatchedUserDO watchedUser = new WatchedUserDO();
			watchedUser.setUserId(userId);
			watchedUserDAO.addWatchedUser(watchedUser);
		}
	}

	public String findUserIdByName(String name) {
		return weiboService.findUserIdByName(name);
	}

	@Override
	public void checkByName(String name) {
		String userId = findUserIdByName(name);
		if (userId != null) {
			check(userId);
		}
	}

	private void checkWatchedUser(WatchedUserDO user) {
		// 当前时间
		Date currentDate = new Date();
		// 保留的时间
		Date retentionDate = DateUtils.addSeconds(currentDate,
				-Constants.TWEET_RETENTION_TIME);
		long minTid = 0;
		List<TweetDO> savedTweets = tweetDAO.findByUserId(user.getUserId());
		Set<String> retentionTweets = new HashSet<String>();
		for (TweetDO tweet : savedTweets) {
			// 如果已经过期
			if (retentionDate.after(tweet.getPostDate())) {
				// 删除
				tweetDAO.deleteById(tweet.getId());
			} else {
				retentionTweets.add(String.valueOf(tweet.getId()));
				long longId = Long.parseLong(tweet.getId());
				if (minTid == 0) {
					minTid = longId;
				} else {
					if (longId < minTid) {
						minTid = longId;
					}
				}
			}
		}

		List<TweetModel> newTweets = weiboService.findUserTimelineByUserId(
				user.getUserId(), minTid);
		for (TweetModel t : newTweets) {
			if (retentionTweets.contains(t.getId())) {
				retentionTweets.remove(t.getId());
			} else {
				TweetDO d = new TweetDO();
				TweetMapper.model2Do(t, d);
				if (t.getCreatedAt() == null) {
					System.out.println("createdAt = null : " + t.getHtml());
				}
				if (!retentionDate.after(d.getPostDate())) {
					tweetDAO.addTweet(d);
				}
			}
		}
		for (String id : retentionTweets) {
			TweetDO s = deletedTweetDAO.findById(id);
			if (s == null) {
				TweetDO deleted = tweetDAO.findById(id);
				deletedTweetDAO.addTweet(deleted);
			}
		}
	}

	@Override
	public void check(String userId) {
		WatchedUserDO user = watchedUserDAO.findWatchedUser(userId);
		if (user == null) {
			this.addWatchedUser(userId);
			user = watchedUserDAO.findWatchedUser(userId);
		}
		checkWatchedUser(user);
	}

	@Override
	public void checkAll() {
		List<WatchedUserDO> ids = watchedUserDAO.getAllWatchedUsers();
		for (WatchedUserDO id : ids) {
			checkWatchedUser(id);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					DeletedTweetServiceImpl.this.checkAll();
					Constants.LOGGER.info("checkAll finished!");
					try {
						Thread.sleep(Constants.WEIBO_QUERY_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}

		});// .start();
	}

	public void setWatchedUserDAO(WatchedUserDAO watchedUserDAO) {
		this.watchedUserDAO = watchedUserDAO;
	}

	public void setTweetDAO(TweetDAO tweetDAO) {
		this.tweetDAO = tweetDAO;
	}

	public void setDeletedTweetDAO(DeletedTweetDAO deletedTweetDAO) {
		this.deletedTweetDAO = deletedTweetDAO;
	}

	public void setWeiboService(WeiboService weiboService) {
		this.weiboService = weiboService;
	}

}
