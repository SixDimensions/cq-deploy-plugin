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
package com.sixdimensions.wcm.cq.vlt;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.sixdimensions.wcm.cq.AbstractCQMojo;
import com.sixdimensions.wcm.cq.service.CQServiceConfig;

public class VLTInstallMojo extends AbstractCQMojo {

	/**
	 * The paths of the folders to be imported into CQ.
	 * 
	 * @required
	 */
	private String[] paths;

	/**
	 * Root of the files for VLT to act upon.
	 * 
	 * @parameter default-value="jcr_root"
	 * @required
	 */
	private String root;

	/**
	 * A file reference to the VLT Executable.
	 * 
	 * @required
	 */
	private File vltExe;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sixdimensions.wcm.cq.AbstractCQMojo#initConfig(com.sixdimensions.
	 * wcm.cq.service.CQServiceConfig)
	 */
	@Override
	protected void initConfig(CQServiceConfig cfg) {
		super.initConfig(cfg);
		CQVLTServiceConfig config = (CQVLTServiceConfig) cfg;
		config.setRoot(root);
		config.setVltExe(vltExe);
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("execute");

		CQServiceConfig config = new CQVLTServiceConfig();
		initConfig(config);
		
		//TODO: Actually run the import
	}

}
