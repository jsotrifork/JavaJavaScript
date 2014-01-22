package com.trifork.jjs.compiler;

import java.util.ArrayList;

public class Phrase {
	private ArrayList<Object> elements = new ArrayList<Object>();

	public Phrase (Object... elements) {
		for (Object elm: elements) {
			this.elements.add(elm);
		}
	}
	
	public void append(Object elm) {
		elements.add(elm);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Object elm: elements) {
			sb.append(elm);
		}
		
		return sb.toString();
	}
}
