package com.chenjw.spider.dt.web.app.valve;

import static com.alibaba.citrus.turbine.util.TurbineUtil.getTurbineRunData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.service.pipeline.PipelineContext;
import com.alibaba.citrus.service.pipeline.support.AbstractValve;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.citrus.turbine.TurbineRunDataInternal;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.service.UserService;
import com.chenjw.spider.dt.service.WeiboService;
import com.chenjw.spider.dt.web.app.constants.DtConstants;

public class OAuthValve extends AbstractValve {
	@Autowired
	private WeiboService weiboService;
	@Autowired
	private UserService userService;

	@Autowired
	private HttpServletRequest request;

	@Override
	protected void init() {
		//
	}

	private void forceLogin(TurbineRunDataInternal rundata, HttpSession session) {
		String force = rundata.getParameters().getString("force");
		if (!StringUtils.isBlank(force)) {
			TokenModel userToken = (TokenModel) session
					.getAttribute(DtConstants.USER_SESSION_KEY);
			userToken = userService.findWatchedUserById(force);
			session.setAttribute(DtConstants.USER_SESSION_KEY, userToken);
		}
	}

	public void invoke(PipelineContext pipelineContext) throws Exception {
		TurbineRunDataInternal rundata = (TurbineRunDataInternal) getTurbineRunData(request);
		HttpSession session = rundata.getRequest().getSession();
		// 强制登录
		forceLogin(rundata, session);
		// 已登录
		TokenModel userToken = (TokenModel) session
				.getAttribute(DtConstants.USER_SESSION_KEY);
		if (userToken != null) {
			pipelineContext.invokeNext();
			return;
		}
		String code = rundata.getParameters().getString("code");
		if (StringUtils.isBlank(code)) {
			oauth(rundata);
			return;
		}
		String token = weiboService.findAccessTokenByCode(code);
		// token不存在表示code无效
		if (StringUtils.isBlank(token)) {
			errorPage(rundata, "当前环境为测试环境，您的帐号未开通测试权限，请联系管理员把您的帐号添加到测试帐号列表。");
			pipelineContext.invokeNext();
			return;
		}
		TokenModel model = userService.findWatchedUserByToken(token);
		// 未开通权限
		if (model == null) {
			// 添加一个未开通权限的用户
			model = userService.addInvalidUser(token);
		} else if (!StringUtils.equals(model.getToken(), token)) {
			model.setToken(token);
			userService.updateUser(model);
		}

		// 已开通权限
		session.setAttribute(DtConstants.USER_SESSION_KEY, model);
		session.setAttribute(DtConstants.LOGIN_USER_SESSION_KEY, model);
		rundata.setRedirectLocation("/");
		return;
	}

	private void oauth(TurbineRunData rundata) {
		// 重定向到登录页面
		String redirectUrl = weiboService.findAuthorizeUrl();
		rundata.setRedirectLocation(redirectUrl);
	}

	private void errorPage(TurbineRunDataInternal rundata, String errorMessage) {
		rundata.getContext().put("errorMessage", errorMessage);
		rundata.setTarget("error.vm");

	}

}