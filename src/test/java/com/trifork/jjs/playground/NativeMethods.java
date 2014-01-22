package com.trifork.jjs.playground;

import com.trifork.jjs.api.Native;

@Native (resource="NativeMethods.js" )
public class NativeMethods {

	@Native (value=
			"Window.alert(\"Greetings \" + me); " +
			"Window.alert(\"Greetings again, \" + me); "
			)
	public static void goNative(String me) {}

	@Native (resource="NativeMethodsExtra.js", name="foo")
	public static void goNative2(String me) {}
}
