package com.trifork.jjs.visitor;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.IdentityHashMap;

import org.objectweb.asm.Label;

public class ControlFlow {

	private IdentityHashMap<Label, LabelPlaceholder> labelMap = new IdentityHashMap<>();
	private Deque<Control> controlStack = new ArrayDeque<Control>();
	private Label currentLabel;



	public Object cmp(String condition, Label label) {
		if (isForwardLabel(label)) {
			// Forward compare-and-jump - can only be start of an if statement
			If ifCtlt = new If(condition, label);
			controlStack.push(ifCtlt);
			return ifCtlt;
		} else {
			// Backwards compare-and-jump - can be the end of a while or do-while control 
			LabelPlaceholder labelPlaceholder = labelMap.get(label);

			boolean isWhile = true; // TODO figure out how to differentiate between while and do-while
			if (isWhile) {
				labelPlaceholder.setText("while (" + condition + ") {");
				return "}";
			} else {
				labelPlaceholder.setText("do {");
				return "} while (" + condition + ");";
			}
		}
	}

	public Object goTo(Label label) {
		// A goto can be translated from a number of constructs. Just add a generic Goto control for now..
		Goto goTo = new Goto(label);
		pushControl(goTo);

		return goTo;
	}

	public Object label(Label label) {
		LabelPlaceholder labelHolder = new LabelPlaceholder(label);

		labelMap.put(label, labelHolder);
		
		currentLabel = label;

		for (Control control: controlStack) {
			Object val = control.label(label);
			if (val != null) {
				return val;
			}

		}


		return labelHolder;
	}

	private boolean isForwardLabel(Label label) {
		return !labelMap.containsKey(label);
	}

	public void pushControl(Control control) {
		controlStack.push(control);
	}

	public Control topControl() {
		if (controlStack.isEmpty()) {
			return null;
		}
		return controlStack.getFirst();
	}

	public Control popControl() {
		return controlStack.pop();
	}

	abstract class Control {

		abstract Object label(Label label);

	}

	class If extends Control {

		private String condition;
		private Label endIfLabel;

		public If(String condition, Label label) {
			this.condition = condition;
			this.endIfLabel = label;
		}

		@Override
		public String toString() {
			return "if (" + condition + ") {";
		}

		@Override
		Object label(Label label) {

			if (label == this.endIfLabel) {

				// If previous instruction was a forward goto, this if has an else branch starting now..

				Control top = topControl();

				if (this == top) {
					// End of _if_ with no else branch
					return "}";
				} else {
					// Must be a goto..
					Goto gotoCtrl = (Goto) top;

					assert(this == popControl());

					pushControl(new Else(gotoCtrl.label));

					return "} else {";
				}
			}

			return null;
		}
	}
	public class Else extends Control {

		private Label endElseLabel;

		public Else(Label label) {
			this.endElseLabel = label;
		}

		@Override
		Object label(Label label) {
			if (label == endElseLabel) {
				assert(this == popControl());
				return "}";
			}

			return null;
		}
	}

	class While extends Control {

		public While(Label label) {
			// TODO Auto-generated constructor stub
		}

		@Override
		Object label(Label label) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	class Goto extends Control {

		final Label label;

		Goto(Label label) {
			this.label = label;
		}

		@Override
		Object label(Label label) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String toString() {
			// 
			return "";
		}

	}
}