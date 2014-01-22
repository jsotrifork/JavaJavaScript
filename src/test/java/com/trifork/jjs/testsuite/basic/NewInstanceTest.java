package com.trifork.jjs.testsuite.basic;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Test;

import com.trifork.jjs.testsuite.AbstractRhinoTest;
import com.trifork.jjs.testsuite.basic.testsubjects.Base;
import com.trifork.jjs.testsuite.basic.testsubjects.BaseClient;

public class NewInstanceTest extends AbstractRhinoTest {
	@Test
	public void createNewInstanceMethod() throws ScriptException, IOException {
		compileAndEvalClass(Base.class);
		compileAndEvalClass(BaseClient.class);
		
		invokeStatic(BaseClient.class, "createNewInstance");
		assertEquals("base", getOutput());
	}

}
