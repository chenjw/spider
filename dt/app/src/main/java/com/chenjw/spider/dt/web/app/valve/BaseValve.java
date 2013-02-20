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

public abstract class BaseValve extends AbstractValve {

	public static final String DELIM = ".";

	@Autowired
	protected WebxComponent component;
	@Autowired
	protected HttpServletRequest request;

	protected String getUrlEscaped() {
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