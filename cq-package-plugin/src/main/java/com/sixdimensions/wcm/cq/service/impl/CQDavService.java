package com.sixdimensions.wcm.cq.service.impl;

import java.io.File;

import org.apache.maven.plugin.logging.Log;

import com.sixdimensions.wcm.cq.service.CQService;
import com.sixdimensions.wcm.cq.service.CQServiceConfig;

public class CQDavService implements CQService {

	private Log log;

	public CQDavService(CQServiceConfig config) {
		log = config.getLog();
	}

	public void createFolder(String path, boolean createParent)
			throws Exception {
		// TODO Auto-generated method stub

	}

	public void uploadFile(File file, String path) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
