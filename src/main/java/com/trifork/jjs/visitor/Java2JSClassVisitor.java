package com.trifork.jjs.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class Java2JSClassVisitor extends ClassVisitor {

	private String jsClassName;
	private ClassContext cCtx;
	private String superClassName;
	private boolean addComments = false;

	public Java2JSClassVisitor(ClassContext cCtx, boolean addComments) {
		super(Opcodes.ASM4);
		this.cCtx = cCtx;
		this.addComments = addComments;
	}

    @Override
    public void visit(int version, int access, String name, String signature,
            String superName, String[] interfaces) {
    
    	jsClassName = cCtx.mapClassName(name);
    	superClassName = cCtx.mapClassName(superName);

//    	cCtx.print("var ");
    	cCtx.print(jsClassName);
//    	if ("java/lang/Object".equals(superName)) {
//    		cCtx.print(" = (function ()");
//        	cCtx.pushScope();
//    	} else {
    		cCtx.print(" = (function (_super)");
        	cCtx.pushScope();
    		cCtx.print("__extends(");
    		cCtx.print(jsClassName);
    		cCtx.println(", _super);");
//    	}
    }

	@Override
    public void visitSource(String source, String debug) {
    	super.visitSource(source, debug);
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
    	super.visitOuterClass(owner, name, desc);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
    	return super.visitAnnotation(desc, visible);
    }

    @Override
    public void visitAttribute(Attribute attr) {
    	super.visitAttribute(attr);
    }

    @Override
    public void visitInnerClass(String name, String outerName,
            String innerName, int access) {

    	super.visitInnerClass(name, outerName, innerName, access);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc,
            String signature, Object value) {

            return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
            String signature, String[] exceptions) {

    	MethodVisitor methodVisitor = new Java2JSMethodVisitor(cCtx, jsClassName, access, name, desc, signature, exceptions, addComments);
    	
    	if (addComments ) {
    		methodVisitor = new AddCommentsMethodVisitor(cCtx, methodVisitor);
    	}
    	
        return methodVisitor;
    }

	@Override
    public void visitEnd() {
    	cCtx.print("return ");
    	cCtx.print(jsClassName);
    	cCtx.println(";");
    	cCtx.popScope();
    	cCtx.print(")(");
//    	if (!"java_lang_Object".equals(superClassName)) {
    		cCtx.print(superClassName);
//    	}
    	cCtx.println(");");
    }
}
