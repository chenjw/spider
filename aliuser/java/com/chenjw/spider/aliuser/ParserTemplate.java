package com.chenjw.spider.aliuser;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.chenjw.dynacomponent.core.Switchable;
import com.chenjw.parse.ParseEngine;
import com.chenjw.parser.HtmlParser;

public abstract class ParserTemplate implements HtmlParser,InitializingBean,Switchable{
    
    private ParseEngine parseEngine;
    private String id;
    @Override
    public Map<String, Object> parse(String html) {
        return parseEngine.parse(html);
    }
    
    public Map<String,Object> config() {
        return null;
    }

    public Map<String,Object> rule() {
        return null;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        parseEngine=new ParseEngine(rule(),null);
        Map<String,Object> config=config();
        id=config.get("id").toString();
    }
}
