package com.trifork.jjs.testsuite.basic;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Test;

import com.trifork.jjs.testsuite.AbstractRhinoTest;
import com.trifork.jjs.testsuite.basic.testsubjects.WhileConditions;

public class WhileConditionsTest extends AbstractRhinoTest {
	@Test
	public void while1() throws ScriptException, IOException {
		compileAndEvalClass(WhileConditions.class);
		
		invokeStatic(WhileConditions.class, "whileCondition1", 3);
		assertEquals("3.0,2.0,1.0,", getOutput());
	}

}
