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
package com.sixdimensions.wcm.cq.pack;

import java.io.File;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;

import com.sixdimensions.wcm.cq.AbstractCQMojo;
import com.sixdimensions.wcm.cq.pack.service.PackageManagerConfig;
import com.sixdimensions.wcm.cq.pack.service.PackageManagerService;

/**
 * Goal which uploads and installs a package to Adobe CQ.
 * 
 * @goal install-package
 * 
 * @phase install
 */
public class PackageMojo extends AbstractCQMojo implements Mojo {

	/**
	 * Flag to determine whether or not to first delete the package before
	 * uploading. Default is false.
	 * 
	 * @parameter default-value=false
	 */
	private boolean deleteFirst = true;

	/**
	 * Location of the file. Default is
	 * "${project.artifactId}-${project.version}.${project.packaging}"
	 * 
	 * @parameter expression=
	 *            "${project.build.directory}/${project.artifactId}-${project.version}.${project.packaging}"
	 * @required
	 */
	private File packageFile;

	/**
	 * The path to upload the package to. Default is
	 * "${project.artifactId}-${project.version}.${project.packaging}"
	 * 
	 * @parameter
	 * @required expression=
	 *           "${project.build.directory}/${project.artifactId}-${project.version}.${project.packaging}"
	 */
	private String path;

	/**
	 * Flag to determine whether or not to only upload and not install the
	 * package. False by default.
	 * 
	 * @parameter default-value=false
	 */
	private boolean uploadOnly;

	/**
	 * Flag to determine whether or not to use the Legacy API. False by default.
	 * 
	 * @parameter default-value=false
	 */
	private boolean useLegacy;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	public void execute() throws MojoExecutionException {
		getLog().info("execute");

		PackageManagerConfig config = new PackageManagerConfig();
		initConfig(config);
		config.setUseLegacy(useLegacy);

		getLog().debug("Retrieving service");
		PackageManagerService packageMgrSvc = PackageManagerService.Factory
				.getPackageMgr(config);
		try {
			if(deleteFirst){
				packageMgrSvc.delete(path);
			}
			packageMgrSvc.upload(path, packageFile);
			getLog().info("Package upload successful");
			if (!this.uploadOnly) {
				packageMgrSvc.install(path);
				getLog().info("Package installation successful");
			}
		} catch (Exception e) {
			getLog().error("Exception uploading/installing package.", e);
			throw new MojoExecutionException(
					"Exception uploading/installing package.", e);
		}
		getLog().info("Package Upload/Installation Completed Successfully");
	}
}