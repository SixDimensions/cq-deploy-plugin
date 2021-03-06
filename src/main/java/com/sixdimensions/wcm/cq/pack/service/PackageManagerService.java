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
package com.sixdimensions.wcm.cq.pack.service;

import java.io.File;

import com.sixdimensions.wcm.cq.pack.service.impl.LegacyPackageManagerServiceImpl;
import com.sixdimensions.wcm.cq.pack.service.impl.PackageManagerServiceImpl;

/**
 * A Package Manager interacts with the CQ Package Manager HTTP API to manage CQ
 * Packages.
 * 
 * @author dklco
 */
public interface PackageManagerService {
	/**
	 * Factory for creating instances of the PackageManagerService. Using this
	 * you don't need to have any knowledge of the implementing class.
	 */
	static class Factory {
		/**
		 * Get an instance of the Package Manager Service.
		 * 
		 * @param config
		 *            the configuration used it instantiate the Package Manager
		 *            Service.
		 * @return the instance of the Package Manager Service
		 */
		public static PackageManagerService getPackageMgr(
				final PackageManagerConfig config) {
			PackageManagerService svc = null;
			if (config.isUseLegacy()) {
				svc = new LegacyPackageManagerServiceImpl(config);
			} else {
				svc = new PackageManagerServiceImpl(config);
			}
			return svc;
		}
	}

	/**
	 * The base path under which the packages are stored.
	 */
	public static final String PACKAGES_BASE_PATH = "/etc/packages";

	/**
	 * Deletes the package at the specified path. Attempting to delete a file
	 * that does not exist will not raise an exception.
	 * 
	 * @param path
	 *            the path of the file to delete
	 * @throws Exception
	 *             an exception occurs deleting the file
	 */
	public void delete(String path) throws Exception;

	/**
	 * Perform a dry run of an installation of the package.
	 * 
	 * @param path
	 *            the path of the package to install
	 * @throws Exception
	 *             an exception occurs attempting to dry run the installation
	 */
	public void dryRun(String path) throws Exception;

	/**
	 * Install the package at the specified path.
	 * 
	 * @param path
	 *            the path of the package to install
	 * @throws Exception
	 *             an exception occurs attempting to install the package
	 */
	public void install(String path) throws Exception;

	/**
	 * Uploads a package into CQ.
	 * 
	 * @param path
	 *            the path to upload the package to
	 * @param pkg
	 *            the package to upload
	 * @throws Exception
	 *             an exception occurs attempting to upload the package
	 */
	public void upload(String path, File pkg) throws Exception;
}
