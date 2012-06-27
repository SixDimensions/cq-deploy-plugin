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

/**
 * Represents an Access Control Handling value.
 * 
 * @author dklco
 */
public enum AC_HANDLING {
	clear("clear"), DEFAULT(""), ignore("ignore"), merge("merge"), merge_preserve(
			"merge_preserve"), overwrite("overwrite");

	/**
	 * The internal value string
	 */
	private final String value;

	/**
	 * Construct a new AC_HANDLING with the specified string
	 * 
	 * @param cmd
	 *            the string representation of the value for this AC_HANDLING
	 *            instance
	 */
	AC_HANDLING(String value) {
		this.value = value;
	}

	/**
	 * Gets the value string.
	 * 
	 * @return the value string
	 */
	public String getValue() {
		return value;
	}
}