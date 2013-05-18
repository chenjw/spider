package com.chenjw.spider.dt.constants;

import com.chenjw.spider.dt.env.BaeProvider;
import com.chenjw.spider.dt.env.CloudfoundryProvider;
import com.chenjw.spider.dt.env.EnvProvider;
import com.chenjw.spider.dt.env.LocalProvider;

public class EnvConstants {
	public static EnvProvider getEnvProvider() {
		return EnvProviderHolder.instance;
	}

	public static boolean isProductMode() {
		// return true;
		return !"local".equals(EnvProviderHolder.instance.getName());
	}

	private static class EnvProviderHolder{  
        /**  
         * 静态初始化器，由JVM来保证线程安全  
         */  
        private volatile static EnvProvider instance ;  
    	static {
    		try{
        		EnvProvider[] envs = new EnvProvider[] { new CloudfoundryProvider(),
       				 new BaeProvider(), new LocalProvider() };
	       		for (EnvProvider env : envs) {
	       			if (env.isEnable()) {
	       				instance = env;
	       				break;
	       			}
	       		}
    		}

    		catch(Throwable t){
    			instance= new BaeProvider();
    		}
    		if(instance==null){
    			instance= new BaeProvider();
    		}
    		// 初始化
    		instance.init();
    	}
    }  

	
	
	public static void main(String[] args){
		System.out.println(EnvConstants.getEnvProvider());
	}
}
