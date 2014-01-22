package com.trifork.jjs.testsuite;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Before;
import org.junit.BeforeClass;

import com.trifork.jjs.compiler.Compiler;

public abstract class AbstractRhinoTest {

	static ScriptEngineManager mgr;

	static String jsRuntime;
	
	protected ScriptEngine scriptEngine;

	private ByteArrayOutputStream outputStream;

	private PrintStream sysout;

	
	@BeforeClass
	public static void createScriptEngineManager() throws IOException {
		mgr = new ScriptEngineManager();
		
		char[] buf = new char[1024];
		InputStreamReader reader = new InputStreamReader(AbstractRhinoTest.class.getResourceAsStream("/rt.js"));

		jsRuntime = "";

		int nRead;
		while ((nRead = reader.read(buf)) != -1) {
			jsRuntime += new String(buf, 0, nRead);
		}
		
	}

	@Before
	public void createScriptEngine() throws ScriptException {
		scriptEngine = mgr.getEngineByName("JavaScript");
		
        outputStream = new ByteArrayOutputStream();
        sysout = new PrintStream(outputStream);
        
        scriptEngine.put("java_lang_System", new SystemObject(sysout));
        
		scriptEngine.eval(jsRuntime);
		
//        Context c = Context.enter();
//        ScriptableObject scope = c.initStandardObjects();
//        StringWriter writer = new StringWriter();
//        ScriptableObject.putProperty(scope, "writer", writer);
//
//        String source = "  writer.write('hello');  ";
//        c.evaluateString(scope, source, "TEST", 1, null);
//        System.out.println(writer.getBuffer());
	}

	protected String getOutput() {
		sysout.flush();
		try {
			return new String(outputStream.toString(StandardCharsets.UTF_8.toString()));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	protected void clearOutput() {
		sysout.flush();
		outputStream.reset();
	}
	
	protected void compileAndEvalClass(Class<?> cls) throws IOException, ScriptException {
		String jsClass = Compiler.compileSingleClass(cls);
		
		System.out.println("Compiling class " + cls.getName());
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println(jsClass);
		System.out.println("-------------------------------------------------------------------------------");
		
		scriptEngine.eval(jsClass);
	}

	protected Object invokeStatic(Class<?> cls, String methodName, Object... args) throws ScriptException {
		String jsClassName = Compiler.mapClassName(cls.getName());
		
		return scriptEngine.eval(jsClassName + "." + methodName + "(" + join(args) + ")");
	}

	private String join(Object[] l) {
		StringBuilder sb = new StringBuilder();
		for (Object o: l) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(o);
		}
		
		return sb.toString();
	}


}
