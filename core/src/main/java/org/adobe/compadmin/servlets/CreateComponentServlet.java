package org.adobe.compadmin.servlets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFactory;
import javax.servlet.ServletException;

import org.adobe.compadmin.utility.ParseHTMLUtility;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.commons.jcr.JcrUtil;

@SlingServlet(label = "Component Creation", description = "Creating component under /apps/comp-admin/user/components", paths = {
		"/bin/createComponent" }, methods = { "POST" }, // Ignored if
														// paths is
														// set -
														// Defaults
														// to GET if
														// not
														// specified.
		extensions = { "html" } // Ignored if paths is set
)
public class CreateComponentServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(CreateComponentServlet.class);

	private static final String COMPONENT_PATH = "/apps/componentadmin/user/components";
	private static final String TEXTFIELD = "textfield";
	private static final String IMAGE = "image";
	private static final String PATHFIELD = "pathfield";

	@SuppressWarnings("deprecation")
	@Override
	protected final void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		ResourceResolver resolver = request.getResourceResolver();
		String success = "success";
        String requestBody = ParseHTMLUtility.getBody(request);

		try {
			JSONObject jObject = new JSONObject(requestBody);
			String compTitle = jObject.getString("compTitle");
			String compDesc = jObject.getString("compDesc");
			String compGroup = jObject.getString("compGroup");
			String compHTML = jObject.getString("compHtml");
			createComponent(compTitle, compDesc, compGroup, compHTML, resolver);
			createComponentDialog(compTitle, jObject, resolver);
			resolver.commit();
			response.getWriter().write("success");
		} catch (JSONException e) {
			log.error("Error in parsing component structure {}", e);
		}
	}

	private void createComponent(String compTitle, String compDesc, String compGroup, String compHTML,
			ResourceResolver resolver) {
		Session session = resolver.adaptTo(Session.class);
		if (session == null) {
			log.error("Error in getting session");
			return;
		}
		try {
			Node componentNode = JcrUtil.createPath(COMPONENT_PATH, JcrConstants.NT_FOLDER, session);
			Node component = JcrUtil.createUniqueNode(componentNode, JcrUtil.createValidName(compTitle.toLowerCase()),
					"cq:Component", session);
			component.setProperty("jcr:title", compTitle);
			component.setProperty("jcr:description", compTitle);
			component.setProperty("componentGroup", compGroup);
			Node componentHtml = JcrUtil.createUniqueNode(component,
					JcrUtil.createValidName(compTitle.toLowerCase()) + ".html", JcrConstants.NT_FILE, session);
			InputStream is = new ByteArrayInputStream(compHTML.getBytes());
			Node jcrConstants = JcrUtil.createUniqueNode(componentHtml, JcrConstants.JCR_CONTENT,
					JcrConstants.NT_RESOURCE, session);
			ValueFactory valueFactory = session.getValueFactory();
			if (valueFactory != null) {
				Binary binary = session.getValueFactory().createBinary(is);
				jcrConstants.setProperty("jcr:data", binary);
				resolver.commit();
			}
		} catch (RepositoryException e) {
			log.error("Error in creating component nodes {}", e);
		} catch (PersistenceException e) {
			log.error("Error in writing component to repository {}", e);
		}
	}

	private void createComponentDialog(String compTitle, JSONObject jObject, ResourceResolver resolver) {
		Session session = resolver.adaptTo(Session.class);
		if (session == null) {
			log.error("Error in getting session");
			return;
		}
		try {
			Node component = JcrUtils
					.getNodeIfExists(COMPONENT_PATH + "/" + JcrUtil.createValidName(compTitle.toLowerCase()), session);
			Node dialog = JcrUtil.createUniqueNode(component, "cq:dialog", JcrConstants.NT_UNSTRUCTURED, session);
			dialog.setProperty("jcr:title", compTitle);
			dialog.setProperty("sling:resourceType", "cq/gui/components/authoring/dialog");
			Node content = JcrUtil.createUniqueNode(dialog, "content", JcrConstants.NT_UNSTRUCTURED, session);
			content.setProperty("sling:resourceType", "granite/ui/components/foundation/container");
			Node layout = JcrUtil.createUniqueNode(content, "layout", JcrConstants.NT_UNSTRUCTURED, session);
			layout.setProperty("sling:resourceType", "granite/ui/components/foundation/layouts/fixedcolumns");
			Node items = JcrUtil.createUniqueNode(content, "items", JcrConstants.NT_UNSTRUCTURED, session);
			Node column = JcrUtil.createUniqueNode(items, "column", JcrConstants.NT_UNSTRUCTURED, session);
			column.setProperty("sling:resourceType", "granite/ui/components/foundation/container");
			Node items2 = JcrUtil.createUniqueNode(column, "items", JcrConstants.NT_UNSTRUCTURED, session);
			JSONArray dialogProperties = new JSONArray(jObject.getString("dialog"));
			for (int i = 0; i < dialogProperties.length(); i++) {
				JSONObject property = (JSONObject) dialogProperties.get(i);
				String propertyType = property.getString("resourceType");
				switch (propertyType) {
				case TEXTFIELD:
					addTextfieldToNode(items2, property, i, resolver);
					break;
				case IMAGE:
					addImageToNode(items2, property, i, resolver);
					break;
				case PATHFIELD:
					addPathfieldToNode(items2, property, i, resolver);
					break;
				default:
					break;
				}
			}
		} catch (RepositoryException e) {
			log.error("Error in getting jcr nodes {}", e);
		} catch (JSONException e) {
			log.error("Error in parsing json for dialog proerties {}", e);
		}
	}

	private void addPathfieldToNode(Node items, JSONObject property, int count, ResourceResolver resolver) {
		Session session = resolver.adaptTo(Session.class);
		if (session == null) {
			log.error("Error in getting session");
			return;
		}
		try {
			Node link = JcrUtil.createUniqueNode(items, "path" + count, JcrConstants.NT_UNSTRUCTURED, session);
			link.setProperty("sling:resourceType", "granite/ui/components/foundation/form/pathbrowser");
			link.setProperty("name", "./element" + count);
			link.setProperty("fieldLabel", property.getString("fieldLabel"));
			link.setProperty("required", property.getString("isMandatory").equals("Yes") ? true : false);
		} catch (RepositoryException e) {
			log.error("Error in creating imagefield {}", e);
		} catch (JSONException e) {
			log.error("Error in parsing JSON for dialog {}", e);
		}
	}

	private void addImageToNode(Node items, JSONObject property, int count, ResourceResolver resolver) {
		Session session = resolver.adaptTo(Session.class);
		if (session == null) {
			log.error("Error in getting session");
			return;
		}
		try {
			Node file = JcrUtil.createUniqueNode(items, "image"+count, JcrConstants.NT_UNSTRUCTURED, session);
			file.setProperty("sling:resourceType", "cq/gui/components/authoring/dialog/fileupload");
			file.setProperty("name", "./element" + count);
			file.setProperty("uploadUrl", "${suffix.path}");
			file.setProperty("fileNameParameter", "./fileName");
			file.setProperty("fileReferenceParameter", "./filereference"+count);
			file.setProperty("fieldLabel", "Image Asset");
		} catch (RepositoryException e) {
			log.error("Error in creating imagefield {}", e);
		} 
	}

	private void addTextfieldToNode(Node items, JSONObject property, int count, ResourceResolver resolver) {
		Session session = resolver.adaptTo(Session.class);
		if (session == null) {
			log.error("Error in getting session");
			return;
		}
		try {
			Node text = JcrUtil.createUniqueNode(items, "text" + count, JcrConstants.NT_UNSTRUCTURED, session);
			text.setProperty("sling:resourceType", "granite/ui/components/coral/foundation/form/textfield");
			text.setProperty("name", "./element" + count);
			text.setProperty("fieldLabel", property.getString("fieldLabel"));
			text.setProperty("required", property.getString("isMandatory").equals("Yes") ? true : false);
		} catch (RepositoryException e) {
			log.error("Error in creating textfield {}", e);
		} catch (JSONException e) {
			log.error("Error in parsing JSON for dialog {}", e);
		}
	}
}
