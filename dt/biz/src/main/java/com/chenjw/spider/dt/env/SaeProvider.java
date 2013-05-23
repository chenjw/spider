package com.chenjw.spider.dt.env;


public class SaeProvider  extends BaseProvider {


	@Override
	public void init() {
		initProp("env/sae/env.properties");
		initWeibo(this.getProperties());
	}

	public boolean isEnable() {
		return true;
	}

	@Override
	public String getName() {
		return "sae";
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
