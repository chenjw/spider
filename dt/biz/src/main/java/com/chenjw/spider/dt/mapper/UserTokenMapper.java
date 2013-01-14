package com.chenjw.spider.dt.mapper;

import com.chenjw.spider.dt.dataobject.WatchedUserDO;
import com.chenjw.spider.dt.model.UserTokenModel;
import com.chenjw.tools.BeanCopyUtils;

public class UserTokenMapper {
	public static void model2Do(UserTokenModel model, WatchedUserDO dataobject) {
		BeanCopyUtils.copyProperties(dataobject, model);
	}

	public static void do2Model(WatchedUserDO dataobject, UserTokenModel model) {
		BeanCopyUtils.copyProperties(model, dataobject);
	}

}
