package com.chenjw.spider.hacktools.test.provider;

import java.io.File;

import com.chenjw.spider.hacktools.test.spi.Provider;
import com.chenjw.spider.hacktools.test.spi.Style;
import com.chenjw.spider.hacktools.test.spi.Tester;
import com.chenjw.spider.hacktools.test.style.AkkaStyle;
import com.chenjw.spider.hacktools.test.tester.AkkaTester;

public class AkkaProvider implements Provider {


	@Override
	public Tester createTest() {
		return new AkkaTester();
	}

	@Override
	public Style[] getStyles() {
		File fd=new File(AkkaStyle.class.getClassLoader().getResource(
				AkkaStyle.fd).getFile());
		String[] s = fd.list();
		Style[] r = new Style[s.length];
		for (int i = 0; i < s.length; i++) {
			r[i] = new AkkaStyle(s[i]);
		}
		return r;
	}

}
