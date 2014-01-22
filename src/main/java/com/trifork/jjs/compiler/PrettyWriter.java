package com.trifork.jjs.compiler;

import java.io.IOException;
import java.io.Writer;

public class PrettyWriter extends Writer {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private Writer delegate;
	private int indentLevel = 0;
	private static final int INDENT_CHARS = 4;
	private String[] cachedIndents = { "" };

	private boolean newlinePending;

	public PrettyWriter(Writer writer) {
		this.delegate = writer;
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		for (int i = off; i < len; i++) {
			char c = cbuf[i];
			switch (c) {
			case '}':
				indentLevel--;
				newlinePending = true;
				break;
			default:
				break;
			}
			
			if (newlinePending) {
				newlinePending = false;
				delegate.write(LINE_SEPARATOR);
				delegate.write(cachedIndents[indentLevel]);
			}
			delegate.write(c);

			switch (c) {
			case '{':
				indentLevel++;
				ensureCachedIndents();
				newlinePending = true;
				break;
			case '}':
				delegate.write(LINE_SEPARATOR);
				delegate.write(cachedIndents[indentLevel]);
				break;
			case ';':
				newlinePending = true;
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void flush() throws IOException {
	}

	@Override
	public void close() throws IOException {
	}

	@Override
	public String toString() {
		return delegate.toString();
	}
	
	private void ensureCachedIndents() {
		if (indentLevel >= cachedIndents.length) {
			String[] tmp = new String[indentLevel + 1];
			System.arraycopy(cachedIndents, 0, tmp, 0, cachedIndents.length);
			char[] ws = new char[ indentLevel * INDENT_CHARS ];
			for (int i = 0; i < ws.length; i++) {
				ws[i] = ' ';
			}
			
			tmp[indentLevel] = new String(ws);
			cachedIndents = tmp;
		}

	}
}
