package com.trifork.jjs.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import com.trifork.jjs.visitor.ClassContext;
import com.trifork.jjs.visitor.Java2JSClassVisitor;
import com.trifork.jjs.visitor.TypeOracle;

public class Compiler {

	static boolean debug = false;
	
	public static String compileSingleClass(Class<?> cls) throws IOException {
		StringWriter writer = new StringWriter();
		compileSingleClass(cls, writer);
		
		return writer.toString();
	}
	
	public static void compileSingleClass(Class<?> cls, Writer pw) throws IOException {
//		ClassReader cr = new ClassReader(cls.getName());

		String classFileName = cls.getName().replace('.', '/') + ".class";
		
		InputStream in = Compiler.class.getClassLoader().getResourceAsStream(classFileName);
		ClassReader cr = new ClassReader(in);
		

		compileSingleClass(cr, pw);
	}

	
	public static void compileSingleClass(ClassReader cr, Writer pw) throws IOException {
		TypeOracle typeOracle = new TypeOracle();
		GeneratorStream generatorStream = new GeneratorStream();
		ClassContext ctx = new ClassContext(generatorStream, typeOracle);
		
		ClassVisitor cv = new Java2JSClassVisitor(ctx, debug);
		cr.accept(cv, 0);
		generatorStream.writeTo(pw);
	}

	public static String mapClassName(String name) {
		return new TypeOracle().mapClassName(name);
	}


}
