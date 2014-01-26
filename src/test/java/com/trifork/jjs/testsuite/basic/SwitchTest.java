package com.trifork.jjs.testsuite.basic;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Ignore;
import org.junit.Test;

import com.trifork.jjs.testsuite.AbstractRhinoTest;
import com.trifork.jjs.testsuite.basic.testsubjects.Switch;

public class SwitchTest extends AbstractRhinoTest {
	@Test
	public void switchWithDefault() throws ScriptException, IOException {
		compileAndEvalClass(Switch.class);
		
		invokeStatic(Switch.class, "switchWithDefault", 3);
		assertEquals("three", getOutput());

		clearOutput();
		invokeStatic(Switch.class, "switchWithDefault", 4);
		assertEquals("default", getOutput());

	}

	@Test
	public void switchNoDefault() throws ScriptException, IOException {
		compileAndEvalClass(Switch.class);
		
		invokeStatic(Switch.class, "switchNoDefault", 2);
		assertEquals("two", getOutput());

		clearOutput();
		
		invokeStatic(Switch.class, "switchNoDefault", 5);
		assertEquals("", getOutput());

	}

	@Test
	public void switchNoBreak() throws ScriptException, IOException {
		compileAndEvalClass(Switch.class);
		
		invokeStatic(Switch.class, "switchNoBreak", 2);
		assertEquals("twothreedefault", getOutput());

	}

	@Test
	public void lookupSwitch() throws ScriptException, IOException {
		compileAndEvalClass(Switch.class);
		
		invokeStatic(Switch.class, "nonConsequtiveSwitch", 200);
		assertEquals("twohundred", getOutput());

	}

	@Ignore
	@Test
	public void switchOnString() throws ScriptException, IOException {
		compileAndEvalClass(Switch.class);
		
		invokeStatic(Switch.class, "switchNoString", "two");
		assertEquals("2", getOutput());

	}
}
