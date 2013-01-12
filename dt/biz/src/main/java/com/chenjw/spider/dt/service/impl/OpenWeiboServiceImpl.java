package com.chenjw.spider.dt.service.impl;

import java.util.ArrayList;
import java.util.List;

import weibo4j.Comments;
import weibo4j.Friendships;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.model.UserModel;
import com.chenjw.spider.dt.model.UserTokenModel;
import com.chenjw.spider.dt.service.Handler;
import com.chenjw.spider.dt.service.WeiboService;
import com.chenjw.spider.dt.utils.OAuthUtils;
import com.chenjw.tools.BeanCopyUtils;

public class OpenWeiboServiceImpl implements WeiboService {
	// private String token = "2.00WaGSGC0TXPFW2859bb150etwDzcB";

	private String DEFAULT_TOKEN = "2.006ONAWD0TXPFWdf8b2ae70b00kK9K";




	public String[] findFriendIdsByUserId(String userId) {
		Friendships friendshipsManager = new Friendships();
		friendshipsManager.setToken(DEFAULT_TOKEN);
		try {
			return friendshipsManager.getFriendsIdsByUid(userId, 5000, 0);
		} catch (WeiboException e) {
			e.printStackTrace();
			return null;
		}
	}



	private void readUserTimeline(Timeline timelineManager,String uid, long sinceId, Handler handler)
			throws WeiboException {

		
		long lastId = 0;
		while (true) {
			Paging page = new Paging();
			if (lastId != 0) {
				page.setMaxId(lastId);
			}
			if (sinceId != 0) {
				// 因为需要包括这一条，所以必须-1
				page.setSinceId(sinceId - 1);
			}

			page.setCount(100);
			StatusWapper status;
			status = timelineManager.getUserTimelineByUid(uid, page, 0, 0);
			if (status.getStatuses().size() == 0) {
				return;
			}
			for (Status s : status.getStatuses()) {
				handler.handleStatus(s);
				lastId = s.getIdstr() - 1;
			}
			// 如果没到count数量，表示只有一页，退出
			if (status.getStatuses().size() < page.getCount()) {
				return;
			}

		}

	}
	
	private void readFrientsTimeline(Timeline timelineManager,long sinceId, Handler handler)
			throws WeiboException {
		long lastId = 0;
		while (true) {
			Paging page = new Paging();
			if (lastId != 0) {
				page.setMaxId(lastId);
			}
			if (sinceId != 0) {
				// 因为需要包括这一条，所以必须-1
				page.setSinceId(sinceId - 1);
			}

			page.setCount(100);
			StatusWapper status;
			status = timelineManager.getFriendsTimeline(0, 0, page);
			if (status.getStatuses().size() == 0) {
				return;
			}
			for (Status s : status.getStatuses()) {
				handler.handleStatus(s);
				lastId = s.getIdstr() - 1;
			}
			// 如果没到count数量，表示只有一页，退出
			if (status.getStatuses().size() < page.getCount()) {
				return;
			}

		}

	}

	@Override
	public String findUserIdByName(String name) {
		Users usersManager=new Users();
		usersManager.setToken(DEFAULT_TOKEN);
		User user;
		try {
			
			user = usersManager.showUserByScreenName(name);
		} catch (WeiboException e) {
			e.printStackTrace();
			return null;
		}
		return user.getId();
	}

	@Override
	public List<TweetModel> findUserTimelineByUserId(final String userId,
			long sinceId) {
		Timeline timelineManager=new Timeline();
		timelineManager.setToken(DEFAULT_TOKEN);
		final List<TweetModel> result = new ArrayList<TweetModel>();
		Handler handler = new Handler() {
			@Override
			public void handleStatus(Status s) {
				TweetModel model = new TweetModel();
				BeanCopyUtils.copyProperties(model, s);
				UserModel user = new UserModel();
				BeanCopyUtils.copyProperties(user, s.getUser());
				model.setUser(user);
				result.add(model);
			}
		};
		// 目标用户
		try {
			readUserTimeline(timelineManager,userId, sinceId, handler);
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<TweetModel> findFriendsTimeline(UserTokenModel user,
			long sinceId) {
		Timeline timelineManager=new Timeline();
		timelineManager.setToken(user.getToken());
		final List<TweetModel> result = new ArrayList<TweetModel>();
		Handler handler = new Handler() {
			@Override
			public void handleStatus(Status s) {
				TweetModel model = new TweetModel();
				BeanCopyUtils.copyProperties(model, s);
				UserModel user = new UserModel();
				BeanCopyUtils.copyProperties(user, s.getUser());
				model.setUser(user);
				result.add(model);
			}
		};
		// 目标用户
		try {
			this.readFrientsTimeline(timelineManager, sinceId, handler);
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return result;
	}

}
