package com.chenjw.spider.aliuser.impl;

import com.chenjw.spider.aliuser.ActorFactory;
import com.chenjw.spider.aliuser.AliUserService;

public class AliUserServiceImpl implements AliUserService {


    public void start() {
        ActorFactory.createActor(LoginActor.class).start();
        //ActorFactory.send(new StatActor());
    }


    
}
