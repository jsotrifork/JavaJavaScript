package com.trifork.jjs.api;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(value=RUNTIME)
@Target(value={TYPE, METHOD})
public abstract @interface Native {
	
	public abstract String value() default "";
	public abstract String resource() default "";
	public abstract String name() default "";

}
