package com.chenjw.spider.dt.web.app.valve;

import static com.alibaba.citrus.turbine.util.TurbineUtil.getTurbineRunData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.service.pipeline.PipelineContext;
import com.alibaba.citrus.service.pipeline.support.AbstractValve;
import com.alibaba.citrus.turbine.TurbineConstant;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.citrus.turbine.TurbineRunDataInternal;
import com.alibaba.citrus.turbine.util.TurbineUtil;
import com.alibaba.citrus.util.StringUtil;
import com.alibaba.citrus.webx.WebxComponent;
import com.chenjw.spider.dt.constants.UserStatusEnum;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.service.PermissionService;
import com.chenjw.spider.dt.service.UserService;
import com.chenjw.spider.dt.service.WeiboService;
import com.chenjw.spider.dt.web.app.constants.DtConstants;

public class OAuthValve extends AbstractValve {

	public static final String DELIM = ".";

	@Autowired
	private WeiboService weiboService;
	@Autowired
	private UserService userService;

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private WebxComponent component;

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
		// 如果不需要登录
		String urlEscaped = getUrlEscaped();
		if (permissionService.havePermission(urlEscaped)) {
			pipelineContext.invokeNext();
			return;
		}
		// 已登录
		TokenModel userToken = (TokenModel) session
				.getAttribute(DtConstants.USER_SESSION_KEY);
		if (userToken != null) {
			TokenModel dbToken = userService.findWatchedUserById(userToken
					.getUserId());
			if (dbToken!=null && dbToken.isValid()) {

				pipelineContext.invokeNext();
				return;
			}
			// 已取消授权
			else {
				session.removeAttribute(DtConstants.USER_SESSION_KEY);
				session.removeAttribute(DtConstants.LOGIN_USER_SESSION_KEY);
			}
		}
		String signedRequest = rundata.getParameters().getString(
				"signed_request");
		if (StringUtils.isBlank(signedRequest)) {
			oauth(rundata, pipelineContext);
			return;
		}
		String token = weiboService.parseSignedRequest(signedRequest);
		if (StringUtils.isBlank(token)) {
			oauth(rundata, pipelineContext);
			return;
		}
		// token不存在表示code无效
		// if (StringUtils.isBlank(token)) {
		// errorPage(rundata, pipelineContext,
		// "当前环境为测试环境，您的帐号未开通测试权限，请联系管理员把您的帐号添加到测试帐号列表。");
		//
		// return;
		// }
//		TokenModel model = userService.findWatchedUserByToken(token);
//		// 未开通权限
//		if (model == null) {
//			// 添加一个未开通权限的用户
//			model = userService.addUser(token);
//		} else {
//			boolean changed = false;
//			if (!model.isValid()) {
//				model.setStatus(UserStatusEnum.FOREVER_VALID);
//				changed = true;
//			}
//			if (!StringUtils.equals(model.getToken(), token)) {
//				model.setToken(token);
//				changed = true;
//			}
//			if (changed) {
//				userService.updateUser(model);
//			}
//		}

		// 已开通权限
//		session.setAttribute(DtConstants.USER_SESSION_KEY, model);
//		session.setAttribute(DtConstants.LOGIN_USER_SESSION_KEY, model);
		rundata.setRedirectLocation("/");
		return;
	}

	private void oauth(TurbineRunDataInternal rundata,
			PipelineContext pipelineContext) {
		rundata.setTarget("oauth.vm");
		pipelineContext.invokeNext();
		// 重定向到登录页面
		// String redirectUrl = weiboService.findAuthorizeUrl();
		// rundata.setRedirectLocation(redirectUrl);

	}

	private void errorPage(TurbineRunDataInternal rundata,
			PipelineContext pipelineContext, String errorMessage) {
		rundata.getContext().put("errorMessage", errorMessage);
		rundata.setTarget("error.vm");
		pipelineContext.invokeNext();

	}

	private String getUrlEscaped() {
		TurbineRunData rundata = TurbineUtil.getTurbineRunData(request);
		return getScreenPermissionURI(rundata);

	}

	private String getScreenPermissionURI(TurbineRunData rundata) {
		String target = rundata.getTarget();

		target = getContent(target);
		if (target == null) {
			return null;
		}
		return component.getName() + DELIM + TurbineConstant.SCREEN_MODULE
				+ DELIM + target;
	}

	private String getContent(String target) {
		if (StringUtils.isEmpty(target)) {
			return null;
		}
		int dashIndex = target.lastIndexOf(DELIM);
		if (dashIndex != -1) {
			target = target.substring(0, dashIndex);
		}
		target = StringUtils.replace(target, "/", DELIM);
		if (target != null && target.startsWith(DELIM)) {
			target = StringUtils.substring(target, 1);
		}
		return StringUtil.toCamelCase(target);
	}

}