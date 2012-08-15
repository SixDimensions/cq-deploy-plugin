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
package com.sixdimensions.wcm.cq.service;

import org.apache.maven.plugin.logging.Log;

/**
 * Configuration file for CQ Services. Passes all of the standard configuration
 * values needed to create an instance of the CQ Service.
 * 
 * @author dklco
 */
public class CQServiceConfig {

	private Log log;
	private boolean errorOnFailure = true;
	private String host = "http://localhost";
	private String port = "4502";
	private String user = "admin";
	private String password = "admin";

	public String getHost() {
		return this.host;
	}

	public Log getLog() {
		return this.log;
	}

	public String getPassword() {
		return this.password;
	}

	public String getPort() {
		return this.port;
	}

	public String getUser() {
		return this.user;
	}

	public boolean isErrorOnFailure() {
		return this.errorOnFailure;
	}

	public void setErrorOnFailure(final boolean errorOnFailure) {
		this.errorOnFailure = errorOnFailure;
	}

	public void setHost(final String host) {
		this.host = host;
	}

	public void setLog(final Log log) {
		this.log = log;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setPort(final String port) {
		this.port = port;
	}

	public void setUser(final String user) {
		this.user = user;
	}
}
