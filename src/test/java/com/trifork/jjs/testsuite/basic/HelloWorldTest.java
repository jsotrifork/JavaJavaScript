package com.trifork.jjs.testsuite.basic;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Test;

import com.trifork.jjs.testsuite.AbstractRhinoTest;
import com.trifork.jjs.testsuite.basic.testsubjects.PrintHello;

public class HelloWorldTest extends AbstractRhinoTest {

	@Test
	public void printHello() throws ScriptException, IOException {
		compileAndEvalClass(PrintHello.class);
		invokeStatic(PrintHello.class, "print");
		assertEquals("Hello!", getOutput());
	}
	
}
