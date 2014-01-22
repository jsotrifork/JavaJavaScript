package com.trifork.jjs.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AddCommentsMethodVisitor extends MethodVisitor {

	private ClassContext ctx;

	public AddCommentsMethodVisitor(ClassContext ctx, MethodVisitor mv) {
		super(Opcodes.ASM4, mv);
		this.ctx = ctx;
	}

	private void print(String txt) {
		ctx.print("/* ");
		ctx.print(txt);
		ctx.print(" */\n ");
	}
	
	private void print(String txt, int opcode) {
		ctx.print("/* ");
		ctx.print(txt);
		ctx.print("(");
		ctx.print(OpcodeUtil.names[opcode]);
		ctx.print(")");
		ctx.print(" */\n ");
	}
	
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		print("visitAnnotation");
		return super.visitAnnotation(desc, visible);
	}
	
	public AnnotationVisitor visitAnnotationDefault() {
		print("visitAnnotationDefault");
		return super.visitAnnotationDefault();
	}

	public void visitAttribute(Attribute attr) {
		print("visitAttribute");
		super.visitAttribute(attr);
	}
	
	public void visitCode() {
		print("visitCode");
		super.visitCode();
	}

	public void visitEnd() {
		print("visitEnd");
		super.visitEnd();
	}

	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		print("visitFieldInsn", opcode);
		super.visitFieldInsn(opcode, owner, name, desc);
	}

	public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
		print("visitFrame");
		super.visitFrame(type, nLocal, local, nStack, stack);
	}

	public void visitIincInsn(int var, int increment) {
		print("visitIincInsn");
		super.visitIincInsn(var, increment);
	}

	public void visitInsn(int opcode) {
		print("visitInsn", opcode);
		super.visitInsn(opcode);
	}

	public void visitIntInsn(int opcode, int operand) {
		print("visitIntInsn", opcode);
		super.visitIntInsn(opcode, operand);
	}

	public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
		print("visitInvokeDynamicInsn");
		super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
	}

	public void visitJumpInsn(int opcode, Label label) {
		print("visitJumpInsn", opcode);
		super.visitJumpInsn(opcode, label);
	}

	public void visitLabel(Label label) {
		print("visitLabel");
		super.visitLabel(label);
	}

	public void visitLdcInsn(Object cst) {
		print("visitLdcInsn");
		super.visitLdcInsn(cst);
	}

	public void visitLineNumber(int line, Label start) {
//		print("visitLineNumber");
		super.visitLineNumber(line, start);
	}

	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
//		print("visitLocalVariable");
		super.visitLocalVariable(name, desc, signature, start, end, index);
	}

	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
		print("visitLookupSwitchInsn");
		super.visitLookupSwitchInsn(dflt, keys, labels);
	}

	public void visitMaxs(int maxStack, int maxLocals) {
//		print("visitMaxs");
		super.visitMaxs(maxStack, maxLocals);
	}

	public void visitMethodInsn(int opcode, String owner, String name, String desc) {
		print("visitMethodInsn", opcode);
		super.visitMethodInsn(opcode, owner, name, desc);
	}

	public void visitMultiANewArrayInsn(String desc, int dims) {
		print("visitMultiANewArrayInsn");
		super.visitMultiANewArrayInsn(desc, dims);
	}

	public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
		print("visitParameterAnnotation");
		return super.visitParameterAnnotation(parameter, desc, visible);
	}

	public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
		print("visitTableSwitchInsn");
		super.visitTableSwitchInsn(min, max, dflt, labels);
	}

	public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
		print("visitTryCatchBlock");
		super.visitTryCatchBlock(start, end, handler, type);
	}

	public void visitTypeInsn(int opcode, String type) {
		print("visitTypeInsn", opcode);
		super.visitTypeInsn(opcode, type);
	}

	public void visitVarInsn(int opcode, int var) {
		print("visitVarInsn", opcode);
		super.visitVarInsn(opcode, var);
	}
}
