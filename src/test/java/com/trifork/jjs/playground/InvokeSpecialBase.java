package com.trifork.jjs.playground;

public class InvokeSpecialBase {
	InvokeSpecialBase(String arg) {
		
	}
}

class InvokeSpecialSub extends InvokeSpecialBase {
	InvokeSpecialSub(String arg) {
		super(arg);
	}
}

class InvokeSpecialClient {
	InvokeSpecialClient(String arg) {
		new InvokeSpecialBase(arg);
	}
}


