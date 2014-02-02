package com.chenjw.spider.aliuser;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

import com.chenjw.dynacomponent.util.AutowireUtils;
import com.chenjw.spider.aliuser.annotation.ActorMethod;

public class ActorFactory implements ApplicationContextAware {
    private static ActorFactory           INSTANCE;

    private ApplicationContext            applicationContext;

    private final Map<Class<?>, Class<?>> AKKA_ACTOR_CLASS = new ConcurrentHashMap<Class<?>, Class<?>>();

    private final ActorSystem             SYSTEM           = ActorSystem.create("mainkernel");



    private <T> T doCreateActor(Class<T> actorClass) {
        Class<?> akkaActorClass=createAkkaActorClass(actorClass);
        ActorRef actorRef=SYSTEM.actorOf(Props.create(akkaActorClass));
        Class<?> proxyClass =createActorClass(actorClass, actorRef);
        Object actor = null;
        try {
            actor = proxyClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        AutowireUtils.autowireBean(actor, applicationContext);
        return (T)actor;
    }

    public static <T> T createActor(Class<T> actorClass) {
        return INSTANCE.doCreateActor(actorClass);
    }


    private Class<?> createActorClass(final Class<?> actorClass, final ActorRef actorRef) {
        ProxyFactory e = new ProxyFactory();
        e.setSuperclass(actorClass);
        e.setFilter(new MethodFilter() {

            @Override
            public boolean isHandled(Method method) {
                if (method.isAnnotationPresent(ActorMethod.class)) {
                    return true;
                }
                return false;
            }

        });
        e.setHandler(new MethodHandler() {

            @Override
            public Object invoke(Object obj, Method method, Method method1, Object[] aobj)
                                                                                          throws Throwable {
                actorRef.tell(new MethodInvocation(obj, method1, aobj), actorRef);
                return null;
            }

        });
        return e.createClass();
        //       SYSTEM.actorOf(Props.create(proxyClass));
    }

    private Class<?> createAkkaActorClass(Class<?> actorClass) {
        Class<?> akkaActorClass = AKKA_ACTOR_CLASS.get(actorClass);
        if (akkaActorClass == null) {
            synchronized (AKKA_ACTOR_CLASS) {
                ProxyFactory e = new ProxyFactory();
                e.setSuperclass(UntypedActor.class);
                e.setFilter(new MethodFilter() {

                    @Override
                    public boolean isHandled(Method method) {
                        if ("onReceive".equals(method.getName())) {
                            return true;
                        }
                        return false;
                    }

                });
                e.setHandler(new MethodHandler() {

                    @Override
                    public Object invoke(Object obj, Method method, Method method1, Object[] aobj)
                                                                                                  throws Throwable {
                        MethodInvocation invocation = (MethodInvocation) aobj[0];
                        return invocation.getMethod().invoke(invocation.getThisObject(),
                            invocation.getArgs());
                    }

                });
                akkaActorClass = e.createClass();
                AKKA_ACTOR_CLASS.put(actorClass, akkaActorClass);
            }
        }

        //       
        return akkaActorClass;
    }



    private static class MethodInvocation {
        private Method   method;
        private Object[] args;
        private Object   thisObject;

        public MethodInvocation(Object thisObject, Method method, Object[] args) {
            this.thisObject = thisObject;
            this.method = method;
            this.args = args;
        }

        public Method getMethod() {
            return method;
        }

        public Object[] getArgs() {
            return args;
        }

        public Object getThisObject() {
            return thisObject;
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationcontext) throws BeansException {
        this.applicationContext = applicationcontext;
        INSTANCE = this;
    }

}
