package com.trifork.jjs.testsuite.basic.testsubjects;

public class Base {
	
	public void foo() {
		System.out.print("base");
	}
	
	public void bar() {
		foo();
	}
}
