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

import com.sixdimensions.wcm.cq.service.CQServiceConfig;

/**
 * Configuration file for the package manager. Passes all of the standard
 * configuration values needed to create an instance of the package manager.
 * 
 * @author dklco
 */
public class PackageManagerConfig extends CQServiceConfig {

	private boolean useLegacy = false;

	public boolean isUseLegacy() {
		return useLegacy;
	}

	public void setUseLegacy(boolean useLegacy) {
		this.useLegacy = useLegacy;
	}

}
