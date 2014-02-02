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

public class MonitorActor {
    private long startTime;
    private int started=0;
    private int finished=0;
    private boolean allTaskStarted=false;

    
    private void checkStart(){
        if(started==0){
            startTime=System.currentTimeMillis();
        }
    }
    
    private void checkFinished(){
        if(allTaskStarted){
            if(finished==started){
                System.out.println("use "+(System.currentTimeMillis()-startTime)+" ms");
                System.out.println("started = "+started );
                System.out.println("finished = "+finished );
            }
        }
    }
    
    @ActorMethod
    public void onTaskStart() {
        checkStart();
        started++;
    }
    
    @ActorMethod
    public void onTaskFinished() {
        finished++;
        checkFinished();
    }
    
    @ActorMethod
    public void onAllTaskStarted() {
        allTaskStarted=true;
        checkFinished();
    }


}
