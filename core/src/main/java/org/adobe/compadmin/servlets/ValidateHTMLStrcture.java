package org.adobe.compadmin.servlets;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SlingServlet(
        label = "Component Admin - Valdate HTML Strcture",
        description = "Comonent Admin - Validation of HTML Strcuture",
        paths = { "/validateHTMLStrcture" },
        methods = {"POST" }, // Ignored if paths is set - Defaults to GET if not specified.
        extensions = { "html"}  // Ignored if paths is set
)
public class ValidateHTMLStrcture extends SlingAllMethodsServlet {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private static final Logger log = LoggerFactory.getLogger(ValidateHTMLStrcture.class);
	@Override
	protected final void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws
	        ServletException, IOException {
	}

}
