package com.chenjw.spider.dt.web.app.module.rpc;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;

import weibo4j.model.WeiboException;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.databind.File;
import com.alibaba.nonda.databind.annotation.RequestParam;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.service.WeiboService;
import com.chenjw.spider.dt.web.app.constants.DtConstants;

public class UploadRpc {

	@Autowired
	private WeiboService weiboService;

	@ResourceMapping
	public String upload(@File("pic") FileItem fileItem,
			@RequestParam(name = "status") String status, HttpSession session) {
		TokenModel userToken = (TokenModel) session
				.getAttribute(DtConstants.USER_SESSION_KEY);
		try {
			weiboService.upload(userToken, status, fileItem.get());
		} catch (WeiboException e) {

			e.printStackTrace();
		}
		return "SUCCESS";
	}
}
