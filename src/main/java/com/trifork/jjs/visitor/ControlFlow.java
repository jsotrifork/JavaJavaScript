package com.trifork.jjs.visitor;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;

import org.objectweb.asm.Label;

public class ControlFlow {

	private IdentityHashMap<Label, LabelPlaceholder> labelMap = new IdentityHashMap<>();
	private Deque<Control> controlStack = new ArrayDeque<Control>();
	private Label currentLabel;
	private MethodContext mCtx;

	ControlFlow(MethodContext mCtx) {
		this.mCtx = mCtx;
	}
	
	public Object cmp(String condition, Label label) {
		if (isForwardLabel(label)) {
			// Forward compare-and-jump - can only be start of an if statement
			If ifCtlt = new If(condition, label);
			controlStack.push(ifCtlt);
			return ifCtlt;
		} else {
			// Backwards compare-and-jump - can be the end of a while or do-while control 
			LabelPlaceholder labelPlaceholder = labelMap.get(label);

			Control topControl = topControl();
			if (topControl != null && topControl instanceof Goto) {
				// This is a while, not a do-while control
				popControl();
				labelPlaceholder.setText("while (" + condition + ") {");
				return "}";
			} else {
				// No goto, so this is a do-while
				labelPlaceholder.setText("do {");
				return "} while (" + condition + ");";
			}
			
		}
	}

	public Object goTo(Label label) {
		// A goto can be translated from a number of constructs. Just add a generic Goto control for now..
		Goto goTo = new Goto(label);

		for (Control control: controlStack) {
			Object o = control.acceptGoto(goTo);
			if (o != null) {
				return o;
			}
		}
		
		// No control on the stack accepted this goto as part of their flow. Push a chameleon Goto control
		pushControl(goTo);

		return goTo;
	}

	public Object label(Label label) {
		LabelPlaceholder labelHolder = new LabelPlaceholder(label);

		labelMap.put(label, labelHolder);
		
		currentLabel = label;

		for (Control control: controlStack) {
			Object val = control.acceptLabel(label);
			if (val != null) {
				return val;
			}
		}

		return labelHolder;
	}

	public Object tableSwitch(String on, int min, int max, Label dflt, Label[] labels) {
		TableSwitch tableSwitch = new TableSwitch(on, min, max, dflt, labels);
		pushControl(tableSwitch);
		return tableSwitch;
	}
	
	public Object lookupSwitch(String on, Label dflt, int[] keys, Label[] labels) {
		LookupSwitch lookupSwitch = new LookupSwitch(on, dflt, keys, labels);
		pushControl(lookupSwitch);
		return lookupSwitch;
	}
	
	public Object endMethod() {
		AggregatedResult result = new AggregatedResult();

		while (topControl() != null) {
			Object o = popControl().end();
			if (o != null) {
				result.add(o);
			}
		}
		return result;
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

		abstract Object acceptLabel(Label label);

		Object end() {
			return null;
		}

		abstract Object acceptGoto(Goto goTo);

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
		Object acceptLabel(Label label) {

			if (label == this.endIfLabel) {

				// If previous instruction was a forward goto, this if has an else branch starting now..

				Control top = topControl();

				if (this == top) {
					// End of _if_ with no else branch
					return "}";
				} else {
					// Must be a goto..
					Goto gotoCtrl = (Goto) top;

					Control topControl = popControl();
					assert(this == topControl);

					pushControl(new Else(gotoCtrl.label));

					return "} else {";
				}
			}

			return null;
		}

		@Override
		Object acceptGoto(Goto goTo) {
			// Not able to verify that this goto belongs to this if control
			// before we see endIfLabel. Just allow a generic Goto control to be
			// pushed, and we will pop it when we see the label..
			return null;
		}
	}
	public class Else extends Control {

		private Label endElseLabel;

		public Else(Label label) {
			this.endElseLabel = label;
		}

		@Override
		Object acceptLabel(Label label) {
			if (label == endElseLabel) {
				assert(this == popControl());
				return "}";
			}

			return null;
		}

		@Override
		Object acceptGoto(Goto goTo) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	class While extends Control {

		public While(Label label) {
			// TODO Auto-generated constructor stub
		}

		@Override
		Object acceptLabel(Label label) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		Object acceptGoto(Goto goTo) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	class Goto extends Control {

		// A (forward) goto is a chameleon. It may be: 
		//  - end of true branch of an if-else control
		//  - a switch break
		//  - a loop break (before condition)
		//  - a loop continue (after condition)
		final Label label;
		private String text = "";

		Goto(Label label) {
			this.label = label;
		}

		@Override
		Object acceptLabel(Label label) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		Object acceptGoto(Goto goTo) {
			// TODO Auto-generated method stub
			return null;
		}

		public void setText(String text) {
			this.text = text ;
		}

		@Override
		public String toString() {
			return text;
		}

	}

	abstract class AbstractSwitch extends Control {

		private String on;
		private Label defaultLabel;
		private Label[] labels;
		private HashSet<Label> endLabelCandidates = new HashSet<>();
		
		
		public AbstractSwitch(String on, Label defaultLabel, Label[] labels) {
			this.on = on;
			this.defaultLabel = defaultLabel;
			this.labels = labels;
		}

		@Override
		Object acceptLabel(Label label) {

			if (endLabelCandidates.contains(label)) {
				handleOrphanGotos();
				Control topControl = popControl();
				assert(topControl == this);
				return "}";
			}
			
			Object caseValue = getCaseForLabel(label);
			if (caseValue != null) {
				// This label belongs to the current switch statement

				handleOrphanGotos();

				mCtx.maybeEndStatement();
				
				if (defaultLabel == label) {
					return "default:";
				} else {
					
					return "case " + caseValue + ":";
				}

			}	
			return null;
		}

		abstract Object getCaseForLabel(Label label);

		private void handleOrphanGotos() {
			// Check if control stack has an orphan goto belonging to previous case on top. 
			while (topControl() instanceof Goto) {
				Goto goTo = (Goto) topControl();
				
				// TODO: This could also be a break <LBL> referring to an outer switch or loop!
				goTo.setText(";break;");
				popControl();

				endLabelCandidates.add(goTo.label);
			}
		}

		@Override
		Object acceptGoto(Goto goTo) {
			if (goTo.label == defaultLabel) {
				// A goto to the default label can only mean one thing: This
				// switch control has no default case, thus defaultLabel ==
				// endOfSwitch. In other words, this is a switch break.
				endLabelCandidates.add(goTo.label);
				return ";break;";
			}

			return null;
		}

		@Override
		Object end() {
			handleOrphanGotos();
			return "}";
		}
		
		@Override
		public String toString() {
			return "switch (" + on + ") {";
		}

	}

	class TableSwitch extends AbstractSwitch {
		private int min;
		private int max;
		private IdentityHashMap<Label, Integer> label2Index = new IdentityHashMap<Label, Integer>();

		TableSwitch(String on, int min, int max, Label defaultLabel, Label[] labels) {
			super(on, defaultLabel, labels);
			this.min = min;
			this.max = max;
			for (int i = min; i <= max; i++) {
				this.label2Index.put(labels[i - min], i);
			}
		}

		@Override
		Object getCaseForLabel(Label label) {
			return label2Index.get(label);
		}
	}	

	class LookupSwitch extends AbstractSwitch {

		private IdentityHashMap<Label, Integer> label2Key = new IdentityHashMap<>();
		
		LookupSwitch(String on, Label defaultLabel, int[] keys, Label[] labels) {
			super(on, defaultLabel, labels);
			
			for (int i = 0; i < keys.length; i++) {
				label2Key.put(labels[i], keys[i]);
			}
		}

		@Override
		Object getCaseForLabel(Label label) {
			return label2Key.get(label);
		}
	}
}