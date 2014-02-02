package com.chenjw.spider.aliuser.impl;

import java.util.Map;

import com.chenjw.client.HttpClient;
import com.chenjw.client.exception.HttpClientException;
import com.chenjw.spider.aliuser.BeanWriter;
import com.chenjw.spider.aliuser.annotation.ActorMethod;
import com.chenjw.spider.aliuser.constants.AliUserConstants;
import com.chenjw.spider.aliuser.container.ParserContainer;
import com.chenjw.spider.aliuser.model.AliUserInfo;
import com.chenjw.tools.BeanCopyUtils;

public class DetailActor {
    private HttpClient httpClient;

    @ActorMethod
    public void getUserInfo(MonitorActor monitorActor, String sessionId, int workNum,
                            BeanWriter beanWriter) {
        try {
            String url = "https://ehr.alibaba-inc.com/employee/empInfo.htm?workNo=" + workNum;
            long start = System.currentTimeMillis();
            String html = null;
            try {
                html = httpClient.get(sessionId, url, null, "UTF-8");
            } catch (HttpClientException e) {
                e.printStackTrace();
            }
            // 返回系统错误表示这个人已经离职了
            if (html.contains("系统错误")) {
                return;
            }
            Map<String, Object> r = ParserContainer.parse(AliUserConstants.PAGE_NW, html);
            AliUserInfo user = new AliUserInfo();
            //System.out.println(html);
            BeanCopyUtils.copyProperties(user, r);
            beanWriter.write(user);
            System.out.println(url + " use " + (System.currentTimeMillis() - start) + " ms");
        } finally {
            monitorActor.onTaskFinished();

        }
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

}
