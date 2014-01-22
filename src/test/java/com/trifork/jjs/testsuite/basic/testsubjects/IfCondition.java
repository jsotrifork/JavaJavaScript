package com.trifork.jjs.testsuite.basic.testsubjects;

public class IfCondition {

	public static void if1(int x, int y) {
		if (x > y) {
			System.out.print(x);
		}
	}

	public static void if2(int x, int y) {
		if (x == y) {
			System.out.print("equal");
		}
	}

	public static void ifElse(int x, int y) {
		if (x > y) {
			System.out.print(x);
		} else {
			System.out.print(y);
		}
	}
	
}
