package com.chenjw.spider.hacktools.spi.convertor;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.chenjw.parser.utils.TemplateDesc;
import com.chenjw.parser.utils.TemplateUtils;
import com.chenjw.spider.hacktools.model.AccountModel;
import com.chenjw.spider.hacktools.spi.Convertor;
import com.chenjw.tools.BeanCopyUtils;

public class ExpressionConvertor implements Convertor {
	private TemplateDesc templateDesc;

	public ExpressionConvertor(String expression) {
		this.templateDesc = TemplateUtils.build(expression);
	}

	@Override
	public AccountModel convert(String line) {
		if (StringUtils.isBlank(line)) {
			return null;
		}
		Map<String, String> result=TemplateUtils.parse(line, templateDesc);
		if(result==null){
			return null;
		}
		AccountModel account=new AccountModel();
		BeanCopyUtils.copyProperties(account, result);
		account.trim();
		return account;
	}

}
