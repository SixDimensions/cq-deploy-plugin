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
package com.sixdimensions.wcm.cq.service.impl;

import java.io.File;

import org.apache.maven.plugin.logging.Log;

import com.sixdimensions.wcm.cq.dao.webdav.WebDavDAO;
import com.sixdimensions.wcm.cq.service.CQService;
import com.sixdimensions.wcm.cq.service.CQServiceConfig;

public class CQWebDavService implements CQService {

	private final Log log;
	private final CQServiceConfig config;
	private final WebDavDAO webDavDAO;

	public CQWebDavService(final CQServiceConfig config) {
		this.log = config.getLog();
		this.config = config;
		this.webDavDAO = new WebDavDAO(config);
	}

	/**
	 * Generates a url from the specified path and current configuration.
	 * 
	 * @param path
	 *            the path of the package to be updated
	 * @return the url
	 */
	protected String assembleUrl(final String path) {
		this.log.debug("assembleUrl");
		return this.config.getHost() + ":" + this.config.getPort() + path;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sixdimensions.wcm.cq.service.CQService#createFolder(java.lang.String)
	 */
	public void createFolder(final String path) throws Exception {
		this.log.debug("createFolder");

		this.webDavDAO.init();
		if (!this.webDavDAO.folderExists(path)) {
			final String[] parts = path.split("/");

			final StringBuffer createPath = new StringBuffer();
			for (int i = 1; i < parts.length; i++) {
				createPath.append("/" + parts[i]);
				if (!this.webDavDAO.folderExists(createPath.toString())) {
					this.webDavDAO.createFolder(createPath.toString());
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sixdimensions.wcm.cq.service.CQService#delete(java.lang.String)
	 */
	public void delete(final String path) throws Exception {
		this.log.debug("delete");

		this.webDavDAO.init();
		this.webDavDAO.delete(path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sixdimensions.wcm.cq.service.CQService#uploadFile(java.io.File,
	 * java.lang.String)
	 */
	public void uploadFile(final File file, final String path) throws Exception {
		this.log.debug("uploadFile");

		this.log.debug("Uploading file " + file.getAbsolutePath()
				+ " to  path: " + path);

		this.createFolder(path);

		this.webDavDAO.init();
		this.webDavDAO.uploadFile(file, path);
	}

}
