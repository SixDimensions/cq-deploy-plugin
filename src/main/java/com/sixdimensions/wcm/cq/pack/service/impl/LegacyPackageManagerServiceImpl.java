package com.sixdimensions.wcm.cq.pack.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.maven.plugin.logging.Log;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sixdimensions.wcm.cq.pack.dao.PackageManagerAPIDAO;
import com.sixdimensions.wcm.cq.pack.service.PackageManagerConfig;
import com.sixdimensions.wcm.cq.pack.service.PackageManagerService;

public class LegacyPackageManagerServiceImpl implements PackageManagerService {

	private Log log;
	private PackageManagerConfig config;
	private PackageManagerAPIDAO pmAPI;
	private static final String SERVICE_PATH = "/crx/packmgr/service.jsp";
	private static final String DELETE_COMMAND = "rm";
	private static final String INSTALL_COMMAND = "ins";
	private static final String COMMAND_KEY = "?cmd=";
	private static final String NAME_KEY = "&name=";
	private static final String SUCCESS_KEY = "200";

	public LegacyPackageManagerServiceImpl(PackageManagerConfig config) {
		log = config.getLog();
		this.config = config;
		pmAPI = new PackageManagerAPIDAO(config);
	}

	private String assembleUrl(String path, String command) {
		log.debug("assembleUrl");
		String url = config.getHost() + ":" + config.getPort() + SERVICE_PATH;
		url += COMMAND_KEY + command;

		if (path.contains("/")) {
			// TODO: Parse out the name and path
		} else {
			url += NAME_KEY + path;
		}
		return url;
	}

	public void delete(String path) throws Exception {
		log.debug("delete");

		byte[] result = pmAPI.doGet(assembleUrl(path, DELETE_COMMAND));

		log.debug("Result of command: " + new String(result, "UTF-8"));

		try {
			Response response = parseResponse(result);
			if (!response.isSucceeded() && config.isErrorOnFailure()) {
				throw new Exception("Failed to delete package, code: "
						+ response.getCode() + " message: "
						+ response.getMessage());
			}
		} catch (Exception e) {
			throw new Exception(
					"Exception parsing response, unable to determine command success or failure",
					e);
		}
	}

	public void dryRun(String path) throws Exception {
		throw new Exception("Dry Run is not supported in Legacy API");
	}

	public void install(String path) throws Exception {
		// TODO Auto-generated method stub

	}

	private Response parseResponse(byte[] response)
			throws XPathExpressionException, SAXException, IOException,
			ParserConfigurationException {
		log.debug("parseResponse");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // never forget this!
		Document doc = factory.newDocumentBuilder().parse(
				new ByteArrayInputStream(response));

		XPath xpath = XPathFactory.newInstance().newXPath();

		// Sample response
		//
		// <crx version="2.0" user="admin" workspace="crx.default">
		// <request>
		// <param name="cmd" value="rm"/>
		// <param name="name" value="myPackage"/>
		// </request>
		// <response>
		// <status code="200">ok</status>
		// </response>
		// </crx>

		log.debug("Parsing response code");
		XPathExpression codeXpr = xpath.compile("/crx/response/status/@code");
		String responseCode = ((NodeList) codeXpr.evaluate(doc,
				XPathConstants.NODESET)).item(0).getNodeValue();

		log.debug("Parsing response message");
		XPathExpression messageXpr = xpath.compile("/crx/response/status");
		String responseMessage = ((NodeList) messageXpr.evaluate(doc,
				XPathConstants.NODESET)).item(0).getChildNodes().item(0)
				.getNodeValue();

		Response responseObj = new Response(SUCCESS_KEY.equals(responseCode),
				responseCode, responseMessage);
		log.info("Response Code: " + responseCode);
		if (SUCCESS_KEY.equals(responseCode)) {
			log.info("Response Message: " + responseMessage);
		} else {
			log.warn("Error Message: " + responseMessage);
		}

		return responseObj;
	}

	public void preview(String path) throws Exception {
		throw new Exception("Preview is not supported in Legacy API");
	}

	public void upload(String path, File pkg) throws Exception {
		// TODO Auto-generated method stub

	}

	public boolean validatePath(String path) {
		// TODO Auto-generated method stub
		return false;
	}
}

class Response {
	boolean succeeded;
	private String code;
	private String message;

	protected Response(boolean succeeded, String code, String message) {
		this.succeeded = succeeded;
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public boolean isSucceeded() {
		return succeeded;
	}

}
