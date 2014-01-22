package com.trifork.jjs.visitor;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Type;

public class Frame {

	ArrayList<VarDescriptor> vars = new ArrayList<VarDescriptor>();
	public final int lowWaterMark;
	
	public Frame(int lowWaterMark) {
		this.lowWaterMark = lowWaterMark;
	}

	public List<VarDescriptor> getLocalVars() {
		return vars;
	}

	public String addLocalVar(int i, Type type) {
		String name = "var" + i;
		return addLocalVar(i, type, name);
	}

	public String addLocalVar(int i, Type type, String name) {
		VarDescriptor var = new VarDescriptor(name, type);
		
		if (vars.size() <= i) {
			for (int j = vars.size(); j < i; j++ ) {
				vars.add(j, null);
			}
			vars.add(var);
		} else {
			vars.set(i, var);
		}
		vars.set(i, var);
		
		return var.name;
	}

	public String addOrGetLocalVar(int i) {
		if (i > vars.size() - 1 || vars.get(i) == null) {
			return addLocalVar(i, null);
		} else {
			return getLocalVar(i);
		}
	}
	
	public String getLocalVar(int i) {
		if (i >= vars.size()) {
			return null;
		}
		return vars.get(i).name;
	}

}
