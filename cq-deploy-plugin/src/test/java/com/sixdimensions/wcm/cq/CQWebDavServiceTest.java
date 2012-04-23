package com.sixdimensions.wcm.cq;

import java.io.File;
import java.net.URLDecoder;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Before;
import org.junit.Test;

import com.sixdimensions.wcm.cq.service.CQService;
import com.sixdimensions.wcm.cq.service.CQServiceConfig;

public class CQWebDavServiceTest {

	private CQService cqSvc;
	private Log log = new SystemStreamLog();

	@Before
	public void init() {
		log.info("Init");

		CQServiceConfig config = new CQServiceConfig();
		config.setHost("http://localhost");
		config.setPort("4502");
		config.setUser("admin");
		config.setPassword("admin");
		config.setLog(log);

		cqSvc = CQService.Factory.getService(config);
		log.info("Init Complete");
	}

	@Test
	public void testCreateFolder() throws Exception {
		log.info("testCreateFolder");

		cqSvc.createFolder("/apps/bundles/install");
		log.info("Create Folder Complete");
	}

	@Test
	public void testUploadFile() throws Exception {
		log.info("testUploadFile");

		File f = new File(URLDecoder.decode(getClass().getClassLoader()
				.getResource("org.apache.servicemix.bundles.commons-csv-1.0-r706900_3.jar").getPath(), "UTF-8"));
		cqSvc.uploadFile(f, "/apps/bundles/install");
		log.info("Upload File Complete");
	}

	@Test
	public void testDeleteFile() throws Exception {
		log.info("testDeleteFile");

		cqSvc.delete("/apps/bundles/install/test-bundle-1.0.0.jar");

		log.info("Delete File Complete");
	}
}
