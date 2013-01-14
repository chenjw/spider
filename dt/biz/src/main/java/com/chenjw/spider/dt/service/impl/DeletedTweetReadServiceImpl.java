package com.chenjw.spider.dt.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.chenjw.spider.dt.dao.DeletedTweetDAO;
import com.chenjw.spider.dt.dataobject.TweetDO;
import com.chenjw.spider.dt.mapper.TweetMapper;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.service.DeletedTweetReadService;
import com.chenjw.spider.dt.utils.Page;
import com.chenjw.spider.dt.utils.PagedResult;

public class DeletedTweetReadServiceImpl implements DeletedTweetReadService {

	private DeletedTweetDAO deletedTweetDAO;

	@Override
	public PagedResult<TweetModel> findDeletedTweetsByUserId(String userId,
			Page page) {
		PagedResult<TweetModel> result = new PagedResult<TweetModel>();
		List<TweetModel> l = new ArrayList<TweetModel>();
		List<TweetDO> list = deletedTweetDAO.findByMemberUserId(userId, page);
		for (TweetDO d : list) {
			TweetModel model = new TweetModel();
			TweetMapper.do2Model(d, model);
			l.add(model);
		}
		result.setList(l);
		if (l.size() < page.getPageSize()) {
			result.setMoreNum(0);
		} else {
			int moreNum = deletedTweetDAO.countByMemberUserId(userId,
					Long.parseLong(l.get(l.size() - 1).getId()));
			result.setMoreNum(moreNum);

		}
		return result;
	}

	public void setDeletedTweetDAO(DeletedTweetDAO deletedTweetDAO) {
		this.deletedTweetDAO = deletedTweetDAO;
	}

}
