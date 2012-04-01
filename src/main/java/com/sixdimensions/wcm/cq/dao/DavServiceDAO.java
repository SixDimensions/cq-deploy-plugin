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
package com.sixdimensions.wcm.cq.dao;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.jackrabbit.webdav.client.methods.MkColMethod;
import org.apache.maven.plugin.logging.Log;

import com.sixdimensions.wcm.cq.service.CQServiceConfig;

/**
 * Performs interactions with the CQ Dav API.
 * 
 * @author klcodanr
 */
public class DavServiceDAO {

	private CQServiceConfig config;
	private Log log;

	/**
	 * Construct a new PackageManagerAPIDAO with the specified configuration.
	 * 
	 * @param config
	 *            the configuration to use
	 */
	public DavServiceDAO(CQServiceConfig config) {
		this.config = config;
		this.log = config.getLog();
	}

	public void createFolder(String url) {
		log.debug("createFolder");
		//TODO: Interact with WebDav
	}
}
