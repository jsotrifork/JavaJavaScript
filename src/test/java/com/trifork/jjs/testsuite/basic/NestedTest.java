package com.trifork.jjs.testsuite.basic;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Test;

import com.trifork.jjs.testsuite.AbstractRhinoTest;
import com.trifork.jjs.testsuite.basic.testsubjects.Stateful;
import com.trifork.jjs.testsuite.basic.testsubjects.StatefulClient;

public class NestedTest extends AbstractRhinoTest {
	@Test
	public void createNewInstanceMethod() throws ScriptException, IOException {
		compileAndEvalClass(Stateful.class);
		compileAndEvalClass(StatefulClient.class);
		
		invokeStatic(StatefulClient.class, "doNested");
		assertEquals("7.0", getOutput());
	}

}
