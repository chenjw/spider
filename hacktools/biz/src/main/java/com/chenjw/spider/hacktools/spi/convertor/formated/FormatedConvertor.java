package com.chenjw.spider.hacktools.spi.convertor.formated;

import org.apache.commons.lang.StringUtils;

import com.chenjw.spider.hacktools.mapper.AccountMapper;
import com.chenjw.spider.hacktools.model.AccountModel;
import com.chenjw.spider.hacktools.spi.Convertor;


/**
 * 针对已格式化的格式的转换器
 * 
 * @author Administrator
 *
 */
public class FormatedConvertor implements Convertor {

	@Override
	public AccountModel convert(String line) {
		if (StringUtils.isBlank(line)) {
			return null;
		}
		return AccountMapper.stringToModel(line);
	}

}
