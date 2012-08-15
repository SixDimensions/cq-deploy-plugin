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

import com.sixdimensions.wcm.cq.service.CQServiceConfig;

/**
 * Configuration file for the package manager. Passes all of the standard
 * configuration values needed to create an instance of the package manager.
 * 
 * @author dklco
 */
public class PackageManagerConfig extends CQServiceConfig {

	/**
	 * The Access Control handling.
	 */
	private AC_HANDLING acHandling = AC_HANDLING.DEFAULT;

	/**
	 * How frequently to save, defaults to 1024.
	 */
	private int autosave = 1024;

	/**
	 * Whether to recursively load packages inside this package.
	 */
	private boolean recursive = true;

	/**
	 * Whether or not to use the legacy API.
	 */
	private boolean useLegacy = false;

	/**
	 * Gets the Access Control Handling.
	 * 
	 * @return the access control handling
	 */
	public AC_HANDLING getAcHandling() {
		return this.acHandling;
	}

	/**
	 * Gets the autosave interval.
	 * 
	 * @return the autosave interval
	 */
	public int getAutosave() {
		return this.autosave;
	}

	/**
	 * Gets the recusive flag.
	 * 
	 * @return the recursive flag
	 */
	public boolean isRecursive() {
		return this.recursive;
	}

	/**
	 * Gets the legacy flag.
	 * 
	 * @return the legacy flag
	 */
	public boolean isUseLegacy() {
		return this.useLegacy;
	}

	/**
	 * Set the Access control handling.
	 * 
	 * @param acHandling
	 *            the access control handling
	 */
	public void setAcHandling(final AC_HANDLING acHandling) {
		this.acHandling = acHandling;
	}

	/**
	 * Set the auto save interval.
	 * 
	 * @param autosave
	 *            the auto save interval
	 */
	public void setAutosave(final int autosave) {
		this.autosave = autosave;
	}

	/**
	 * Set the recursive flag.
	 * 
	 * @param recursive
	 *            the recursive flag
	 */
	public void setRecursive(final boolean recursive) {
		this.recursive = recursive;
	}

	/**
	 * Set the use legacy API flag.
	 * 
	 * @param useLegacy
	 *            the use legacy api flag
	 */
	public void setUseLegacy(final boolean useLegacy) {
		this.useLegacy = useLegacy;
	}

}
