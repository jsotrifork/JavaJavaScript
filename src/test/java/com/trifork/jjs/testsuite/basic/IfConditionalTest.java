package com.trifork.jjs.testsuite.basic;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Test;

import com.trifork.jjs.testsuite.AbstractRhinoTest;
import com.trifork.jjs.testsuite.basic.testsubjects.IfCondition;

public class IfConditionalTest extends AbstractRhinoTest {
	@Test
	public void doIf1() throws ScriptException, IOException {
		compileAndEvalClass(IfCondition.class);
		invokeStatic(IfCondition.class, "if1", "4", "3");
		assertEquals("4.0", getOutput());
	}

	@Test
	public void doIf2() throws ScriptException, IOException {
		compileAndEvalClass(IfCondition.class);
		
		invokeStatic(IfCondition.class, "if2", "3", "4");
		assertEquals("", getOutput());

		clearOutput();
		
		invokeStatic(IfCondition.class, "if2", "4", "4");
		assertEquals("equal", getOutput());
	}

	@Test
	public void doIfElse() throws ScriptException, IOException {
		compileAndEvalClass(IfCondition.class);
		
		invokeStatic(IfCondition.class, "ifElse", "4", "3");
		assertEquals("4.0", getOutput());

		clearOutput();

		invokeStatic(IfCondition.class, "ifElse", "3", "4");
		assertEquals("4.0", getOutput());
}
}
