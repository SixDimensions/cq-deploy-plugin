/*
 * Copyright 2012 - Six Dimensions
 * 
 * This file is part of the CQ Package Plugin.
 * 
 * The CQ Package Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * The CQ Package Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the CQ Package Plugin.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sixdimensions.wcm.cq.dao;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.jcr.Binary;
import javax.jcr.Credentials;
import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.maven.plugin.logging.Log;

import com.sixdimensions.wcm.cq.service.CQServiceConfig;

/**
 * Performs interactions with the CQ Dav API.
 * 
 * @author klcodanr
 */
public class JCRDAO {

	/**
	 * Gets the mime type for the specified file.
	 * 
	 * @param file
	 *            the file from which to get the mime type
	 * @return the mime type string
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static String getMimeType(File file) throws IOException,
			MalformedURLException {
		String type = null;
		URL u = new URL("file://" + file.getAbsolutePath());
		URLConnection uc = null;
		uc = u.openConnection();
		type = uc.getContentType();
		return type;
	}

	private CQServiceConfig config;
	private Log log;

	private Session session;

	/**
	 * Construct a new PackageManagerAPIDAO with the specified configuration.
	 * 
	 * @param config
	 *            the configuration to use
	 */
	public JCRDAO(CQServiceConfig config) {
		this.config = config;
		this.log = config.getLog();
	}

	/**
	 * Create a folder under the specified path with the specified name.
	 * 
	 * @param path
	 *            the path under which to create the folder
	 * @throws PathNotFoundException
	 * @throws RepositoryException
	 */
	public void createFolder(String path) throws PathNotFoundException,
			RepositoryException {
		log.debug("createFolder");

		log.debug("Creating nodes");
		String folderPath = path.substring(0, path.lastIndexOf("/"));
		String name = path.substring(path.lastIndexOf("/") + 1);
		Node parentFolder = session.getNode(folderPath);
		parentFolder.addNode(name, JcrConstants.NT_FOLDER);

		log.debug("Saving changes");
		session.save();
	}

	/**
	 * Deletes the node at the specified path and all subnodes.
	 * 
	 * @param path
	 *            the path of the node to delete
	 * @throws PathNotFoundException
	 * @throws RepositoryException
	 */
	public void delete(String path) throws PathNotFoundException,
			RepositoryException {
		log.debug("delete");
		delete(session.getNode(path));
		session.save();
	}

	protected void delete(Node node) throws RepositoryException {
		log.debug("delete");

		log.debug("Deleting nodes at path: " + node.getPath());
		NodeIterator nodes = node.getNodes();
		while (nodes.hasNext()) {
			Node childNode = nodes.nextNode();

			if (JcrConstants.JCR_CONTENT.equals(childNode.getName())) {
				continue;
			}
			delete(childNode);
		}
		node.remove();
	}

	/**
	 * Checks to see if a folder exists at the specified path.
	 * 
	 * @param path
	 *            the path to check if a folder exists, an invalid path will not
	 *            trigger an error
	 * @return true if the folder exists or false otherwise
	 * @throws RepositoryException
	 */
	public boolean folderExists(String path) throws RepositoryException {
		log.debug("folderExists");
		if (!path.startsWith("/")) {
			log.warn("Invalid path: " + path);
			return false;
		}
		return session.nodeExists(path);
	}

	/**
	 * Starts the connection to the repository.
	 * 
	 * @throws LoginException
	 * @throws RepositoryException
	 */
	public void init() throws LoginException, RepositoryException {
		Repository repository = JcrUtils.getRepository(config.getHost() + ":"
				+ config.getPort() + "/crx/server");
		Credentials cred = new SimpleCredentials(config.getUser(), config
				.getPassword().toCharArray());
		session = repository.login(cred);
	}

	/**
	 * Closes the existing connection to the repository and reclaims all
	 * resources.
	 */
	public void shutdown() {
		if (session != null) {
			try {
				session.logout();
			} catch (Exception e) {
				log.warn("Exception closing session", e);
			}
		}
	}

	/**
	 * Uploads the specified file into the repository at the specified path.
	 * 
	 * @param file
	 *            the file to upload
	 * @param path
	 *            the path to which to upload the file
	 * @throws PathNotFoundException
	 * @throws RepositoryException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public void uploadFile(File file, String path)
			throws PathNotFoundException, RepositoryException,
			FileNotFoundException {
		log.debug("createFile");
		Node parentFolder = session.getNode(path);

		Node contentNode = null;
		if (!parentFolder.hasNode(file.getName())) {
			log.debug("Creating nodes");
			Node fileNode = parentFolder.addNode(file.getName(),
					JcrConstants.NT_FILE);
			contentNode = fileNode.addNode(JcrConstants.JCR_CONTENT,
					JcrConstants.NT_RESOURCE);
		} else {
			contentNode = parentFolder.getNode(file.getName() + "/"
					+ JcrConstants.JCR_CONTENT);
		}

		log.debug("Reading file contents");
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		Binary bin = session.getValueFactory().createBinary(is);

		log.debug("Setting data property");
		contentNode.setProperty(JcrConstants.JCR_DATA, bin);
		try {

			contentNode.setProperty(JcrConstants.JCR_MIMETYPE,
					getMimeType(file));
		} catch (Exception e) {
			log.warn("Exception retrieving mime type, will not be set", e);
		}

		log.debug("Saving changes");
		session.save();
	}
}
