package com.trifork.jjs.visitor;

import com.trifork.jjs.compiler.GeneratorStream;

public class ClassContext {
	
	private static final int INDENT_CHARS = 4;
	private static final String lineSeparator = System.getProperty("line.separator");
	GeneratorStream out;
	private int scopeLevel = 0;
	private String[] cachedIndents = { "" };
	private TypeOracle typeOracle;  
	private boolean newlinePending;
	
	
	public ClassContext(GeneratorStream generatorStream, TypeOracle typeOracle) {
		this.out = generatorStream;
		this.typeOracle = typeOracle;
	}
	
	void print(Object s) {
		if (newlinePending) {
			newlinePending = false;
			newline();
			out.append(cachedIndents[scopeLevel]);
		}
		out.append(s);
	}
	
	private void newline() {
		out.append(lineSeparator);
	}

	void println(Object s) {
		if (newlinePending) {
			newline();
		}
		print(s);
		newlinePending = true;
	}

	void println() {
		if (newlinePending) {
			newline();
		} else {
			newlinePending = true;
		}
	}
	
	void pushScope() {
		scopeLevel++;
		ensureCachedIndents();
		println(" {");
	}
	
	private void ensureCachedIndents() {
		if (scopeLevel >= cachedIndents.length) {
			String[] tmp = new String[scopeLevel + 1];
			System.arraycopy(cachedIndents, 0, tmp, 0, cachedIndents.length);
			char[] ws = new char[ scopeLevel * INDENT_CHARS ];
			for (int i = 0; i < ws.length; i++) {
				ws[i] = ' ';
			}
			
			tmp[scopeLevel] = new String(ws);
			cachedIndents = tmp;
		}

	}

	void popScope() {
		scopeLevel--;
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
