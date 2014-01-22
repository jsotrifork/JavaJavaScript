package com.trifork.jjs.testsuite.basic.testsubjects;

public class Expressions {

	public static void add(float x, float y) {
		System.out.print(x + y);
	}
	
	public static void subtract(float x, float y) {
		System.out.print(x - y);
	}
	
	public static void multiply(float x, float y) {
		System.out.print(x * y);
	}
	
	public static void divide(float x, float y) {
		System.out.print(x / y);
	}

	public static void remainder(float x, float y) {
		System.out.print(x % y);
	}
	
	public static void changeSign(float x) {
		System.out.print(-x);
	}

	public static void combine1(float x, float y, float z) {
		System.out.print(x + y * z);
	}

	public static void combine2(float x, float y, float z) {
		System.out.print(x * y + z);
	}

	public static void combine3(float x, float y, float z) {
		System.out.print((x + y) * z);
	}
}
