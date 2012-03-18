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
package com.sixdimensions.wcm.cq.pack.service.impl;

import java.io.File;

import org.apache.maven.plugin.logging.Log;
import org.json.JSONObject;

import com.sixdimensions.wcm.cq.pack.dao.PackageManagerAPIDAO;
import com.sixdimensions.wcm.cq.pack.service.PackageManagerConfig;
import com.sixdimensions.wcm.cq.pack.service.PackageManagerService;

public class PackageManagerServiceImpl implements PackageManagerService {
	private PackageManagerAPIDAO pmAPI;
	private Log log;
	private PackageManagerConfig config;
	private static final String PACK_MGR_PATH = "/crx/packmgr/service/.json";
	private static final String PACKAGE_BASE_PATH = "/etc/packages/";
	private static final String DELETE_COMMAND = "?cmd=delete";
	private static final String PREVIEW_COMMAND = "?cmd=preview";
	private static final String SUCCESS_KEY = "success";
	private static final String MESSAGE_KEY = "msg";

	public PackageManagerServiceImpl(PackageManagerConfig config) {
		log = config.getLog();
		this.config = config;
		pmAPI = new PackageManagerAPIDAO(config);
	}

	protected String assembleUrl(String path) {
		log.debug("assembleUrl");
		if (path.startsWith("/")) {
			return config.getHost() + ":" + config.getPort() + PACK_MGR_PATH
					+ path;
		} else {
			return config.getHost() + ":" + config.getPort() + PACK_MGR_PATH
					+ PACKAGE_BASE_PATH + path;
		}
	}

	public void delete(String path) throws Exception {
		log.debug("delete");
		JSONObject result = pmAPI.doPost(assembleUrl(path) + DELETE_COMMAND,
				null);

		log.debug("Succeeded: " + result.getBoolean(SUCCESS_KEY));
		log.debug("Message: " + result.getString(MESSAGE_KEY));

		if (!result.getBoolean(SUCCESS_KEY) && config.isErrorOnFailure()) {
			throw new Exception("Failed to delete package: "
					+ result.getString(MESSAGE_KEY));
		}
	}

	public void dryRun(String path) throws Exception {
		// TODO Auto-generated method stub

	}

	public void install(String path) throws Exception {
		// TODO Auto-generated method stub

	}

	public void upload(String path, File pkg) throws Exception {
		// TODO Auto-generated method stub

	}

	public boolean validatePath(String path) {
		// TODO Auto-generated method stub
		return false;
	}

	public void preview(String path) throws Exception {
		log.debug("preview");
		JSONObject result = pmAPI.doPost(assembleUrl(path) + PREVIEW_COMMAND,
				null);

		log.debug("Succeeded: " + result.getBoolean(SUCCESS_KEY));
		log.debug("Message: " + result.getString(MESSAGE_KEY));

		if (!result.getBoolean(SUCCESS_KEY) && config.isErrorOnFailure()) {
			throw new Exception("Failed to preview package: "
					+ result.getString(MESSAGE_KEY));
		}
	}

}
