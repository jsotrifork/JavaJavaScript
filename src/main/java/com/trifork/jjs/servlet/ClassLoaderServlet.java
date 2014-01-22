package com.trifork.jjs.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ClassLoaderServlet extends HttpServlet {

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		System.out.println("path info: " + req.getPathInfo());

		String className = req.getPathInfo();
		
		if (className != null && className.startsWith("/")) {
			className = className.substring(1);
		}

		try {
			Class<?> cls = Class.forName(className);

			String js = com.trifork.jjs.compiler.Compiler.compileSingleClass(cls);

			res.getWriter().print(js);
			
		} catch (Exception ex) {
			throw new ServletException("Unable to load class " + className, ex);
		}
		
	}
}
