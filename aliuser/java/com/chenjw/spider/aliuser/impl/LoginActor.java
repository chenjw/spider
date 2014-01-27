package com.chenjw.spider.aliuser.impl;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.chenjw.client.TheHttpClient;
import com.chenjw.spider.aliuser.Actor;
import com.chenjw.spider.aliuser.BeanWriter;
import com.chenjw.spider.aliuser.Sender;
import com.chenjw.spider.aliuser.constants.AliUserConstants;
import com.chenjw.spider.aliuser.container.ParserContainer;
import com.chenjw.spider.aliuser.model.AliUserInfo;
import com.chenjw.spider.location.HttpLocation;
import com.chenjw.tools.BeanCopyUtils;

public class LoginActor implements Actor {
    private TheHttpClient httpClient;



    
    public void action(Sender sender) throws Exception{
        String sessionId=UUID.randomUUID().toString();
        String url = "https://login.alibaba-inc.com/ssoLogin.htm?APP_NAME=EHR&BACK_URL=https%3A%2F%2Fehr.alibaba-inc.com%2Femployee%2F%2FempInfo.htm%3FworkNo%3D2&CANCEL_CERT=true&CONTEXT_PATH=employee";

        String html = httpClient.get(sessionId, url, null,
            "UTF-8");
        BeanWriter writer=new BeanWriter("/home/chenjw/test/output.csv");
        Map r = ParserContainer.parse(AliUserConstants.PAGE_LOGIN, html);
        String h=httpClient.post(sessionId, url, r, "UTF-8");
        
        for(int i=1;i<76000;i++){
            sender.send(new DetailActor(sessionId,i, writer));
        }
       
        

    }

    public void setHttpClient(TheHttpClient httpClient) {
        this.httpClient = httpClient;
    }
    
   
}
