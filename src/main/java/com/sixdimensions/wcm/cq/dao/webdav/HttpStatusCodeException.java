/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.sixdimensions.wcm.cq.dao.webdav;

import java.io.IOException;

/**
 * Exception thrown when the expected status code is not returned.
 * 
 * 
 */
@SuppressWarnings("serial")
public class HttpStatusCodeException extends IOException {

	/**
	 * The expected status.
	 */
	private final int expectedStatus;

	/**
	 * The actual status returned.
	 */
	private final int actualStatus;

	/**
	 * Construct a new Http Status Code Exception instance.
	 * 
	 * @param expectedStatus
	 *            the expected status
	 * @param actualStatus
	 *            the actual status
	 * @param method
	 *            the http method invoked
	 * @param url
	 *            the url invoked
	 */
	public HttpStatusCodeException(final int expectedStatus,
			final int actualStatus, final String method, final String url) {
		this(expectedStatus, actualStatus, method, url, null);
	}

	/**
	 * Construct a new Http Status Code Exception instance.
	 * 
	 * @param expectedStatus
	 *            the expected status
	 * @param actualStatus
	 *            the actual status
	 * @param method
	 *            the http method invoked
	 * @param url
	 *            the url invoked
	 * @param content
	 *            the content returned from the request
	 */
	public HttpStatusCodeException(final int expectedStatus,
			final int actualStatus, final String method, final String url,
			final String content) {
		super("Expected status code " + expectedStatus + " for " + method
				+ ", got " + actualStatus + ", URL=" + url
				+ (content != null ? ", Content=[" + content + "]" : ""));
		this.expectedStatus = expectedStatus;
		this.actualStatus = actualStatus;
	}

	/**
	 * Gets the actual status.
	 * 
	 * @return the actual status
	 */
	public int getActualStatus() {
		return this.actualStatus;
	}

	/**
	 * Gets the expected status.
	 * 
	 * @return the expected status
	 */
	public int getExpectedStatus() {
		return this.expectedStatus;
	}
}
