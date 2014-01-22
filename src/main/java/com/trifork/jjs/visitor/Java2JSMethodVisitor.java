package com.trifork.jjs.visitor;

import java.util.ArrayList;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;

import com.trifork.jjs.compiler.Phrase;


public class Java2JSMethodVisitor extends MethodVisitor {

	private ClassContext cCtx;
	private Type returnType;
	private boolean newInstructionPending;
	private boolean isStaticMethod;
	private Label currentLabel;
	private MethodContext mCtx;
	
	public Java2JSMethodVisitor(ClassContext classCtx, String jsClassName, int access, String name, String desc, String signature, String[] exceptions, boolean addComments) {
		super(Opcodes.ASM4);
		this.cCtx = classCtx;

		mCtx = new MethodContext(classCtx, addComments);
		
		isStaticMethod = (access & Opcodes.ACC_STATIC) != 0;
		
		mCtx.pushFrame();
		Frame frame = mCtx.topFrame();
		
		Type[] argumentTypes = Type.getArgumentTypes(desc);
		
		int realArgOffset;
		if (isStaticMethod) {
			realArgOffset = 0;
		} else {
			realArgOffset = 1;
			Type[] tmp = new Type[argumentTypes.length + 1];
			System.arraycopy(argumentTypes, 0, tmp, 0, argumentTypes.length);
			argumentTypes = tmp;
			
			argumentTypes[0] = Type.getType(name); // a.k.a. 'this'
			frame.addLocalVar(0, argumentTypes[0], "this");
		}
		
		for (int i = realArgOffset; i < argumentTypes.length; i++) {
			frame.addLocalVar(i, argumentTypes[i]);
		}
		
		returnType = Type.getReturnType(desc);
		
		if ("<init>".equals(name)) {
			classCtx.print("function ");
			classCtx.print(jsClassName);
		} else if ((access & Opcodes.ACC_STATIC) != 0) {
			// Static method
			classCtx.print(jsClassName);
			classCtx.print(".");
			classCtx.print(name);
			classCtx.print(" = function ");
		} else {
			classCtx.print(jsClassName);
			classCtx.print(".prototype.");
			classCtx.print(name);
			classCtx.print(" = function ");
		}
		classCtx.print("(");

		for (int i = realArgOffset; i < frame.getLocalVars().size(); i++) {
			VarDescriptor var = frame.getLocalVars().get(i);
			if (i > realArgOffset) {
				classCtx.print(",");
			}
			classCtx.print(var.name);
		}
		classCtx.print(")");
		classCtx.pushScope();

	}

	private String[] assignParamNames(String signature) {
		if (signature == null) {
			return new String[0];
		}
		
		final ArrayList<String> parameters = new ArrayList<String>();

		SignatureReader signatureReader = new SignatureReader(signature);
		
		signatureReader.accept(new SignatureVisitor(Opcodes.ASM4) {
			private boolean paramsDone = false;
			@Override
			public void visitBaseType(char descriptor) {
				if (!paramsDone) {
					parameters.add("param" + parameters.size());
				}
			}
			@Override
		    public SignatureVisitor visitReturnType() {
				paramsDone = true;
				return this;
		    }

		});
		
		String[] result = new String[parameters.size()];
		parameters.toArray(result);
		return result;
	}

	@Override
	public AnnotationVisitor visitAnnotationDefault() {
		if (mv != null) {
			return mv.visitAnnotationDefault();
		}
		return null;
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		if (mv != null) {
			return mv.visitAnnotation(desc, visible);
		}
		return null;
	}

	@Override
	public AnnotationVisitor visitParameterAnnotation(int parameter,
			String desc, boolean visible) {
		if (mv != null) {
			return mv.visitParameterAnnotation(parameter, desc, visible);
		}
		return null;
	}

	@Override
	public void visitAttribute(Attribute attr) {
		if (mv != null) {
			mv.visitAttribute(attr);
		}
	}

	@Override
	public void visitCode() {
	}

	@Override
	public void visitFrame(int type, int nLocal, Object[] local, int nStack,
			Object[] stack) {

		// local represents types for locals: Internal class name or
		// Opcodes.TOP, Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.LONG,
		// Opcodes.DOUBLE,Opcodes.NULL or Opcodes.UNINITIALIZED_THIS
		
		switch (type) {
		case Opcodes.F_NEW: 
		case Opcodes.F_SAME: 
		case Opcodes.F_SAME1: 
		case Opcodes.F_APPEND: 
		case Opcodes.F_CHOP: 
		case Opcodes.F_FULL: 
		}
	}

	@Override
	public void visitInsn(int opcode) {
		currentLabel = null;
		
		switch (opcode) {
		case Opcodes.NOP:
			break;
		case Opcodes.ACONST_NULL:
			mCtx.pushOperand("null");
			break;
		case Opcodes.ICONST_M1: 
			mCtx.pushOperand("-1");
			break;
		case Opcodes.ICONST_0: 
			mCtx.pushOperand("0");
			break;
		case Opcodes.ICONST_1:
			mCtx.pushOperand("1");
			break;
		case Opcodes.ICONST_2: 
			mCtx.pushOperand("2");
			break;
		case Opcodes.ICONST_3: 
			mCtx.pushOperand("3");
			break;
		case Opcodes.ICONST_4: 
			mCtx.pushOperand("4");
			break;
		case Opcodes.ICONST_5: 
			mCtx.pushOperand("5");
			break;
		case Opcodes.LCONST_0: 
			mCtx.pushOperand("0");
			break;
		case Opcodes.LCONST_1:
			mCtx.pushOperand("1");
			break;
		case Opcodes.FCONST_0: 
			mCtx.pushOperand("0.0");
			break;
		case Opcodes.FCONST_1: 
			mCtx.pushOperand("1.0");
			break;
		case Opcodes.FCONST_2: 
			mCtx.pushOperand("2.0");
			break;
		case Opcodes.DCONST_0: 
			mCtx.pushOperand("0.0");
			break;
		case Opcodes.DCONST_1: 
			mCtx.pushOperand("1.0");
			break;
		case Opcodes.IALOAD: 
		case Opcodes.LALOAD: 
		case Opcodes.FALOAD: 
		case Opcodes.DALOAD: 
		case Opcodes.AALOAD: 
		case Opcodes.BALOAD: 
		case Opcodes.CALOAD: 
		case Opcodes.SALOAD: {
			String index = mCtx.popOperand();
			cCtx.print(mCtx.popOperand());
			cCtx.print("[");
			cCtx.print(index);
			cCtx.print("]");
			break;
		}
		case Opcodes.IASTORE: 
		case Opcodes.LASTORE: 
		case Opcodes.FASTORE: 
		case Opcodes.DASTORE: 
		case Opcodes.AASTORE: 
		case Opcodes.BASTORE: 
		case Opcodes.CASTORE:
		case Opcodes.SASTORE: {
			String value = mCtx.popOperand();
			String index = mCtx.popOperand();
			cCtx.print(mCtx.popOperand());
			cCtx.print("[");
			cCtx.print(index);
			cCtx.print("]");
			cCtx.print("=");
			cCtx.print(value);
			break;
		}
		case Opcodes.POP: 
			mCtx.popOperand();
			break;
		case Opcodes.POP2: 
			break;
		case Opcodes.DUP: 
			mCtx.pushOperand(mCtx.topOperand());
			break;
		case Opcodes.DUP_X1:
			break;
		case Opcodes.DUP_X2: 
			break;
		case Opcodes.DUP2: 
			break;
		case Opcodes.DUP2_X1:
		case Opcodes.DUP2_X2: 
			break;
		case Opcodes.SWAP: 
			break;
		case Opcodes.IADD: 
		case Opcodes.LADD: 
		case Opcodes.FADD: 
		case Opcodes.DADD: 
			String r = mCtx.popOperand();
			String l = mCtx.popOperand();
			mCtx.pushOperand("(" + l + "+" + r + ")");
			break;
		case Opcodes.ISUB: 
		case Opcodes.LSUB: 
		case Opcodes.FSUB: 
		case Opcodes.DSUB:
			r = mCtx.popOperand();
			l = mCtx.popOperand();
			mCtx.pushOperand("(" + l + "-" + r + ")");
			break;
		case Opcodes.IMUL: 
		case Opcodes.LMUL: 
		case Opcodes.FMUL: 
		case Opcodes.DMUL: 
			r = mCtx.popOperand();
			l = mCtx.popOperand();
			mCtx.pushOperand("(" + l + "*" + r + ")");
			break;
		case Opcodes.IDIV: 
		case Opcodes.LDIV: 
		case Opcodes.FDIV: 
		case Opcodes.DDIV: 
			r = mCtx.popOperand();
			l = mCtx.popOperand();
			mCtx.pushOperand("(" + l + "/" + r + ")");
			break;
		case Opcodes.IREM: 
		case Opcodes.LREM:
		case Opcodes.FREM: 
		case Opcodes.DREM: 
			r = mCtx.popOperand();
			l = mCtx.popOperand();
			mCtx.pushOperand("(" + l + "%" + r + ")");
			break;
		case Opcodes.INEG: 
		case Opcodes.LNEG: 
		case Opcodes.FNEG: 
		case Opcodes.DNEG: 
			mCtx.pushOperand("(-" + mCtx.popOperand() + ")");
			break;
		case Opcodes.ISHL: 
		case Opcodes.LSHL: 
			r = mCtx.popOperand();
			l = mCtx.popOperand();
			mCtx.pushOperand("(" + l + "<<" + r + ")");
			break;
		case Opcodes.ISHR: 
		case Opcodes.LSHR:
		case Opcodes.IUSHR: 
		case Opcodes.LUSHR: 
			r = mCtx.popOperand();
			l = mCtx.popOperand();
			mCtx.pushOperand("(" + l + ">>" + r + ")");
			break;
		case Opcodes.IAND: 
		case Opcodes.LAND: 
			r = mCtx.popOperand();
			l = mCtx.popOperand();
			mCtx.pushOperand("(" + l + "&&" + r + ")");
			break;
		case Opcodes.IOR: 
		case Opcodes.LOR: 
			r = mCtx.popOperand();
			l = mCtx.popOperand();
			mCtx.pushOperand("(" + l + "||" + r + ")");
			break;
		case Opcodes.IXOR: 
		case Opcodes.LXOR: 
			r = mCtx.popOperand();
			l = mCtx.popOperand();
			mCtx.pushOperand("(" + l + "^" + r + ")");
			break;
		case Opcodes.I2L: 
		case Opcodes.I2F: 
		case Opcodes.I2D:
		case Opcodes.L2I: 
		case Opcodes.L2F: 
		case Opcodes.L2D: 
		case Opcodes.F2I: 
		case Opcodes.F2L: 
		case Opcodes.F2D: 
		case Opcodes.D2I: 
		case Opcodes.D2L: 
		case Opcodes.D2F: 
		case Opcodes.I2B: 
		case Opcodes.I2C: 
		case Opcodes.I2S:
			// Ignore cast operators
			break;
		case Opcodes.LCMP: 
			r = mCtx.popOperand();
			l = mCtx.popOperand();
			mCtx.pushOperand("(" + l + "===" + r + ")");
			break;
		case Opcodes.FCMPL: 
		case Opcodes.DCMPL: 
			r = mCtx.popOperand();
			l = mCtx.popOperand();
			mCtx.pushOperand("(" + l + "<" + r + ")");
			break;
		case Opcodes.FCMPG: 
		case Opcodes.DCMPG: 
			r = mCtx.popOperand();
			l = mCtx.popOperand();
			mCtx.pushOperand("(" + l + ">" + r + ")");
			break;
		case Opcodes.IRETURN: 
		case Opcodes.LRETURN: 
		case Opcodes.FRETURN:
		case Opcodes.DRETURN: 
		case Opcodes.ARETURN: 
		case Opcodes.RETURN: 
			if (Type.VOID_TYPE.equals(returnType)) {
			} else {
				cCtx.print("return ");
				cCtx.print(mCtx.popOperand());
			}
			break;
		case Opcodes.ARRAYLENGTH: 
			break;
		case Opcodes.ATHROW: 
			break;
		case Opcodes.MONITORENTER:
		case Opcodes. MONITOREXIT:
			// Ignore
			break;
		}
	}

	@Override
	public void visitIntInsn(int opcode, int operand) {
		currentLabel = null;
		
		switch (opcode) {
		case Opcodes.BIPUSH:
		case Opcodes.SIPUSH:
			mCtx.pushOperand(String.valueOf(operand));
			break;
		case Opcodes.NEWARRAY:
			break;
		}
	}

	@Override
	public void visitVarInsn(int opcode, int var) {
		currentLabel = null;

		switch (opcode) {
		case Opcodes.ILOAD: 
		case Opcodes.LLOAD: 
		case Opcodes.FLOAD:  
		case Opcodes.DLOAD:  
		case Opcodes.ALOAD: 
			mCtx.pushOperand(mCtx.topFrame().getLocalVars().get(var).name);
		break;
		case Opcodes.ISTORE: 
		case Opcodes.LSTORE:  
		case Opcodes.FSTORE:  
		case Opcodes.DSTORE:  
		case Opcodes.ASTORE:
			String localVar = mCtx.topFrame().getLocalVar(var);
			if (localVar == null) {
				cCtx.print("var ");
				localVar = mCtx.topFrame().addLocalVar(var, null);
			}
			cCtx.print(localVar);
			cCtx.print(" = ");
			cCtx.print(mCtx.popOperand());
			//ctx.endStmt();
			break;
		case Opcodes.RET:
		}
	}

	@Override
	public void visitTypeInsn(int opcode, String type) {
		currentLabel = null;

		switch (opcode) {
		case Opcodes.NEW:
			String jsTypeName = cCtx.mapClassName(type);
//			ctx.print(jsTypeName);
//			ctx.print(" ");
//			String varName = ctx.addLocalVar();
//			ctx.print(varName);
//			ctx.print(" = ");
//			ctx.print("new ");
//			ctx.pushOperand(varName);
			mCtx.pushOperand("Dummy ref to " + jsTypeName);
			newInstructionPending = true;
			break;
		case Opcodes.ANEWARRAY:
		case Opcodes.CHECKCAST:
		case Opcodes.INSTANCEOF:
			
		}
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		currentLabel = null;

		switch (opcode) {
		case Opcodes.GETSTATIC:
			mCtx.pushOperand(cCtx.mapClassName(owner) + "." + name);
			break;
		case Opcodes.PUTSTATIC:
			break;
		case Opcodes.GETFIELD:
			String object = mCtx.popOperand();
			mCtx.pushOperand(object + "." + name);
			break;
		case Opcodes.PUTFIELD:
			String value = mCtx.popOperand();
			cCtx.print(mCtx.popOperand()); // object reference
			cCtx.print(".");
			cCtx.print(name);
			cCtx.print("=");
			cCtx.print(value);
			//ctx.endStmt();
			break;
		}
	}
	
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc) {
		currentLabel = null;

		Type[] argumentTypes = Type.getArgumentTypes(desc);
		Type returnType = Type.getReturnType(desc);
		boolean pushResult = false;
		String[] args = new String[argumentTypes.length + 1];
		
		for (int i = argumentTypes.length - 1; i >= 0; i--) {
			// Make room for "this" as the first argument
			args[i + 1] = (mCtx.popOperand());
		}

		int argsOffset = 1;
		
		Phrase phrase = new Phrase();
		
		switch (opcode) {
		case Opcodes.INVOKEVIRTUAL:
			phrase.append(mCtx.popOperand());
			phrase.append(".");
			phrase.append(name);
			break;
		case Opcodes.INVOKESPECIAL:
			if ("<init>".equals(name)) {

				// Differentiate between constructors invoking super and "new X()" invocations?
				if (newInstructionPending) {
					newInstructionPending = false;
					mCtx.popOperand();  // pop dummy ref from new operation ..
					mCtx.popOperand();  // .. twice, since javac copies 'this' ref with DUP - one for constructor and one for result)
					phrase.append("new " + cCtx.mapClassName(owner));
					pushResult = true;
				} else {
					
					if ("java/lang/Object".equals(owner)) {
						// Do we want to explicitly let all classes inherit from java.lang.Object?
						mCtx.popOperand();
						return;
					} else {
						phrase.append("_super.call");
						args[0] = mCtx.popOperand();
						argsOffset = 0;
					}
				}

			} else {
				// Search for method through superclass chain
				phrase.append("_super.call(this, ");
				phrase.append(mCtx.popOperand());
				phrase.append(".");
				phrase.append(name);
				
			}
			break;
		case Opcodes.INVOKESTATIC:
			phrase.append(cCtx.mapClassName(owner));
			phrase.append(".");
			phrase.append(name);
			break;
		case Opcodes.INVOKEINTERFACE:
			break;
		}

		phrase.append("(");

		for (int i = argsOffset; i < args.length; i++) {
			if (i > argsOffset) {
				phrase.append(",");
			}
			phrase.append(args[i]);
		}
		
		phrase.append(")");

		if (Type.VOID_TYPE.equals(returnType) && !pushResult) {
			cCtx.print(phrase.toString());
		} else {
			mCtx.pushOperand(phrase.toString());
		}

	}

	@Override
	public void visitInvokeDynamicInsn(String name, String desc, Handle bsm,
			Object... bsmArgs) {
		currentLabel = null;
	}

	@Override
	public void visitJumpInsn(int opcode, Label label) {
		currentLabel = null;

		// Note: The inversion of conditionals is deliberate (javac does the same)
		
		switch (opcode) {
		case Opcodes.IFEQ:
			String lhs = mCtx.popOperand();
			cCtx.print(mCtx.cmp(lhs + "==0", label));
			break;
		case Opcodes.IF_ICMPEQ:
		case Opcodes.IF_ACMPEQ:
			String rhs = mCtx.popOperand();
			lhs = mCtx.popOperand();
			cCtx.print(mCtx.cmp(lhs + "!=" + rhs, label));
			break;
		case Opcodes.IFNE:
			lhs = mCtx.popOperand();
			cCtx.print(mCtx.cmp(lhs + "!=0", label));
			break;
		case Opcodes.IF_ICMPNE:
		case Opcodes.IF_ACMPNE:
			rhs = mCtx.popOperand();
			lhs = mCtx.popOperand();
			cCtx.print(mCtx.cmp(lhs + "==" + rhs, label));
			break;

		case Opcodes.IFLT:
			lhs = mCtx.popOperand();
			cCtx.print(mCtx.cmp(lhs + "<0", label));
			break;
		case Opcodes.IF_ICMPLT:
			rhs = mCtx.popOperand();
			lhs = mCtx.popOperand();
			cCtx.print(mCtx.cmp(lhs + ">=" + rhs, label));
			break;
		case Opcodes.IFGE:
			lhs = mCtx.popOperand();
			cCtx.print(mCtx.cmp(lhs + ">=0", label));
			break;
		case Opcodes.IF_ICMPGE:
			rhs = mCtx.popOperand();
			lhs = mCtx.popOperand();
			cCtx.print(mCtx.cmp(lhs + "<" + rhs, label));
			break;
		case Opcodes.IFGT:
			lhs = mCtx.popOperand();
			cCtx.print(mCtx.cmp(lhs + ">0" , label));
			break;
		case Opcodes.IF_ICMPGT:
			rhs = mCtx.popOperand();
			lhs = mCtx.popOperand();
			cCtx.print(mCtx.cmp(lhs + "<=" + rhs, label));
			break;
		case Opcodes.IFLE:
			lhs = mCtx.popOperand();
			cCtx.print(mCtx.cmp(lhs + "<=0", label));
			break;
		case Opcodes.IF_ICMPLE:
			rhs = mCtx.popOperand();
			lhs = mCtx.popOperand();
			cCtx.print(mCtx.cmp(lhs + ">" + rhs, label));
			break;
		
		case Opcodes.GOTO:
			cCtx.print(mCtx.goTo(label));
			break;
		case Opcodes.JSR:
			break;
			
		case Opcodes.IFNULL:
			cCtx.print(mCtx.cmp("if (" + mCtx.popOperand() + "!= null" + ")", label));
			break;
		case Opcodes.IFNONNULL:	
			cCtx.print(mCtx.cmp("if (" + mCtx.popOperand() + "== null" + ")", label));
			break;
		}
	}
	@Override
	public void visitLabel(Label label) {
		cCtx.print(mCtx.label(label));
		
		currentLabel = label;
	}

	@Override
	public void visitLdcInsn(Object cst) {
		currentLabel = null;
		 if (cst instanceof Integer) {
			 mCtx.pushOperand(cst.toString());
		 } else if (cst instanceof Float) {
			 mCtx.pushOperand(cst.toString());
		 } else if (cst instanceof Long) {
			 mCtx.pushOperand(cst.toString());
		 } else if (cst instanceof Double) {
			 mCtx.pushOperand(cst.toString());
		 } else if (cst instanceof String) {
			 mCtx.pushOperand("\"" + cst + "\"");
		 } else if (cst instanceof Type) {
		     int sort = ((Type) cst).getSort();
		     if (sort == Type.OBJECT) {
		         // ...
		     } else if (sort == Type.ARRAY) {
		         // ...
		     } else if (sort == Type.METHOD) {
		         // ...
		     } else {
		         // throw an exception
		     }
		 } else if (cst instanceof Handle) {
		     // ...
		 } else {
		     // throw an exception
		 }
	}

	@Override
	public void visitIincInsn(int var, int increment) {
		currentLabel = null;

		if (mCtx.isOperandStackEmpty()) {
			// Empty operand stack means that we are not in the middle of an
			// expression (which would be meaningful for var++/var--).
			// Although iinc does not affect the runtime stack, do a dummy
			// push+pop to trigger beginning of a new statement
			mCtx.pushOperand("dummy");
			mCtx.popOperand();
		}

		String varName = mCtx.topFrame().getLocalVars().get(var).name;

		if (increment == 1) {
			cCtx.println(varName + "++");
		} else if (increment == -1) {
			cCtx.println(varName + "--");
		} else if (increment > 0) {
			cCtx.println(varName + "+=" + increment);
		} else {
			cCtx.println(varName + "-=" + increment);
		}
	}

	@Override
	public void visitTableSwitchInsn(int min, int max, Label dflt,
			Label... labels) {
		currentLabel = null;

		if (mv != null) {
			mv.visitTableSwitchInsn(min, max, dflt, labels);
		}
	}

	@Override
	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
		currentLabel = null;

		if (mv != null) {
			mv.visitLookupSwitchInsn(dflt, keys, labels);
		}
	}

	@Override
	public void visitMultiANewArrayInsn(String desc, int dims) {
		currentLabel = null;

		if (mv != null) {
			mv.visitMultiANewArrayInsn(desc, dims);
		}
	}

	@Override
	public void visitTryCatchBlock(Label start, Label end, Label handler,
			String type) {
		currentLabel = null;

		if (mv != null) {
			mv.visitTryCatchBlock(start, end, handler, type);
		}
	}

	@Override
	public void visitLocalVariable(String name, String desc, String signature,
			Label start, Label end, int index) {
		// Seems this is invoked too late to be useful (after method body is visited)..
	}

	@Override
	public void visitLineNumber(int line, Label start) {
		// TODO: Insert into source map
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
	}

	@Override
	public void visitEnd() {
		mCtx.maybeEndStatement();
		cCtx.popScope();
		mCtx.popFrame();
	}
}
