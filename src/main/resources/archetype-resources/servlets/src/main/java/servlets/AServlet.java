#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * Copyright 2012 - Six Dimensions
 * All Rights Reserved
 */
package ${package}.servlets;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.day.cq.commons.jcr.JcrConstants;
import ${package}.services.constants.AAUConstants;

@Component(label = "Academy of Arts Content XML Servlet", name = "${package}.servlets.ContentXMLServlet", metatype = true, immediate = true)
@Service(value = { Servlet.class })
@Properties({
		@Property(name = "sling.servlet.methods", value = "GET"),
		@Property(name = "sling.servlet.selectors", value = "contentxml"),
		@Property(name = "sling.servlet.resourceTypes", value = {
				"${parentArtifactId}/components/pages/module", "${parentArtifactId}/components/pages/course",
				"${parentArtifactId}/components/pages/department" }),
		@Property(name = "sling.servlet.extensions", value = { "html", "xml" }),
		@Property(name = "service.vendor", value = "Six Dimensions"),
		@Property(name = "service.description", value = "Servlet for returning xml content") })
public class ContentXMLServlet extends SlingAllMethodsServlet {
	private static final long serialVersionUID = -5716924868083580116L;
	private static final Logger log = LoggerFactory
			.getLogger(ContentXMLServlet.class);
	private static final String COURSE_TYPE = "course";
	private static final Object SESSION_TYPE = "session";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.sling.api.servlets.SlingAllMethodsServlet${symbol_pound}doPost(org.apache
	 * .sling.api.SlingHttpServletRequest,
	 * org.apache.sling.api.SlingHttpServletResponse)
	 */
	@Override
	protected void doGet(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {
		log.trace("doGet");

		String host = request.getScheme() + "://" + request.getServerName();
		if (request.getServerPort() != 80) {
			host += ":" + request.getServerPort();
		}
		log.debug("Found host: " + host);

		try {
			// We need a Document
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Creating the XML tree

			// create the root element and add it to the document
			Resource currentResource = request.getResource();
			if (currentResource.getPath().endsWith("jcr:content")) {
				currentResource = currentResource.getParent();
			}

			Element root = toXML(currentResource, doc, host);
			doc.appendChild(root);

			String childElmentName = "";

			if (getType(currentResource).equals("department")) {
				childElmentName = "courses";
			} else {

				childElmentName = getType(currentResource).equals(COURSE_TYPE) ? "modules"
						: "sessions";
			}

			Element childPageElement = doc.createElement(childElmentName);
			root.appendChild(childPageElement);

			listChildren(currentResource, childPageElement, host);

			// ///////////////
			// Output the XML

			// set up a transformer
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			// create string from xml tree
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(doc);
			trans.transform(source, result);
			String xmlString = sw.toString();

			response.setContentType("text/xml");

			// print xml
			response.getWriter().println(xmlString);

		} catch (Exception e) {
			log.error("Unexpected exception: " + e, e);
			throw new ServletException(e);
		}

	}

	/**
	 * Lists out all of the children of the current node into an XML Document.
	 * 
	 * @param current
	 *            the current resource
	 * @param childrenElement
	 *            the element to update
	 * @param host
	 *            the host for the current request
	 * @throws ServletException
	 */
	private void listChildren(Resource current, Element childrenElement,
			String host) {
		log.trace("listChildren");

		Document doc = childrenElement.getOwnerDocument();

		// the children of the current content, terminate if there are none

		Iterator<Resource> children = current.listChildren();

		if (!children.hasNext()) {
			return;
		}

		while (children.hasNext()) {

			Resource childResource = children.next();

			if (childResource.isResourceType("cq:Page")) {
				Element childElement = toXML(childResource, doc, host);
				childrenElement.appendChild(childElement);

				String type = getType(childResource);
				log.trace("listChildren:type=" + type);
				if (!type.equals(SESSION_TYPE)) {
					String childElmentName = type.equals(COURSE_TYPE) ? "modules"
							: "sessions";
					Element childPagesElement = doc
							.createElement(childElmentName);
					childElement.appendChild(childPagesElement);
					listChildren(childResource, childPagesElement, host);
				}

			}
		}
	}

	/**
	 * Gets the type of the specified resource.
	 * 
	 * @param resource
	 *            the current resource
	 * @return the type of the specified resource
	 */
	private String getType(Resource resource) {
		log.trace("getType");
		return resource
				.getChild("jcr:content")
				.getResourceType()
				.substring(
						resource.getChild("jcr:content").getResourceType()
								.lastIndexOf("/") + 1);
	}

	/**
	 * Converts a given Resource to an XML Element, serializing the path, URL,
	 * title and type of the Resource.
	 * 
	 * @param resource
	 *            the resource to serialize
	 * @param doc
	 *            the current XML Document
	 * @param host
	 *            the current host for the request.
	 * @return the XML element to be added into the document
	 */
	private Element toXML(Resource resource, Document doc, String host) {
		log.trace("toXML");

		log.debug("Serializing resource: " + resource.getPath() + " to XML");
		String type = getType(resource);

		log.debug("Creating node of type: " + type);

		Element pageNodeElement = doc.createElement(type);

		ValueMap properties = resource.getChild("jcr:content").adaptTo(
				ValueMap.class);

		addJCRProperty(JcrConstants.JCR_TITLE, "title", properties,
				pageNodeElement);

		Element pathElement = doc.createElement("path");
		pageNodeElement.appendChild(pathElement);
		pathElement.appendChild(doc.createTextNode(resource.getPath()));

		Element currentURLElement = doc.createElement("url");
		currentURLElement.appendChild(doc.createTextNode(host
				+ resource.getPath() + ".html"));
		pageNodeElement.appendChild(currentURLElement);

		if (getType(resource).equals("department")) {
			addJCRProperty(AAUConstants.DEPARTMENT_ID_PROPERTY,
					AAUConstants.DEPARTMENT_ID_PROPERTY, properties,
					pageNodeElement);
		} else if (getType(resource).equals("course")) {
			addJCRProperty(AAUConstants.COURSE_NUMBER_PROPERTY,
					AAUConstants.COURSE_NUMBER_PROPERTY, properties,
					pageNodeElement);
		} else if (getType(resource).equals("version")) {
			addJCRProperty(AAUConstants.COURSE_VERSION_PROPERTY,
					AAUConstants.COURSE_VERSION_PROPERTY, properties,
					pageNodeElement);
		}

		/* Start of Video Information */
		if (getType(resource).equals("session")) {

			// serialize the components
			
			for (Iterator<Resource> components = resource.getChild(
					"jcr:content/session/par").listChildren(); components
					.hasNext(); ) {
				Resource component = components.next();
				if(component == null){
					continue;
				}
				if (component.getResourceType().equals(
						AAUConstants.VIDEO_COMPONENT_TYPE)) {
					Element videoElement = toXMLVideo(component, doc);
					pageNodeElement.appendChild(videoElement);
				}
			}
		}
		return pageNodeElement;
	}

	/**
	 * Adds a given jcr property as an XML property
	 * 
	 * @param key
	 *            the jcr property
	 * @param xmlkey
	 *            the xml key
	 * @param pageNodeElememt
	 *            the current page node element
	 */
	private void addJCRProperty(String key, String xmlKey, ValueMap properties,
			Element pageNodeElement) {
		log.trace("addJCRProperty");
		Element element = pageNodeElement.getOwnerDocument().createElement(
				xmlKey);
		element.appendChild(pageNodeElement.getOwnerDocument().createTextNode(
				properties.get(key, "NULL")));
		pageNodeElement.appendChild(element);
	}

	/**
	 * Adds the session video properties as xml properties
	 * 
	 * 
	 * @param resource
	 *            the resource to serialize
	 * @param doc
	 *            the current XML Document
	 * @return the XML element to be added into the document
	 */
	private Element toXMLVideo(Resource videoRsrc, Document doc) {
		log.trace("toXMLVideo");

		log.debug("Serializing resource: " + videoRsrc.getPath() + " to XML");
		Element pageNodeElement = doc.createElement("video");

		ValueMap video = videoRsrc.adaptTo(ValueMap.class);

		// retrieving the title
		String videoTitle = video.get("title", "NULL");
		Element titleElement = doc.createElement("displaytitle");
		titleElement.appendChild(doc.createTextNode(videoTitle));
		pageNodeElement.appendChild(titleElement);

		// retrieving the mediaType
		String mediaType = video.get("mediaType", "NULL");
		Element mediaTypeElement = doc.createElement("mediaType");
		mediaTypeElement.appendChild(doc.createTextNode(mediaType));
		pageNodeElement.appendChild(mediaTypeElement);

		if (video.containsKey("video")) {
			try {
				log.debug("Retrieving video JSON Data");
				// set the default to {}, which is an empty JSON Object
				JSONObject videoJSON = new JSONObject(video.get("video", "{}"));

				Iterator<String> keys = videoJSON.keys();
				while (keys.hasNext()) {

					String key = keys.next();
					String value = videoJSON.getString(key);

					Element keyElement = doc.createElement(key);
					keyElement.appendChild(doc.createTextNode(value));
					pageNodeElement.appendChild(keyElement);
				}
			} catch (JSONException e) {
				log.error("Exception retrieving data from JSON", e);
			}
		} else {
			log.debug("No JSON Video Data");
		}

		return pageNodeElement;
	}

}
