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
package com.sixdimensions.wcm.cq.vlt.service;

import com.sixdimensions.wcm.cq.vlt.CQVLTServiceConfig;
import com.sixdimensions.wcm.cq.vlt.service.impl.VLTServiceImpl;

public interface VLTService {

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
		public static VLTService getService(CQVLTServiceConfig config) {
			return new VLTServiceImpl(config);
		}
	}

	/**
	 * Import the files at the specified paths. The paths are relative to the
	 * Root set in the service configuration.
	 * 
	 * @param paths
	 *            the paths to import
	 */
	public void importFiles(String[] paths);
}
