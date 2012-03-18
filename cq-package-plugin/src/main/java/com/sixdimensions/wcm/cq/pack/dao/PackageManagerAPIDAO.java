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
package com.sixdimensions.wcm.cq.pack.dao;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.maven.plugin.logging.Log;

import com.sixdimensions.wcm.cq.pack.service.PackageManagerConfig;

/**
 * Performs interactions with the CQ Package API including handling connections
 * and parsing the response.
 * 
 * @author dklco
 */
public class PackageManagerAPIDAO {
	private PackageManagerConfig config;
	private Log log;

	/**
	 * Construct a new PackageManagerAPIDAO with the specified configuration.
	 * 
	 * @param config
	 *            the configuration to use
	 */
	public PackageManagerAPIDAO(PackageManagerConfig config) {
		this.config = config;
		this.log = config.getLog();
	}

	/**
	 * Performs a HTTP Get to the specified url with the specified parameters.
	 * Uses the username and password set in the config.
	 * 
	 * @param url
	 *            the url to get from
	 * @param param
	 *            the parameters to pass to the request
	 * @return the response from the server
	 * @throws ParseException
	 * @throws IOException
	 */
	public byte[] doGet(String url)
			throws ParseException, IOException {
		log.debug("doGet");
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpGet httpget = new HttpGet(url);

			httpclient.getCredentialsProvider().setCredentials(
					AuthScope.ANY,
					new UsernamePasswordCredentials(config.getUser(), config
							.getPassword()));

			log.debug("executing request " + httpget.getRequestLine());
			HttpResponse response = httpclient.execute(httpget);

			log.debug("Recieving response");
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity resEntity = response.getEntity();

				return EntityUtils.toByteArray(resEntity);
			} else {
				throw new IOException("Invalid response: "
						+ response.getStatusLine().getStatusCode() + " "
						+ response.getStatusLine().getReasonPhrase());
			}
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception ignore) {
			}
		}
	}

	/**
	 * Performs a HTTP Post to the specified url with the specified parameters.
	 * Uses the username and password set in the config.
	 * 
	 * @param url
	 *            the url to post to
	 * @param param
	 *            the parameters to pass to the request
	 * @return the response from the server
	 * @throws ParseException
	 * @throws IOException
	 */
	public byte[] doPost(String url, Map<String, String> params)
			throws ParseException, IOException {
		log.debug("doPost");
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(url);

			httpclient.getCredentialsProvider().setCredentials(
					AuthScope.ANY,
					new UsernamePasswordCredentials(config.getUser(), config
							.getPassword()));

			if (params != null) {
				for (String key : params.keySet()) {
					httppost.getParams().setParameter(key, params.get(key));
				}
			}

			log.debug("executing request " + httppost.getRequestLine());
			HttpResponse response = httpclient.execute(httppost);

			log.debug("Recieving response");
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity resEntity = response.getEntity();
				return EntityUtils.toByteArray(resEntity);
			} else {
				throw new IOException("Invalid response: "
						+ response.getStatusLine().getStatusCode() + " "
						+ response.getStatusLine().getReasonPhrase());
			}
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception ignore) {
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
	 */
	public byte[] postFile(String url, File file)
			throws ClientProtocolException, IOException {
		log.debug("postFile");
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(url);

			httpclient.getCredentialsProvider().setCredentials(
					AuthScope.ANY,
					new UsernamePasswordCredentials(config.getUser(), config
							.getPassword()));

			FileBody bin = new FileBody(file);
			log.debug("Posting file: " + file);
			log.debug("Post URL: " + url);

			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("bin", bin);

			httppost.setEntity(reqEntity);

			log.debug("executing request " + httppost.getRequestLine());
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();

			log.debug("Recieving response");
			return EntityUtils.toByteArray(resEntity);
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception ignore) {
			}
		}
	}
}
