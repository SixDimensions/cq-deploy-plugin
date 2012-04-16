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

import java.io.File;

import javax.jcr.Credentials;
import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.maven.plugin.logging.Log;

import com.sixdimensions.wcm.cq.service.CQServiceConfig;

/**
 * Performs interactions with the CQ Dav API.
 * 
 * @author klcodanr
 */
public class JCRDAO {

	private CQServiceConfig config;
	private Log log;
	private Session session;

	/**
	 * Construct a new PackageManagerAPIDAO with the specified configuration.
	 * 
	 * @param config
	 *            the configuration to use
	 */
	public JCRDAO(CQServiceConfig config) {
		this.config = config;
		this.log = config.getLog();
	}

	public void init() throws LoginException, RepositoryException {
		Repository repository = JcrUtils.getRepository(config.getHost() + ":"
				+ config.getPort() + "/crx/server");
		Credentials cred = new SimpleCredentials(config.getUser(), config
				.getPassword().toCharArray());
		session = repository.login(cred);
	}

	public void createFolder(String path) {
		log.debug("createFolder");
		//f(session.)
	}

	public boolean folderExists(String path) {
		log.debug("folderExists");
		return false; 
	}

	public void createFile(File file, String path) {
		// TODO Auto-generated method stub

	}

	public void shutdown() {
		if(session != null){
			try{
				session.logout();
			}catch(Exception e){
				log.warn("Exception closing session",e);
			}
		}
	}
}
