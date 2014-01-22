package com.trifork.jjs.playground;

public class Conditionals {

	public static void if1(int x, int y) {
		if (x > y) {
			System.out.print(x);
		}
	}

	public static void f(int x) {
		if (x == 1) {
			System.out.println("1");
		} else if (x == 2) {
			System.out.println("2");
		} else if (x == 3) {
			System.out.println("3");
		} else {
			System.out.println("?");
		}
	}
	
	public static void nestedDoWhile(int x) {
		int i = 0;
		do {
			int j = 0;
			do {
				System.out.println(j);
				j++;
			} while (j < x);
			System.out.println(j);
			j++;
		} while (i < x);
	}

	public static void forLoop(int x) {
		for (int i = 0; i < x; i++) {
			System.out.println(i);
		}
	}
	
	public static void whileLoop(int x) {
		int i = 0; 
		
		while (i < x) {
			System.out.println(i);
			i++;
		}
	}
	
	public static void doWhileLoop(int x) {
		int i = 0;
		do {
			System.out.println(i);
			i++;
		} while (i < x);
	}
	
	public static void doSwitch(int x) {
		switch (x) {
		default:
			System.out.println("?");
			break;
		case 1:
			System.out.println("1");
			break;
		case 2:
			System.out.println("2");
		case 3: 
			System.out.println("3");
			break;
		}
	}
	
	public static void ifElse(int x) {
		if (x == 0) {
			System.out.println("0");
		} else if (x == 1) {
			System.out.println("1");
		} else {
			System.out.println("?");
		}
	}
	
	public static void whileWithBreak(int x) {
		int i = 0;
		while (i < x) {
			if (i == 3) {
				break;
			}
			System.out.println(i);
		}
	}
	
	public static void whileWithContinue(int x) {
		int i = 0;
		while (i < x) {
			if (i == 3) {
				continue;
			}
			System.out.println(i);
		}
	}
}
