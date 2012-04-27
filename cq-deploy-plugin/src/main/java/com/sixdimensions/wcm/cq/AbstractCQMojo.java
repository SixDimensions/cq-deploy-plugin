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
package com.sixdimensions.wcm.cq;

import org.apache.maven.plugin.AbstractMojo;

import com.sixdimensions.wcm.cq.service.CQServiceConfig;

/**
 * Abstract class for CQ Mojos to extend, provides basic parameters and
 * configuration values needed to interact with CQ.
 * 
 * @author dklco
 */
public abstract class AbstractCQMojo extends AbstractMojo {
	/**
	 * Flag to determine whether or not to quit and throw an error when an API
	 * call fails. Default is true.
	 * 
	 * @parameter default-value=true
	 */
	protected boolean errorOnFailure = true;

	/**
	 * The host of the server to connect to, including protocol. Default is
	 * 'http://localhost'.
	 * 
	 * @parameter default-value="http://localhost"
	 */
	protected String host;

	/**
	 * The password to use when connecting. Default is 'admin'.
	 * 
	 * @parameter default-value="admin"
	 */
	protected String password;

	/**
	 * The port of the server to connect to. Default is 'admin'.
	 * 
	 * @parameter default-value="4502"
	 */
	protected String port;

	/**
	 * The username to use when connecting. Default is 'admin'.
	 * 
	 * @parameter default-value="admin"
	 */
	protected String user;

	public String getHost() {
		return host;
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

	/**
	 * Initialized the configuration object with the values passed in through
	 * the Maven configuration.
	 * 
	 * @param config
	 *            the configuration object to configure
	 */
	protected void initConfig(CQServiceConfig config) {
		getLog().debug("Instantiating configuration object.");

		config.setErrorOnFailure(errorOnFailure);
		config.setHost(host);
		config.setLog(getLog());
		config.setPassword(password);
		config.setPort(port);
		config.setUser(user);
	}

	public boolean isErrorOnFailure() {
		return errorOnFailure;
	}

	public void setErrorOnFailure(boolean errorOnFailure) {
		this.errorOnFailure = errorOnFailure;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
