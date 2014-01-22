package com.trifork.jjs.testsuite.basic;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Test;

import com.trifork.jjs.compiler.Compiler;
import com.trifork.jjs.testsuite.AbstractRhinoTest;
import com.trifork.jjs.testsuite.basic.testsubjects.Base;
import com.trifork.jjs.testsuite.basic.testsubjects.Sub;

public class VirtualCallTest extends AbstractRhinoTest {
	@Test
	public void invokeOverwrittenVirtualMethod() throws ScriptException, IOException {
		compileAndEvalClass(Base.class);
		compileAndEvalClass(Sub.class);
		
		scriptEngine.eval("new " + Compiler.mapClassName(Sub.class.getName()) + "().bar();");
		assertEquals("sub", getOutput());
	}

}
