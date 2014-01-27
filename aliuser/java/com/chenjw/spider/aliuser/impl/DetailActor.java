package com.chenjw.spider.aliuser.impl;

import java.io.PrintWriter;
import java.util.Map;

import com.chenjw.client.TheHttpClient;
import com.chenjw.spider.aliuser.Actor;
import com.chenjw.spider.aliuser.BeanWriter;
import com.chenjw.spider.aliuser.Sender;
import com.chenjw.spider.aliuser.constants.AliUserConstants;
import com.chenjw.spider.aliuser.container.ParserContainer;
import com.chenjw.spider.aliuser.model.AliUserInfo;
import com.chenjw.spider.location.HttpLocation;
import com.chenjw.tools.BeanCopyUtils;
import com.csvreader.CsvWriter;

public class DetailActor implements Actor{
    private TheHttpClient httpClient;
    private BeanWriter beanWriter;
    private String sessionId;
    private int workNum;
   
    public  DetailActor(String sessionId,int workNum,BeanWriter beanWriter){
        this.sessionId=sessionId;
        this.workNum=workNum;
        this.beanWriter=beanWriter;
    }
    
    public void action(Sender sender) throws Exception{
        String url = "https://ehr.alibaba-inc.com/employee/empInfo.htm?workNo=" + workNum;
        long start = System.currentTimeMillis();
        String html = httpClient.get(sessionId, url, null,
            "UTF-8");
        // 返回系统错误表示这个人已经离职了
        if(html.contains("系统错误")){
            return;
        }
        Map<String, Object> r = ParserContainer.parse(AliUserConstants.PAGE_NW, html);
        AliUserInfo user = new AliUserInfo();
        //System.out.println(html);
        BeanCopyUtils.copyProperties(user, r);
        beanWriter.write(user);
        System.out.println(url + " use "
                           + (System.currentTimeMillis() - start) + " ms");

    }

    public void setHttpClient(TheHttpClient httpClient) {
        this.httpClient = httpClient;
    }
    
   
}
