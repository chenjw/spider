package com.chenjw.parser;

import java.util.Map;

public interface HtmlParser {
    public String id();
    
	/**
	 * 从html中读取需要的数据
	 * 
	 * @param html
	 * @return
	 */
	public Map<String, Object> parse(String html);


}
