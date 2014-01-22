package com.trifork.jjs.testsuite;

import static org.junit.Assert.*;

import org.junit.Test;


public class BasicRhinoTest extends AbstractRhinoTest {

	@Test
	public void test() throws Exception {
		// A test that just verifies that the test environment is setup correctly
		scriptEngine.put("name", "Jeppe");
		scriptEngine.eval("java_lang_System.out.print('Hello ' + name + '!')");
		
		assertEquals("Hello Jeppe!", getOutput());
	}
}
