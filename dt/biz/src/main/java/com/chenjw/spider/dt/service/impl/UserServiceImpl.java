package com.chenjw.spider.dt.service.impl;

import com.chenjw.spider.dt.constants.UserStatusEnum;
import com.chenjw.spider.dt.dao.WatchedUserDAO;
import com.chenjw.spider.dt.dataobject.WatchedUserDO;
import com.chenjw.spider.dt.mapper.UserTokenMapper;
import com.chenjw.spider.dt.model.UserModel;
import com.chenjw.spider.dt.model.UserTokenModel;
import com.chenjw.spider.dt.service.UserService;
import com.chenjw.spider.dt.service.WeiboService;

public class UserServiceImpl implements UserService {
	private WatchedUserDAO watchedUserDAO;
	private WeiboService weiboService;

	@Override
	public UserTokenModel addInvalidUser(String token) {
		String userId = weiboService.findUserIdByToken(token);
		UserModel user = weiboService.findUserByUserId(userId, token);
		WatchedUserDO oldUser = watchedUserDAO.findWatchedUser(userId);
		UserTokenModel tokenModel = new UserTokenModel();
		if (oldUser == null) {
			WatchedUserDO newUser = new WatchedUserDO();
			newUser.setUserId(userId);
			newUser.setToken(token);
			newUser.setStatus(UserStatusEnum.FOREVER_VALID.name());
			newUser.setScreenName(user.getScreenName());
			watchedUserDAO.addWatchedUser(newUser);
			UserTokenMapper.do2Model(newUser, tokenModel);
		} else {
			// 如果token已更新，更新db中的token
			if (!token.equals(oldUser.getToken())) {
				oldUser.setToken(token);
				watchedUserDAO.updateWatchedUser(oldUser);
			}
			UserTokenMapper.do2Model(oldUser, tokenModel);
		}
		return tokenModel;

	}

	@Override
	public UserTokenModel findWatchedUserById(String userId) {
		WatchedUserDO d = watchedUserDAO.findWatchedUser(userId);
		if (d == null) {
			return null;
		} else {
			UserTokenModel m = new UserTokenModel();
			UserTokenMapper.do2Model(d, m);
			return m;
		}
	}

	@Override
	public UserTokenModel findWatchedUserByToken(String token) {
		String userId = weiboService.findUserIdByToken(token);
		WatchedUserDO d = watchedUserDAO.findWatchedUser(userId);
		if (d == null) {
			return null;
		} else {
			UserTokenModel m = new UserTokenModel();
			UserTokenMapper.do2Model(d, m);
			return m;
		}
	}

	@Override
	public UserTokenModel updatePeriodValid(String token, int days) {
		throw new java.lang.UnsupportedOperationException();
	}

	public void setWatchedUserDAO(WatchedUserDAO watchedUserDAO) {
		this.watchedUserDAO = watchedUserDAO;
	}

	public void setWeiboService(WeiboService weiboService) {
		this.weiboService = weiboService;
	}

}
