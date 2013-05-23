package com.chenjw.spider.dt.env;


public class BaeProvider extends BaseProvider {
	

	@Override
	public void init() {
		initProp("env/bae/env.properties");
		initWeibo(getProperties());
		
	}
	

	public boolean isEnable() {
		return "bae".equals(System.getenv("USER"));
	}

	@Override
	public String getName() {
		return "bae";
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
