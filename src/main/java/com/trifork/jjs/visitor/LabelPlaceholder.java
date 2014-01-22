package com.trifork.jjs.visitor;

import org.objectweb.asm.Label;

public class LabelPlaceholder {

	private Label label;
	private Object output = "";
	
	public LabelPlaceholder(Label label) {
		this.label = label;
	}

	
	@Override
	public String toString() {
		return output.toString();
	}

	public void setText(Object output) {
		this.output = output;
	}

	public Label getLabel() {
		return label;
	}
}
