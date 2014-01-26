package com.trifork.jjs.visitor;

import java.util.ArrayList;

public class AggregatedResult {
	ArrayList<Object> l = new ArrayList<>();
	
	public void add(Object o) {
		l.add(o);
	}
	
	@Override 
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Object o: l) {
			sb.append(o);
		}
		
		return sb.toString();
	}
}
