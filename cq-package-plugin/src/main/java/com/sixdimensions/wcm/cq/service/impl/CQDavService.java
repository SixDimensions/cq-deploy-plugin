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
package com.sixdimensions.wcm.cq.service.impl;

import java.io.File;

import org.apache.maven.plugin.logging.Log;

import com.sixdimensions.wcm.cq.dao.JCRDAO;
import com.sixdimensions.wcm.cq.service.CQService;
import com.sixdimensions.wcm.cq.service.CQServiceConfig;

public class CQDavService implements CQService {

	private Log log;
	private CQServiceConfig config;
	private JCRDAO jcrDAO;

	public CQDavService(CQServiceConfig config) {
		log = config.getLog();
		this.config = config;
		//jcrDAO = new JCRDAO(config);
	}

	/**
	 * Generates a url from the specified path and current configuration.
	 * 
	 * @param path
	 *            the path of the package to be updated
	 * @return the url
	 */
	protected String assembleUrl(String path) {
		log.debug("assembleUrl");
		return config.getHost() + ":" + config.getPort() + path;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sixdimensions.wcm.cq.service.CQService#createFolder(java.lang.String)
	 */
	public void createFolder(String path) throws Exception {
		log.debug("createFolder");

		String[] parts = path.split("/");

		StringBuffer createPath = new StringBuffer();
		for (int i = 1; i < parts.length; i++) {
			createPath.append("/" + parts[i]);
			if (jcrDAO.folderExists(createPath.toString())) {
				jcrDAO.createFolder(createPath.toString());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sixdimensions.wcm.cq.service.CQService#uploadFile(java.io.File,
	 * java.lang.String)
	 */
	public void uploadFile(File file, String path) throws Exception {
		log.debug("uploadFile");

		log.debug("Uploading file " + file.getAbsolutePath() + " to  path: "
				+ path);
		createFolder(path);
		jcrDAO.createFile(file, path);
	}

}
