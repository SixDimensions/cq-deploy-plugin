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
package com.sixdimensions.wcm.cq.pack.service;

import org.apache.maven.plugin.logging.Log;

/**
 * Configuration file for the package manager. Passes all of the standard
 * configuration values needed to create an instance of the package manager.
 * 
 * @author dklco
 */
public class PackageManagerConfig {

	private Log log;
	private boolean errorOnFailure = true;
	private String host = "http://localhost";
	private String port = "4502";
	private String user ="admin";
	private String password = "admin";
	private boolean useLegacy = false;

	public String getHost() {
		return host;
	}

	public Log getLog() {
		return log;
	}

	public String getPassword() {
		return password;
	}

	public String getPort() {
		return port;
	}

	public String getUser() {
		return user;
	}

	public boolean isErrorOnFailure() {
		return errorOnFailure;
	}

	public boolean isUseLegacy() {
		return useLegacy;
	}

	public void setErrorOnFailure(boolean errorOnFailure) {
		this.errorOnFailure = errorOnFailure;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setUseLegacy(boolean useLegacy) {
		this.useLegacy = useLegacy;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
