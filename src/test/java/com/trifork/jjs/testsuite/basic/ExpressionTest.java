package com.trifork.jjs.testsuite.basic;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Test;

import com.trifork.jjs.testsuite.AbstractRhinoTest;
import com.trifork.jjs.testsuite.basic.testsubjects.Expressions;

public class ExpressionTest extends AbstractRhinoTest {
	@Test
	public void add() throws ScriptException, IOException {
		compileAndEvalClass(Expressions.class);
		
		invokeStatic(Expressions.class, "add", 3, 10);
		assertEquals("13.0", getOutput());
	}

	@Test
	public void subtract() throws ScriptException, IOException {
		compileAndEvalClass(Expressions.class);
		
		invokeStatic(Expressions.class, "subtract", 3, 10);
		assertEquals("-7.0", getOutput());
	}

	@Test
	public void multiply() throws ScriptException, IOException {
		compileAndEvalClass(Expressions.class);
		
		invokeStatic(Expressions.class, "multiply", 3, 10);
		assertEquals("30.0", getOutput());
	}

	@Test
	public void divide() throws ScriptException, IOException {
		compileAndEvalClass(Expressions.class);
		
		invokeStatic(Expressions.class, "divide", 3, 10);
		assertEquals("0.3", getOutput());
	}

	@Test
	public void remainder() throws ScriptException, IOException {
		compileAndEvalClass(Expressions.class);
		
		invokeStatic(Expressions.class, "remainder", 10, 3);
		assertEquals("1.0", getOutput());
	}

	@Test
	public void changeSign() throws ScriptException, IOException {
		compileAndEvalClass(Expressions.class);
		
		invokeStatic(Expressions.class, "changeSign", 10);
		assertEquals("-10.0", getOutput());
	}

	@Test
	public void combine1() throws ScriptException, IOException {
		compileAndEvalClass(Expressions.class);
		
		invokeStatic(Expressions.class, "combine1", 10, 3, 20);
		assertEquals("70.0", getOutput());
	}

	@Test
	public void combine2() throws ScriptException, IOException {
		compileAndEvalClass(Expressions.class);
		
		invokeStatic(Expressions.class, "combine2", 10, 3, 20);
		assertEquals("50.0", getOutput());
	}

	@Test
	public void combine3() throws ScriptException, IOException {
		compileAndEvalClass(Expressions.class);
		
		invokeStatic(Expressions.class, "combine3", 10, 3, 20);
		assertEquals("260.0", getOutput());
	}
}
