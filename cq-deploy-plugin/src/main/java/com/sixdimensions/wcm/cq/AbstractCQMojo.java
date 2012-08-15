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
	 * The port of the server to connect to. Default is '4502'.
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

	/**
	 * Retrieves host of the server to connect to, including protocol. Default
	 * is 'http://localhost'.
	 * 
	 * @return the host
	 */
	public final String getHost() {
		return this.host;
	}

	/**
	 * Retrieves the password to use when connecting. Default is 'admin'.
	 * 
	 * @return the password
	 */
	public final String getPassword() {
		return this.password;
	}

	/**
	 * The port of the server to connect to. Default is '4502'.
	 * 
	 * @return the server port to connect to
	 */
	public final String getPort() {
		return this.port;
	}

	/**
	 * The username to use when connecting. Default is 'admin'.
	 * 
	 * @return the username
	 */
	public final String getUser() {
		return this.user;
	}

	/**
	 * Initialized the configuration object with the values passed in through
	 * the Maven configuration.
	 * 
	 * @param config
	 *            the configuration object to configure
	 */
	protected void initConfig(final CQServiceConfig config) {
		this.getLog().debug("Instantiating configuration object.");

		config.setErrorOnFailure(this.errorOnFailure);
		config.setHost(this.host);
		config.setLog(this.getLog());
		config.setPassword(this.password);
		config.setPort(this.port);
		config.setUser(this.user);
	}

	/**
	 * Flag to determine whether or not to quit and throw an error when an API
	 * call fails. Default is true.
	 * 
	 * @return the error on failure flag
	 */
	public final boolean isErrorOnFailure() {
		return this.errorOnFailure;
	}

	/**
	 * Sets the flag to determine whether or not to quit and throw an error when
	 * an API call fails.
	 * 
	 * @param errorOnFailure
	 *            the error on failure flag
	 */
	public final void setErrorOnFailure(final boolean errorOnFailure) {
		this.errorOnFailure = errorOnFailure;
	}

	/**
	 * Sets the host of the server to connect to, must include the protocol,
	 * must not be null.
	 * 
	 * @param host
	 *            the host to connect to
	 */
	public void setHost(final String host) {
		this.host = host;
	}

	/**
	 * Sets the password to use when connecting, must not be null.
	 * 
	 * @param password
	 *            the password
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * The port of the server to connect to, this should always be set, even if
	 * it's a standard port for the protocol (ex: 80).
	 * 
	 * @param port
	 *            the port to connect to
	 */
	public void setPort(final String port) {
		this.port = port;
	}

	/**
	 * Sets the username to use when connecting, must not be null.
	 * 
	 * @param user
	 *            the username
	 */
	public void setUser(final String user) {
		this.user = user;
	}
}
