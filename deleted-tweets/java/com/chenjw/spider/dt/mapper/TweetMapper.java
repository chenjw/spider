package com.chenjw.spider.dt.mapper;

import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSON;
import com.chenjw.spider.dt.dataobject.TweetDO;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.tools.BeanCopyUtils;

public class TweetMapper {
	public static void model2Do(TweetModel model, TweetDO dataobject) {
		String content = JSON.toJSONString(model);
		if (content != null) {
			try {
				dataobject.setContent(content.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		dataobject.setId(model.getId());
		dataobject.setUserId(model.getUser().getId());
		dataobject.setPostDate(model.getCreatedAt());

	}

	public static void do2Model(TweetDO dataobject, TweetModel model) {
		TweetModel dbModel = null;
		try {
			dbModel = JSON.parseObject(new String(dataobject.getContent(),
					"UTF-8"), TweetModel.class);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		BeanCopyUtils.copyProperties(model, dbModel);
	}
}
