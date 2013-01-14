package com.chenjw.spider.dt.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.InitializingBean;

import com.chenjw.spider.dt.constants.Constants;
import com.chenjw.spider.dt.dao.DeletedTweetDAO;
import com.chenjw.spider.dt.dao.TweetDAO;
import com.chenjw.spider.dt.dao.WatchedUserDAO;
import com.chenjw.spider.dt.dataobject.TweetDO;
import com.chenjw.spider.dt.dataobject.WatchedUserDO;
import com.chenjw.spider.dt.mapper.TweetMapper;
import com.chenjw.spider.dt.mapper.UserTokenMapper;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.model.UserModel;
import com.chenjw.spider.dt.model.UserTokenModel;
import com.chenjw.spider.dt.service.DeletedTweetCheckService;
import com.chenjw.spider.dt.service.WeiboService;

public class DeletedTweetCheckServiceImpl implements DeletedTweetCheckService,
		InitializingBean {
	private WatchedUserDAO watchedUserDAO;
	private TweetDAO tweetDAO;
	private DeletedTweetDAO deletedTweetDAO;
	private WeiboService weiboService;

	@Override
	public void checkByName(String name) {
		UserModel user = weiboService.findUserByName(name, null);
		if (user != null) {
			check(user.getId());
		}
	}

	private void checkWatchedUser(UserTokenModel user) {
		// 当前时间
		Date currentDate = new Date();
		// 保留的时间
		Date retentionDate = DateUtils.addSeconds(currentDate,
				-Constants.TWEET_RETENTION_TIME);
		long minTid = 0;

		List<TweetDO> savedTweets = tweetDAO.findByMemberUserId(user
				.getUserId());
		//
		Set<String> retentionTweets = new HashSet<String>();
		for (TweetDO tweet : savedTweets) {
			// 如果已经过期
			if (retentionDate.after(tweet.getPostDate())) {
				// 删除
				tweetDAO.deleteByTidAndMemberUserId(tweet.getTid(),
						tweet.getMemberUserId());
			} else {
				retentionTweets.add(tweet.getTid());
				long longId = Long.parseLong(tweet.getTid());
				if (minTid == 0) {
					minTid = longId;
				} else {
					if (longId < minTid) {
						minTid = longId;
					}
				}
			}
		}
		UserTokenModel token = new UserTokenModel();
		token.setToken(user.getToken());
		token.setUserId(user.getUserId());
		List<TweetModel> newTweets = weiboService.findFriendsTimeline(token,
				minTid);
		// 如果等于0表示一条都没取到，否则表示取到的最小ID
		long fetchedMinTid = 0;
		for (TweetModel t : newTweets) {
			if (retentionTweets.contains(t.getId())) {
				retentionTweets.remove(t.getId());
			} else {

				if (t.getCreatedAt() == null) {
					System.out.println("createdAt = null : " + t.getHtml());
				}
				if (!retentionDate.after(t.getCreatedAt())) {
					TweetDO d = new TweetDO();
					TweetMapper.model2Do(t, d);
					d.setMemberUserId(user.getUserId());
					tweetDAO.addTweet(d);
				}
			}
			long longId = Long.parseLong(t.getId());
			if (fetchedMinTid == 0) {
				fetchedMinTid = longId;
			} else {
				if (longId < fetchedMinTid) {
					fetchedMinTid = longId;
				}
			}
		}
		for (String tid : retentionTweets) {
			TweetDO s = deletedTweetDAO.findByTidAndMemberUserId(tid,
					user.getUserId());
			if (s == null) {
				TweetDO deleted = tweetDAO.findByTidAndMemberUserId(tid,
						user.getUserId());
				if (fetchedMinTid != 0
						&& Long.parseLong(deleted.getTid()) > fetchedMinTid) {
					deletedTweetDAO.addTweet(deleted);
				}
			}
		}
	}

	@Override
	public void check(String userId) {
		WatchedUserDO user = watchedUserDAO.findWatchedUser(userId);
		if (user != null) {
			UserTokenModel model = new UserTokenModel();
			UserTokenMapper.do2Model(user, model);
			checkWatchedUser(model);
		}

	}

	@Override
	public void checkAll() {
		List<WatchedUserDO> users = watchedUserDAO.getAllWatchedUsers();
		for (WatchedUserDO user : users) {

			UserTokenModel model = new UserTokenModel();
			UserTokenMapper.do2Model(user, model);
			if (model.isValid() && !StringUtils.isBlank(model.getToken())) {
				checkWatchedUser(model);
			}

		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					DeletedTweetCheckServiceImpl.this.checkAll();
					Constants.LOGGER.info("checkAll finished!");
					try {
						Thread.sleep(Constants.WEIBO_QUERY_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}

		}).start();
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
