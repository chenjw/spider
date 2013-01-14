package com.chenjw.spider.dt.service;

import com.chenjw.spider.dt.model.UserTokenModel;

public interface UserService {

	public UserTokenModel addInvalidUser(String token);

	public UserTokenModel updatePeriodValid(String token, int days);

	public UserTokenModel findWatchedUserByToken(String token);

	public UserTokenModel findWatchedUserById(String userId);

}
