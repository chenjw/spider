package com.chenjw.spider.aliuser.impl;

import com.chenjw.spider.aliuser.AliUserService;

public class AliUserServiceImpl implements AliUserService {

    private ActorService actorService;
    public void start() {
        actorService.send(new StatActor());
    }
    public void setActorService(ActorService actorService) {
        this.actorService = actorService;
    }

    
}
