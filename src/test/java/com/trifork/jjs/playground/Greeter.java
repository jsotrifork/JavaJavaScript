package com.trifork.jjs.playground;

class Greeter extends GreeterBase {
    String greeting;
    
    Greeter(String message) {
    	super(1, 2);
        this.greeting = message;
        
        Greeter.greetStatic(1 + 2, 3);
    }

    public String greet(int howMuch) {
        return "Hello, " + this.greeting;
    }

    public static String greetStatic(int x, int y) {
    	return "Hi " + (x - y);
    }
}

class GreeterBase {
	GreeterBase(int x, int y) {
		
	}
}