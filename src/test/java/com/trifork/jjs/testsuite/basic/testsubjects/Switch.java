package com.trifork.jjs.testsuite.basic.testsubjects;

public class Switch {

	public static void switchWithDefault(int x) {
		switch (x) {
		case 3:
			System.out.print("three");
			break;
		case 1:
			System.out.print("one");
			break;
		case 5:
			System.out.print("five");
			break;
		case 6:
			System.out.print("six");
			break;
		default:
			System.out.print("default");
			break;
		}
	}

	public static void switchNoDefault(int x) {
		switch (x) {
		case 1:
			System.out.print("one");
			break;
		case 2:
			System.out.print("two");
			break;
		case 3:
			System.out.print("three");
			break;
		}
	}

	public static void switchNoBreak(int x) {
		switch (x) {
		case 1:
			System.out.print("one");
		case 2:
			System.out.print("two");
		case 3:
			System.out.print("three");
		default:
			System.out.print("default");
		}
	}
/*
	public static void switchOnString(String s) {
		switch (s) {
		case "one":
			System.out.print(1);
			break;
		case "two":
			System.out.print(2);
			break;
		case "three":
			System.out.print(3);
			break;
		default:
			System.out.print(-1);
			break;
		}
	}

	public static void labelledBreaks(int i, int j) {
		OUTER: while (i > 0) {
			INNER: switch(i) {
			case 0: 
				System.out.println("zero");
				switch (j) {
				case 0: 
					break INNER;
				case 1: 
					break OUTER;
				case 2:
					break;
				}
			case 1:
				System.out.println("one");
			}
		}
	}
	*/
}