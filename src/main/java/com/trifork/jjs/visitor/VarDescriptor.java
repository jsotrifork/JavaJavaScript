package com.trifork.jjs.visitor;

import org.objectweb.asm.Type;

public class VarDescriptor {

	public final String name;
	public final Type type;

	public VarDescriptor(String name, Type type) {
		this.name = name;
		this.type = type;
	}

}
