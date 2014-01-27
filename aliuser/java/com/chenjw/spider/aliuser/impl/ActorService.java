package com.chenjw.spider.aliuser.impl;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.chenjw.dynacomponent.util.AutowireUtils;
import com.chenjw.spider.aliuser.Actor;
import com.chenjw.spider.aliuser.Sender;

public class ActorService implements Sender ,ApplicationContextAware{
    private ApplicationContext applicationContext;
    public void send(Actor actor){
        try {
            AutowireUtils.autowireBean(actor, applicationContext);
            actor.action(this);
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
