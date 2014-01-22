package com.trifork.jjs.testsuite.basic.testsubjects;

public class WhileConditions {

	public static void whileCondition1(int x) {
		while (x > 0) {
			System.out.print(x);
			System.out.print(",");
			x--;
		}
	}
	
}
