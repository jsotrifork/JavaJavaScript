package com.trifork.jjs.testsuite.basic.testsubjects;

public class Stateful {
	private int i;
	private Stateful stateful;
	
	public void setInt(int i) {
		this.i = i;
	}
	
	public int getInt() {
		return i;
	}

	public void setStateful(Stateful stateful) {
		this.stateful = stateful;
	}
	
	public Stateful getStateful() {
		return stateful;
	}

}
