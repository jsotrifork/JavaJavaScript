package com.trifork.jjs.visitor;

import com.trifork.jjs.compiler.GeneratorStream;

public class ClassContext {
	
	GeneratorStream out;
	private TypeOracle typeOracle;  
	
	
	public ClassContext(GeneratorStream generatorStream, TypeOracle typeOracle) {
		this.out = generatorStream;
		this.typeOracle = typeOracle;
	}
	
	void print(Object s) {
		out.append(s);
	}
	
	void println(Object s) {
		// Ignore attempts to add a newline..
		print(s);
	}

	void pushScope() {
		println(" {");
	}
	

	void popScope() {
		println("}");
	}
	
	public String mapClassName(String internalName) {
		return typeOracle.mapInternalName(internalName);
	}

	int varNameCount = 0;
	
	public String addLocalVar() {
		return "varName" + varNameCount++;
	}
}
