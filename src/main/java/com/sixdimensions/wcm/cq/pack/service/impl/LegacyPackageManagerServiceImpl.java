/*
 * Copyright 2012 - Six Dimensions
 * 
 * This file is part of the CQ Deploy Plugin.
 * 
 * The CQ Deploy Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * The CQ Deploy Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the CQ Deploy Plugin.  If not, see <http://www.gnu.org/licenses/>.
 */
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

import org.apache.commons.httpclient.HttpStatus;
import org.apache.maven.plugin.logging.Log;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sixdimensions.wcm.cq.dao.HTTPServiceDAO;
import com.sixdimensions.wcm.cq.pack.service.PackageManagerConfig;
import com.sixdimensions.wcm.cq.pack.service.PackageManagerService;

/**
 * Implementation of the Package Manager Service that uses the Legacy API.
 * Useful for CQ < 5.2.
 * 
 * @author dklco
 */
public class LegacyPackageManagerServiceImpl implements PackageManagerService {

	/**
	 * Eunmeration of the various commands for the Legacy Package Manager
	 * Service.
	 * 
	 * @author dklco
	 */
	private static enum COMMAND {
		DELETE("rm"), INSTALL("inst"), UPLOAD("upload");

		/**
		 * The internal command string
		 */
		private final String cmd;

		/**
		 * Construct a new COMMAND with the specified string
		 * 
		 * @param cmd
		 *            the string representation of the command for this COMMAND
		 *            instance
		 */
		COMMAND(final String cmd) {
			this.cmd = cmd;
		}

		/**
		 * Gets the command string to pass to the service.
		 * 
		 * @return the service command string
		 */
		public String getCmd() {
			return this.cmd;
		}
	}

	/**
	 * Constant for the file key.
	 */
	private static final String FILE_KEY = "file";

	/**
	 * Constant for the command key
	 */
	private static final String COMMAND_KEY = "?cmd=";

	/**
	 * Constant for the package group key
	 */
	private static final String GROUP_KEY = "&group=";

	/**
	 * Constant for the package name key.
	 */
	private static final String NAME_KEY = "&name=";

	/**
	 * Constant for the service relative path.
	 */
	private static final String SERVICE_PATH = "/crx/packmgr/service.jsp";

	/**
	 * The configuration for this instance of the service.
	 */
	private final PackageManagerConfig config;

	/**
	 * The logger for this class.
	 */
	private final Log log;

	/**
	 * The DAO used to interact with the service.
	 */
	private final HTTPServiceDAO pmAPI;

	/**
	 * Construct a new legacy package manager service.
	 * 
	 * @param config
	 *            the configuration to use
	 */
	public LegacyPackageManagerServiceImpl(final PackageManagerConfig config) {
		this.log = config.getLog();
		this.config = config;
		this.pmAPI = new HTTPServiceDAO(config);
	}

	/**
	 * Assembles a URL based on the current configuration, the path and the
	 * command to execute.
	 * 
	 * @param path
	 *            the path of the package
	 * @param command
	 *            the command being executed
	 * @return the url to execute
	 */
	private String assembleUrl(final String path, final COMMAND command) {
		this.log.debug("assembleUrl");
		String url = this.config.getHost() + ":" + this.config.getPort()
				+ SERVICE_PATH;
		url += COMMAND_KEY + command.getCmd();

		if (path.contains("/")) {
			final String name = path.substring(path.indexOf("/") + 1);
			final String group = path.substring(0, path.indexOf("/"));
			url += NAME_KEY + name + GROUP_KEY + group;
		} else {
			url += NAME_KEY + path;
		}
		return url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sixdimensions.wcm.cq.pack.service.PackageManagerService#delete(java
	 * .lang.String)
	 */
	public void delete(final String path) throws Exception {
		this.log.debug("delete");

		this.log.info("Deleting package at path: " + path);

		final byte[] result = this.pmAPI.doGet(this.assembleUrl(path,
				COMMAND.DELETE));

		this.log.debug("Result of command: " + new String(result, "UTF-8"));

		final Response response = this.parseResponse(result);
		if (!response.isSucceeded() && this.config.isErrorOnFailure()) {
			if (path.endsWith(".jar")) {
				this.log.warn("Delete failed with jar, trying with zip.");
				this.delete(path.replace(".jar", ".zip"));
			} else {
				throw new Exception("Failed to delete package, code: "
						+ response.getCode() + " message: "
						+ response.getMessage());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sixdimensions.wcm.cq.pack.service.PackageManagerService#dryRun(java
	 * .lang.String)
	 */
	public void dryRun(final String path) throws Exception {
		throw new Exception("Dry Run is not supported in Legacy API");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sixdimensions.wcm.cq.pack.service.PackageManagerService#install(java
	 * .lang.String)
	 */
	public void install(final String path) throws Exception {
		this.log.debug("install");

		this.log.info("Installing package at path: " + path);

		final byte[] result = this.pmAPI.doGet(this.assembleUrl(path,
				COMMAND.INSTALL));

		this.log.debug("Result of command: " + new String(result, "UTF-8"));
		final Response response = this.parseResponse(result);

		if (!response.isSucceeded() && this.config.isErrorOnFailure()) {
			if (path.endsWith(".jar")) {
				this.log.warn("Install failed with jar, trying with zip.");
				this.install(path.replace(".jar", ".zip"));
			} else {
				throw new Exception("Failed to install package, code: "
						+ response.getCode() + " message: "
						+ response.getMessage());
			}
		}
	}

	/**
	 * Parses the response from the server using XPath.
	 * 
	 * @param response
	 *            the response to parse
	 * @return a response object representing the response data
	 * @throws XPathExpressionException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	private Response parseResponse(final byte[] response)
			throws XPathExpressionException, IOException,
			ParserConfigurationException {
		this.log.debug("parseResponse");

		Response responseObj = null;
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setNamespaceAware(true); // never forget this!
			final Document doc = factory.newDocumentBuilder().parse(
					new ByteArrayInputStream(response));

			final XPath xpath = XPathFactory.newInstance().newXPath();

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

			this.log.debug("Parsing response code");
			final XPathExpression codeXpr = xpath
					.compile("/crx/response/status/@code");
			int responseCode = -1;
			try {
				responseCode = Integer.parseInt(((NodeList) codeXpr.evaluate(
						doc, XPathConstants.NODESET)).item(0).getNodeValue(),
						10);
			} catch (final NumberFormatException nfe) {
				this.log.warn("Unable to parse "
						+ ((NodeList) codeXpr.evaluate(doc,
								XPathConstants.NODESET)).item(0).getNodeValue()
						+ " as a number");
			}

			this.log.debug("Parsing response message");
			final XPathExpression messageXpr = xpath
					.compile("/crx/response/status");
			final String responseMessage = ((NodeList) messageXpr.evaluate(doc,
					XPathConstants.NODESET)).item(0).getChildNodes().item(0)
					.getNodeValue();

			responseObj = new Response(HttpStatus.SC_OK == responseCode,
					responseCode, responseMessage);
			this.log.debug("Response Code: " + responseCode);
			if (HttpStatus.SC_OK == responseCode) {
				this.log.debug("Response Message: " + responseMessage);
			} else {
				this.log.warn("Error Message: " + responseMessage);
			}
		} catch (final SAXException se) {
			final String message = "Exception parsing XML response, assuming failure.  "
					+ "This often occurs when an invalid XML file is uploaded as the error message "
					+ "is not properly escaped in the response.";
			this.log.warn(message, se);
			this.log.warn("Response contents: " + new String(response, "utf-8"));
			responseObj = new Response(false, 500, message);
		}

		return responseObj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sixdimensions.wcm.cq.pack.service.PackageManagerService#upload(java
	 * .lang.String, java.io.File)
	 */
	public void upload(final String path, final File pkg) throws Exception {
		this.log.debug("install");

		this.log.info("Uploading package " + pkg.getAbsolutePath()
				+ " to path: " + path);

		final byte[] result = this.pmAPI.postFile(
				this.assembleUrl(path, COMMAND.UPLOAD), FILE_KEY, pkg);

		this.log.debug("Result of command: " + new String(result, "UTF-8"));

		final Response response = this.parseResponse(result);
		if (!response.isSucceeded() && this.config.isErrorOnFailure()) {
			throw new Exception("Failed to upload package, code: "
					+ response.getCode() + " message: " + response.getMessage());
		}
	}
}

/**
 * Represents a response from the Legacy API.
 * 
 * @author klcodanr
 */
class Response {
	private final int code;
	private final String message;
	boolean succeeded;

	/**
	 * Construct a new Response
	 * 
	 * @param succeeded
	 *            whether or not the request succeeded aka the response code
	 *            equals 200
	 * @param code
	 *            the response code returned by the server
	 * @param message
	 *            the message returned by the server usually either 'ok' or some
	 *            sort of error
	 */
	protected Response(final boolean succeeded, final int code,
			final String message) {
		this.succeeded = succeeded;
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}

	public boolean isSucceeded() {
		return this.succeeded;
	}

}
