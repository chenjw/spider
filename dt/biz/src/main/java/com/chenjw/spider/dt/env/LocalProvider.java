package com.chenjw.spider.dt.env;


public class LocalProvider  extends BaseProvider {

	
	@Override
	public void init() {
		initProp("env/local/env.properties");
		initWeibo(getProperties());
	}
	
	public boolean isEnable() {
		return true;

	}

	@Override
	public String getName() {
		return "local";
	}

	@Override
	public int getInstanceCount() {
		return 1;
	}

	@Override
	public int getInstanceIndex() {
		return 0;
	}


}
