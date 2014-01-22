package com.trifork.jjs.compiler;

import java.io.IOException;
import java.io.PrintWriter;

import org.objectweb.asm.ClassReader;

public class Main {

	public static void main(String[] args) throws IOException {
		PrintWriter pw = new PrintWriter(System.out);
		ClassReader cr = new ClassReader("com.trifork.jjs.subjects.Greeter");

		new Compiler().compileSingleClass(cr, pw);
		pw.flush();
	}
}
