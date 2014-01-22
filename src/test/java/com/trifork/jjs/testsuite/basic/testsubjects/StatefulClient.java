package com.trifork.jjs.testsuite.basic.testsubjects;

public class StatefulClient {

	public static void doNested() {
		Stateful s1 = new Stateful();
		Stateful s2 = new Stateful();
		
		s2.setInt(7);
		s1.setStateful(s2);
		
		System.out.print(s1.getStateful().getInt());
	}
	
}
