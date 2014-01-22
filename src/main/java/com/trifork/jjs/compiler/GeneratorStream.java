package com.trifork.jjs.compiler;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class GeneratorStream {
	ArrayList<Object> stream = new ArrayList<Object>();
	
	public void append(Object o) {
		stream.add(o);
	}
	
	public void writeTo(Writer writer) throws IOException {
		for (Object o: stream) {
			writer.write(o.toString());
		}
		
		stream.clear();
	}
}
