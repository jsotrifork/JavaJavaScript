package com.trifork.jjs.visitor;

import java.util.ArrayDeque;
import java.util.Deque;

import org.objectweb.asm.Label;

public class MethodContext {
	private boolean statementInProgress;
	private Deque<String> operandStack;
	private Deque<Frame> frameStack = new ArrayDeque<Frame>();
	private ClassContext classCtx;
	private ControlFlow controlFlow;

	@SuppressWarnings("serial")
	MethodContext(final ClassContext classCtx, boolean addComments) {
		this.classCtx = classCtx;
		controlFlow = new ControlFlow(this);
		if (addComments) {
			operandStack = new ArrayDeque<String>() {
				@Override
				public void push(String op) {
					classCtx.print("/* push(");
					classCtx.print(op);
					classCtx.print(") */");
					super.push(op);
				}
				@Override
				public String pop() {
					if (isEmpty()) {
						classCtx.print("/* pop() -> <?> */");
						return "POP ON EMPTY STACK!"; 
					} else {
						classCtx.print("/* pop() -> ");
						classCtx.print(super.getFirst());
						classCtx.print(" */");
						return super.pop();
					}
				}
				@Override
				public String getFirst() {
					if (isEmpty()) {
						classCtx.print("/* top() -> <?> */");
						return "TOP ON EMPTY STACK!"; 
					} else {
						return super.getFirst();
					}
				}
				
			};
		} else {
			operandStack = new ArrayDeque<String>();
		}

	}
	
	private void beginStatement() {
		statementInProgress = true;
	}

	private boolean readyToEndStatement() {
		return statementInProgress && topFrame() != null && operandStack.size() == topFrame().lowWaterMark;
	}

	void maybeEndStatement() {
		if (readyToEndStatement()) {
			// Each time the stack is 'empty' from a local point of view, a new statement must be started
			endStatement();
		}
	}

	public void endStatement() {
		statementInProgress = false;
		while (operandStack.size() > topFrame().lowWaterMark) {
			classCtx.println(operandStack.pop());
		}

		classCtx.out.append(";");
	}

	public void clearStatement() {
		statementInProgress = false;
	}

	void pushOperand(String op) {
		maybeEndStatement();
		operandStack.push(op);
		beginStatement();
	}
	
	String popOperand() {
		return operandStack.pop();
	}
	
	public String topOperand() {
		return operandStack.getFirst();
	}

	public boolean isOperandStackEmpty() {
		return operandStack.isEmpty();
	}
	
	public void pushFrame() {
		Frame frame = new Frame(operandStack.size());
		frameStack.push(frame);
	}
	
	public Frame topFrame() {
		if (frameStack.isEmpty()) {
			return null;
		}
		return frameStack.getFirst();
	}
	
	public Frame popFrame() {
		return frameStack.pop();
	}

	public Object cmp(String condition, Label label) {
		return controlFlow.cmp(condition, label);
	}

	public Object goTo(Label label) {
		return controlFlow.goTo(label);
	}

	public Object label(Label label) {
		return controlFlow.label(label);
	}

	public Object tableSwitch(String on, int min, int max, Label dflt, Label[] labels) {
		return controlFlow.tableSwitch(on, min, max, dflt, labels);
	}

	public Object lookupSwitch(String on, Label dflt, int[] keys, Label[] labels) {
		return controlFlow.lookupSwitch(on, dflt, keys, labels);
	}

	public Object endMethod() {
		return controlFlow.endMethod();
	}

}
