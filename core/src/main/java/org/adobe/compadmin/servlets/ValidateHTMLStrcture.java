package org.adobe.compadmin.servlets;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SlingServlet(
        label = "Component Admin - Valdate HTML Strcture",
        description = "Component Admin - Validation of HTML Strcuture",
        paths = { "/bin/validateHTMLStrcture" },
        methods = {"GET","POST" }, // Ignored if paths is set - Defaults to GET if not specified.
        extensions = { "html"}  // Ignored if paths is set
)
public class ValidateHTMLStrcture extends SlingAllMethodsServlet {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private static final Logger log = LoggerFactory.getLogger(ValidateHTMLStrcture.class);
	@SuppressWarnings("unchecked")
	@Override
	protected final void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws
	        ServletException, IOException {
		
		response.setContentType("application/json");
		JSONObject jsonResponse = new JSONObject();
        try {
            jsonResponse.put("1","textfield");
            jsonResponse.put("2", "image");
            jsonResponse.put("3", "pathfield");
            // Write the JSON to the response
            response.getWriter().write(jsonResponse.toString(3));
            // Be default, a 200 HTTP Response Status code is used
        } catch (Exception e) {
            log.error("Could not formulate JSON response");
            // Servlet failures should always return an approriate HTTP Status code
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            // If you do not set your own HTML Response content, the OOTB HATEOS Response is used
            response.getWriter().write("ERROR");
        }
	}

}
