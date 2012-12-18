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

import java.io.File;

import org.apache.maven.plugin.logging.Log;
import org.json.JSONObject;

import com.sixdimensions.wcm.cq.dao.HTTPServiceDAO;
import com.sixdimensions.wcm.cq.pack.service.PackageManagerConfig;
import com.sixdimensions.wcm.cq.pack.service.PackageManagerService;

/**
 * Implementation of the Package Manager Service based on the new CQ Package
 * Manager API.
 * 
 * @author klcodanr
 */
public class PackageManagerServiceImpl implements PackageManagerService {
	private static enum COMMAND {
		DELETE("?cmd=delete"), DRY_RUN("?cmd=dryrun"), INSTALL("?cmd=install"), UPLOAD(
				"?cmd=upload");
		private final String cmd;

		COMMAND(final String cmd) {
			this.cmd = cmd;
		}

		public String getCmd() {
			return this.cmd;
		}
	}

	private static final String PARAM_AC_HANDLING = "acHandling";
	private static final String PARAM_AUTOSAVE = "autosave";
	private static final String PARAM_RECURSIVE = "recursive";
	private static final String FILE_KEY = "package";
	private static final String MESSAGE_KEY = "msg";
	private static final String PACK_MGR_PATH = "/crx/packmgr/service/.json";
	private static final String PACKAGE_BASE_PATH = "/etc/packages/";
	private static final String SUCCESS_KEY = "success";
	private final PackageManagerConfig config;
	private final Log log;
	private final HTTPServiceDAO pmAPI;

	/**
	 * Create a new Package Manager Service instance.
	 * 
	 * @param config
	 *            the configuration with which to instantiate the package
	 *            manager service
	 */
	public PackageManagerServiceImpl(final PackageManagerConfig config) {
		this.log = config.getLog();
		this.config = config;
		this.pmAPI = new HTTPServiceDAO(config);
	}

	/**
	 * Generates a url from the specified path and configuration.
	 * 
	 * @param path
	 *            the path of the package to be updated
	 * @return the url
	 */
	protected String assembleUrl(final String path) {
		this.log.debug("assembleUrl");
		if (path.startsWith("/")) {
			return this.config.getHost() + ":" + this.config.getPort()
					+ PACK_MGR_PATH + path;
		} else {
			return this.config.getHost() + ":" + this.config.getPort()
					+ PACK_MGR_PATH + PACKAGE_BASE_PATH + path;
		}
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
		final String responseStr = new String(this.pmAPI.doPost(this
				.assembleUrl(path) + COMMAND.DELETE.getCmd()), "UTF-8");
		this.log.debug("Response: " + responseStr);

		final JSONObject result = new JSONObject(responseStr);
		this.log.debug("Succeeded: " + result.getBoolean(SUCCESS_KEY));
		this.log.debug("Message: " + result.getString(MESSAGE_KEY));

		if (result.getBoolean(SUCCESS_KEY)) {
			this.log.info("Delete succeeded");
		} else {
			this.log.warn("Delete failed: " + result.getString(MESSAGE_KEY));
		}

		if (!result.getBoolean(SUCCESS_KEY) && this.config.isErrorOnFailure()) {
			if (path.endsWith(".jar")) {
				this.log.warn("Delete failed with jar, trying with zip.");
				this.delete(path.replace(".jar", ".zip"));
			} else {
				throw new Exception("Failed to complete delete: "
						+ result.getString(MESSAGE_KEY));
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
		this.log.debug("dryRun");

		this.log.info("Performing Dry Run on package at path: " + path);
		final String responseStr = new String(this.pmAPI.doPost(this
				.assembleUrl(path) + COMMAND.DRY_RUN.getCmd()), "UTF-8");
		this.log.debug("Response: " + responseStr);

		final JSONObject result = new JSONObject(responseStr);
		this.log.debug("Succeeded: " + result.getBoolean(SUCCESS_KEY));
		this.log.debug("Message: " + result.getString(MESSAGE_KEY));

		if (result.getBoolean(SUCCESS_KEY)) {
			this.log.info("Dry Run succeeded");
		} else {
			this.log.warn("Dry Run failed: " + result.getString(MESSAGE_KEY));
		}

		if (!result.getBoolean(SUCCESS_KEY) && this.config.isErrorOnFailure()) {
			if (path.endsWith(".jar")) {
				this.log.warn("Dry run failed with jar, trying with zip.");
				this.dryRun(path.replace(".jar", ".zip"));
			} else {
				throw new Exception("Failed to complete installation dry run: "
						+ result.getString(MESSAGE_KEY));
			}
		}
	}

	/**
	 * Creates a String representation of the extended installation parameters
	 * 
	 * @return the extended installation parameters
	 */
	private String getInstallParameters() {
		this.log.debug("getInstallParameters");
		String paramStr = "&" + PARAM_AUTOSAVE + "="
				+ this.config.getAutosave();
		paramStr += "&" + PARAM_AC_HANDLING + "="
				+ this.config.getAcHandling().getValue();
		paramStr += "&" + PARAM_RECURSIVE + "=" + this.config.isRecursive();
		this.log.debug("Retrieved parameters: " + paramStr);
		return paramStr;
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
		final String responseStr = new String(this.pmAPI.doPost(this
				.assembleUrl(path)
				+ COMMAND.INSTALL.getCmd()
				+ this.getInstallParameters()), "UTF-8");
		this.log.debug("Response: " + responseStr);

		final JSONObject result = new JSONObject(responseStr);
		this.log.debug("Succeeded: " + result.getBoolean(SUCCESS_KEY));
		this.log.debug("Message: " + result.getString(MESSAGE_KEY));

		if (result.getBoolean(SUCCESS_KEY)) {
			this.log.info("Installation succeeded");
		} else {
			this.log.warn("Installation failed: "
					+ result.getString(MESSAGE_KEY));
		}

		if (!result.getBoolean(SUCCESS_KEY) && this.config.isErrorOnFailure()) {
			if (path.endsWith(".jar")) {
				this.log.warn("Installation failed with jar, trying with zip.");
				this.install(path.replace(".jar", ".zip"));
			} else {
				throw new Exception("Failed to complete installation: "
						+ result.getString(MESSAGE_KEY));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sixdimensions.wcm.cq.pack.service.PackageManagerService#upload(java
	 * .lang.String, java.io.File)
	 */
	public void upload(final String path, final File pkg) throws Exception {
		this.log.debug("upload");

		this.log.info("Uploading package " + pkg.getAbsolutePath()
				+ " to path: " + path);
		final String responseStr = new String(
				this.pmAPI.postFile(
						this.assembleUrl(path) + COMMAND.UPLOAD.getCmd(),
						FILE_KEY, pkg), "UTF-8");
		this.log.debug("Response: " + responseStr);
		final JSONObject result = new JSONObject(responseStr);

		this.log.debug("Succeeded: " + result.getBoolean(SUCCESS_KEY));
		this.log.debug("Message: " + result.getString(MESSAGE_KEY));

		if (result.getBoolean(SUCCESS_KEY)) {
			this.log.info("Upload succeeded");
		} else {
			this.log.warn("Upload failed: " + result.getString(MESSAGE_KEY));
		}
		if (!result.getBoolean(SUCCESS_KEY) && this.config.isErrorOnFailure()) {
			throw new Exception("Failed to upload package: "
					+ result.getString(MESSAGE_KEY));
		}
	}
}
