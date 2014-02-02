package com.chenjw.spider.aliuser.impl;

import java.util.Map;
import java.util.UUID;

import com.chenjw.client.HttpClient;
import com.chenjw.client.exception.HttpClientException;
import com.chenjw.spider.aliuser.ActorFactory;
import com.chenjw.spider.aliuser.BeanWriter;
import com.chenjw.spider.aliuser.annotation.ActorMethod;
import com.chenjw.spider.aliuser.constants.AliUserConstants;
import com.chenjw.spider.aliuser.container.ParserContainer;

public class LoginActor {
    private HttpClient httpClient;

    @ActorMethod
    public void start() {
        String sessionId = UUID.randomUUID().toString();
        String url = "https://login.alibaba-inc.com/ssoLogin.htm?APP_NAME=EHR&BACK_URL=https%3A%2F%2Fehr.alibaba-inc.com%2Femployee%2F%2FempInfo.htm%3FworkNo%3D2&CANCEL_CERT=true&CONTEXT_PATH=employee";

        String html;
        try {
            html = httpClient.getInPage(sessionId, url, null, "UTF-8");

            BeanWriter writer = new BeanWriter("/home/chenjw/test/output.csv");
            Map r = ParserContainer.parse(AliUserConstants.PAGE_LOGIN, html);
            String h = httpClient.postInPage(sessionId, url, r, "UTF-8");
            MonitorActor monitorActor=ActorFactory.createActor(MonitorActor.class);
            for (int i = 1; i < 300; i++) {
                monitorActor.onTaskStart();
                ActorFactory.createActor(DetailActor.class).getUserInfo(monitorActor,sessionId, i, writer);
            }
            monitorActor.onAllTaskStarted();
        } catch (HttpClientException e) {
            e.printStackTrace();
        }

    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

}
