package com.chenjw.spider.dt.web.app.utils;

import java.util.Date;

import com.chenjw.tools.beancopy.util.DateUtils;

public class DateUtil {
	public static String toLocaleString(Date date, String dateFormat) {
		return DateUtils.toLocaleString(date, dateFormat);
	}
}
