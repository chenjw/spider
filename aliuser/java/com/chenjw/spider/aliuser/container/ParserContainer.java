package com.chenjw.spider.aliuser.container;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.chenjw.dynacomponent.spi.ComponentContainer;
import com.chenjw.parser.HtmlParser;

public class ParserContainer implements ComponentContainer<HtmlParser>,InitializingBean{
    private Map<String, HtmlParser> htmlParserMap ;
    private static ParserContainer instance;
    
    
    public static Map<String,Object> parse(String id,String html){
        Map<String,Object> r=null;
        HtmlParser parser=instance.htmlParserMap.get(id);
        if(parser!=null){
            r=parser.parse(html);
        }
        
        System.out.println(JSON.toJSONString(r, SerializerFeature.PrettyFormat));
        return r;
    }
    
    @Override
    public Class<HtmlParser> componentType() {
        return HtmlParser.class;
    }

    @Override
    public void onReload(List<HtmlParser> args) {
        Map<String,HtmlParser> parsers=new HashMap<String,HtmlParser>();
        for(HtmlParser parser:args){
            parsers.put(parser.id(), parser);
        }
        htmlParserMap=parsers;
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        instance=this;
    }

}
