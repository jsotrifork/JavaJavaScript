package com.trifork.jjs.playground;

public class Exceptions {

	public static void tryCatch(int x) {
		try {
			g();
		} catch (Exception ex) {
			System.out.println("Got ex: " + ex);
		}
		System.out.println("Outside block");
	}
	
	public static void tryCatch2(int x) throws Exception {
		g();
		try {
			g();
		} catch (Exception ex) {
			System.out.println("Got ex: " + ex);
		}
		System.out.println("Outside block");
	}
	
	public static void tryCatchFinally(int x) {
		try {
			g();
		} catch (Exception ex) {
			System.out.println("Got ex: " + ex);
		} finally {
			System.out.println("Finally!");
		}
		System.out.println("Outside block");
	}

	public static void tryFinally(int x) throws Exception {
		try {
			g();
		} finally {
			System.out.println("Finally!");
		}
		System.out.println("Outside block");
	}

	public static void g() throws Exception {
		System.out.println("Going to throw!");
		throw new RuntimeException("BOO!!");
	}

}
