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
package com.sixdimensions.wcm.cq.dao;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.maven.plugin.logging.Log;

import com.sixdimensions.wcm.cq.service.CQServiceConfig;

/**
 * Performs interactions with the CQ Package API including handling connections
 * and parsing the response.
 * 
 * @author dklco
 */
public class HTTPServiceDAO {
	/**
	 * The configuration for the instance of the DAO.
	 */
	private final CQServiceConfig config;

	/**
	 * The logger
	 */
	private final Log log;

	/**
	 * Construct a new PackageManagerAPIDAO with the specified configuration.
	 * 
	 * @param config
	 *            the configuration to use
	 */
	public HTTPServiceDAO(final CQServiceConfig config) {
		this.config = config;
		this.log = config.getLog();
	}

	/**
	 * Performs a HTTP Get to the specified url with the specified parameters.
	 * Uses the username and password set in the config.
	 * 
	 * @param url
	 *            the url to get from
	 * @return the response from the server
	 * @throws ParseException
	 * @throws IOException
	 * @throws AuthenticationException
	 */
	public byte[] doGet(final String url) throws ParseException, IOException,
			AuthenticationException {
		this.log.debug("doGet");
		final DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			final HttpGet httpget = new HttpGet(url);

			httpget.addHeader(new BasicScheme().authenticate(
					new UsernamePasswordCredentials(this.config.getUser(),
							this.config.getPassword()), httpget));

			this.log.debug("executing request " + httpget.getRequestLine());
			final HttpResponse response = httpclient.execute(httpget);

			this.log.debug("Recieving response");
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				final HttpEntity resEntity = response.getEntity();

				return EntityUtils.toByteArray(resEntity);
			} else {
				throw new IOException("Invalid response: "
						+ response.getStatusLine().getStatusCode() + " "
						+ response.getStatusLine().getReasonPhrase());
			}
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (final Exception ignore) {
			}
		}
	}

	/**
	 * Performs a HTTP Post to the specified url with the specified parameters.
	 * Uses the username and password set in the config.
	 * 
	 * @param url
	 *            the url to post to
	 * @return the response from the server
	 * @throws ParseException
	 * @throws IOException
	 * @throws AuthenticationException
	 */
	public byte[] doPost(final String url) throws ParseException, IOException,
			AuthenticationException {
		this.log.debug("doPost");
		final DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			final HttpPost httppost = new HttpPost(url);

			httppost.addHeader(new BasicScheme().authenticate(
					new UsernamePasswordCredentials(this.config.getUser(),
							this.config.getPassword()), httppost));

			this.log.debug("executing request " + httppost.getRequestLine());
			final HttpResponse response = httpclient.execute(httppost);

			this.log.debug("Recieving response");
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				final HttpEntity resEntity = response.getEntity();
				return EntityUtils.toByteArray(resEntity);
			} else {
				final HttpEntity resEntity = response.getEntity();
				this.log.debug(EntityUtils.toString(resEntity));
				throw new IOException("Invalid response: "
						+ response.getStatusLine().getStatusCode() + " "
						+ response.getStatusLine().getReasonPhrase());
			}
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (final Exception ignore) {
			}
		}
	}

	/**
	 * Posts the supplied file to the specified url. Uses the username and
	 * password set in the config.
	 * 
	 * @param url
	 *            the url to post to
	 * @param file
	 *            the file to post
	 * @return the response from the server
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws AuthenticationException
	 */
	public byte[] postFile(final String url, final String fileAttr,
			final File file) throws ClientProtocolException, IOException,
			AuthenticationException {
		this.log.debug("postFile");
		final DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			final HttpPost httppost = new HttpPost(url);

			httppost.addHeader(new BasicScheme().authenticate(
					new UsernamePasswordCredentials(this.config.getUser(),
							this.config.getPassword()), httppost));

			final FileBody fileBody = new FileBody(file);
			this.log.debug("Posting file: " + file.getAbsolutePath());
			this.log.debug("Post URL: " + url);

			final MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart(fileAttr, fileBody);

			httppost.setEntity(reqEntity);

			this.log.debug("executing request " + httppost.getRequestLine());
			final HttpResponse response = httpclient.execute(httppost);
			final HttpEntity resEntity = response.getEntity();

			this.log.debug("Recieving response");
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				return EntityUtils.toByteArray(resEntity);
			} else {
				this.log.debug(EntityUtils.toString(resEntity));
				throw new IOException("Invalid response: "
						+ response.getStatusLine().getStatusCode() + " "
						+ response.getStatusLine().getReasonPhrase());
			}
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (final Exception ignore) {
			}
		}
	}
}
