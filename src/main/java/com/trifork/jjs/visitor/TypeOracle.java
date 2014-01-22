package com.trifork.jjs.visitor;

public class TypeOracle {

	public String mapInternalName(String internalName) {
    	return internalName.replace('/', '_');
	}

	public String mapClassName(String internalName) {
    	return internalName.replace('.', '_');
	}
}
