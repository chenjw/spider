package com.chenjw.spider.hacktools.test.provider;

import com.chenjw.spider.hacktools.test.spi.Provider;
import com.chenjw.spider.hacktools.test.spi.Style;
import com.chenjw.spider.hacktools.test.spi.Tester;
import com.chenjw.spider.hacktools.test.style.ParallelStyle;
import com.chenjw.spider.hacktools.test.tester.ParallelTester;

public class ParallelProvider implements Provider {
	private int[] nums = new int[] { 8, 30, 100, 200, 500 ,1000};

	@Override
	public Tester createTest() {
		return new ParallelTester();
	}

	@Override
	public Style[] getStyles() {
		Style[] r = new Style[nums.length];
		for (int i = 0; i < nums.length; i++) {
			r[i] = new ParallelStyle(nums[i]);
		}
		return r;
	}

}
