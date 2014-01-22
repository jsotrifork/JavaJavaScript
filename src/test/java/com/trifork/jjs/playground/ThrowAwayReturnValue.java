package com.trifork.jjs.playground;

public class ThrowAwayReturnValue {

	public static int returnSomething() {
		return 27;
	}
	
	public static void throwAway() {
		returnSomething();
		returnSomething();
	}
	
	public static void useResult() {
		int x = returnSomething() + returnSomething();
	}
}
