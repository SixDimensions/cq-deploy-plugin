/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sixdimensions.wcm.cq.dao.webdav;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.maven.plugin.logging.Log;

import com.sixdimensions.wcm.cq.service.CQServiceConfig;

/**
 * Client functions to interact with Sling in integration tests.
 */
public class WebDavDAO {
	/**
	 * The HTTP Client instance for this DAO.
	 */
	private HttpClient httpClient;

	/**
	 * The configuration instance for this DAO.
	 */
	private final CQServiceConfig config;

	/**
	 * The logger for this DAO.
	 */
	private final Log log;

	/**
	 * Construct a new PackageManagerAPIDAO with the specified configuration.
	 * 
	 * @param config
	 *            the configuration to use
	 */
	public WebDavDAO(final CQServiceConfig config) {
		this.config = config;
		this.log = config.getLog();
	}

	/**
	 * Creates a folder at the specified path if one does not already exist.
	 * 
	 * @param path
	 *            the path at which to create the folder
	 * @throws IOException
	 */
	public void createFolder(final String path) throws IOException {
		this.log.debug("createFolder");
		int status = 0;
		final String url = this.config.getHost() + ":" + this.config.getPort()
				+ path;
		status = this.httpClient.executeMethod(new GetMethod(url + ".txt"));
		if (status != 200) {
			status = this.httpClient.executeMethod(new HttpAnyMethod("MKCOL",
					url));
			if (status != 201) {
				throw new IOException("mkdir(" + url + ") failed, status code="
						+ status);
			}
		}
	}

	/**
	 * Delete a file from the Sling repository
	 * 
	 * @return the HTTP status code
	 */
	public int delete(final String path) throws IOException {
		this.log.debug("delete");
		final DeleteMethod delete = new DeleteMethod(this.config.getHost()
				+ ":" + this.config.getPort() + path);
		return this.httpClient.executeMethod(delete);
	}

	/**
	 * Checks to see if a folder exists at the specified path.
	 * 
	 * @param path
	 *            the path to check if a folder exists, an invalid path will not
	 *            trigger an error
	 * @return true if the folder exists or false otherwise
	 * @throws IOException
	 * @throws HttpException
	 */
	public boolean folderExists(String path) throws HttpException, IOException {
		this.log.debug("folderExists");
		int status = 0;
		path += ".json";
		final String url = this.config.getHost() + ":" + this.config.getPort()
				+ path;
		this.log.debug("Checking URL: " + url);
		status = this.httpClient.executeMethod(new GetMethod(url));
		return status == 200;
	}

	/**
	 * Initializes the connection.
	 */
	public void init() {
		// setup HTTP client, with authentication
		this.httpClient = new HttpClient();
		this.httpClient.getParams().setAuthenticationPreemptive(true);
		final Credentials defaultcreds = new UsernamePasswordCredentials(
				this.config.getUser(), this.config.getPassword());
		this.httpClient.getState().setCredentials(
				new AuthScope(this.config.getHost().substring(
						this.config.getHost().lastIndexOf("/") + 1),
						Integer.parseInt(this.config.getPort()),
						AuthScope.ANY_REALM), defaultcreds);

	}

	/**
	 * Uploads the file into CQ.
	 * 
	 * @param file
	 *            The file to upload
	 * @param path
	 *            the path to which to upload the file
	 * @throws IOException
	 */
	public void uploadFile(final File file, final String path)
			throws IOException {
		this.log.debug("uploadFile");
		final String url = this.config.getHost() + ":" + this.config.getPort()
				+ path + "/" + file.getName();
		final PutMethod put = new PutMethod(url);

		put.setRequestEntity(new InputStreamRequestEntity(new FileInputStream(
				file)));
		final int status = this.httpClient.executeMethod(put);
		if ((status != 200) && (status != 201) && (status != 204)) {
			throw new IOException("uploadFile(" + url
					+ ") failed, status code=" + status);
		}
	}

}
