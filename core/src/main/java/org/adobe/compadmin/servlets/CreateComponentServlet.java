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
import javax.swing.JButton;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
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

	@SuppressWarnings("deprecation")
	@Override
	protected final void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		String componentStructure = "{\"compTitle\": \"sample ddd\",\"compDesc\": \"sample\",\"compGroup\": \"test\",\"compHTML\": \"asdadssdasdasdasdsa\",\"dialog\": [{\"type\": \"textfield\",\"label\": \"sample\",\"isMandatory\": \"No\"},{\"type\": \"image\",\"label\": \"sample\",\"isMandatory\": \"No\"}]}";
		ResourceResolver resolver = request.getResourceResolver();
		try {
			JSONObject jObject = new JSONObject(componentStructure.trim());
			String compTitle = jObject.getString("compTitle");
			String compDesc = jObject.getString("compDesc");
			String compGroup = jObject.getString("compGroup");
			String compHTML = jObject.getString("compHTML");
			createComponent(compTitle, compDesc, compGroup, compHTML, resolver);
			createComponentDialog(compTitle, jObject, resolver);
			resolver.commit();
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
				String propertyType = property.getString("type");
				switch (propertyType) {
				case TEXTFIELD:
					addTextfieldToNode(component,items2, property, i, resolver);
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

	private void addTextfieldToNode(Node component, Node items, JSONObject property, int count, ResourceResolver resolver) {
		Session session = resolver.adaptTo(Session.class);
		if (session == null) {
			log.error("Error in getting session");
			return;
		}
		try {
			Node text = JcrUtil.createUniqueNode(items, "text", JcrConstants.NT_UNSTRUCTURED, session);
			text.setProperty("sling:resourceType", "granite/ui/components/coral/foundation/form/textfield");
			text.setProperty("name", "./element" + count);
			text.setProperty("fieldLabel", property.getString("label"));
			text.setProperty("required", property.getString("isMandatory").equals("Yes") ? true : false);
		} catch (RepositoryException e) {
			log.error("Error in creating textfield {}", e);
		} catch (JSONException e) {
			log.error("Error in parsing JSON for dialog {}", e);
		}
	}
}
