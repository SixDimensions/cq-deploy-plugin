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
package com.sixdimensions.wcm.cq.service;

import java.io.File;

import com.sixdimensions.wcm.cq.service.impl.CQJCRService;

/**
 * Service for interacting with CQ over the WebDav protocol. Essentially using
 * HTTP Requests.
 * 
 * @author dklco
 */
public interface CQService {

	/**
	 * Factory for creating instances of the CQDavService. Using this you don't
	 * need to have any knowledge of the implementing class.
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
		public static CQService getService(CQServiceConfig config) {
			CQService svc = new CQJCRService(config);
			return svc;
		}
	}

	/**
	 * Create a folder at the specified path.
	 * 
	 * @param path
	 *            the path at which to create the folder
	 */
	public void createFolder(String path) throws Exception;

	/**
	 * Deletes everything under the specified path.
	 * 
	 * @param path
	 *            the path to delete
	 */
	public void delete(String path) throws Exception;

	/**
	 * Uploads the specified file to the specified path.
	 * 
	 * @param file
	 *            the file to upload
	 * @param path
	 *            the path to which to upload the file
	 * @throws Exception
	 */
	public void uploadFile(File file, String path) throws Exception;
}
