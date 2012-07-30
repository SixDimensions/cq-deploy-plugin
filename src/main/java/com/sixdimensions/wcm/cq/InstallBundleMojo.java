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
package com.sixdimensions.wcm.cq;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;

import com.sixdimensions.wcm.cq.service.CQService;
import com.sixdimensions.wcm.cq.service.CQServiceConfig;

/**
 * Installs the specified bundle into Adobe CQ to a configurable path. This path
 * should be something like /apps/{APP_NAME}/install so it will be picked up by
 * the CQ Bundle installer.
 * 
 * @author dklco
 * @phase install
 * @goal install-bundle
 */
public class InstallBundleMojo extends AbstractCQMojo {

	/**
	 * Location of the bundle file. Default is
	 * "${project.artifactId}-${project.version}.jar"
	 * 
	 * @parameter expression=
	 *            "${project.build.directory}/${project.artifactId}-${project.version}.jar"
	 * @required
	 */
	private File bundleFile;

	/**
	 * The path to upload the package to. If the app name is not specified the
	 * default is "/apps/bundles/install" if the app.name property is specified
	 * the path will be /apps/${app.name}/install
	 * 
	 * @parameter default-value="/apps/${app.name}/install"
	 */
	private String path;

	/**
	 * Whether or not to skip the installation of the bundle. If true, the
	 * installation will be skipped and the plugin process reported to be
	 * successful.
	 * 
	 * @parameter default-value=false
	 */
	private boolean skipInstall = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	public void execute() throws MojoExecutionException {
		getLog().info("execute");

		if (!skipInstall) {
			getLog().info("Initializing");

			if (this.getPath().contains("${app.name}")) {
				getLog().debug("No app name set, using /apps/bundles/install");
				this.setPath(this.getPath().replace("${app.name}", "bundles"));
			}
			CQServiceConfig config = new CQServiceConfig();
			initConfig(config);

			getLog().info(
					"Connecting to server: " + config.getHost() + ":"
							+ config.getPort());
			getLog().info("Connecting with user: " + config.getUser());
			CQService cqSvc = CQService.Factory.getService(config);

			try {
				getLog().info("Creating folders: " + path);
				cqSvc.createFolder(path);

				getLog().info(
						"Uploading bundle " + bundleFile.getAbsolutePath()
								+ " to path " + path);
				cqSvc.uploadFile(bundleFile, path);

				getLog().info("Bundle installation complete");
			} catch (Exception e) {
				getLog().error("Exeption installing bundle: " + e.getMessage(),
						e);
				throw new MojoExecutionException("Exception installing bundle",
						e);
			}
		} else {
			getLog().info("Skipping installation");
		}
	}

	/**
	 * Returns the bundle file to be installed.
	 * 
	 * @return the bundle file to be installed
	 */
	public File getBundleFile() {
		return bundleFile;
	}

	/**
	 * Retrieves the path inside the JCR to which to install the bundle
	 * 
	 * @return the path inside the JCR to which to install the bundle
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Returns true if the installation should be skipped. Can be used to on
	 * multi-level builds to only install the bundles once.
	 * 
	 * @return the skip installation flag value
	 */
	public boolean getSkipInstall() {
		return skipInstall;
	}

	/**
	 * Sets the bundle file to be loaded.
	 * 
	 * @param bundleFile
	 *            the bundle file to load and install
	 */
	public void setBundleFile(File bundleFile) {
		this.bundleFile = bundleFile;
	}

	/**
	 * Sets the path in the JCR to which the bundle file will be loaded.
	 * 
	 * @param path
	 *            the path in the JCR in which the bundle will be loaded.
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Sets the skip installation flag. If set the installation of the bundle
	 * will be skipped.
	 * 
	 * @param skipInstall
	 *            the skip installation flag
	 */
	public void setSkipInstall(boolean skipInstall) {
		this.skipInstall = skipInstall;
	}

}
