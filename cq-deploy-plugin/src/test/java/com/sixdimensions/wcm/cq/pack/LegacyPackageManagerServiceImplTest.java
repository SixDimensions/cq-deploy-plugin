package com.sixdimensions.wcm.cq.pack;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Before;
import org.junit.Test;

import com.sixdimensions.wcm.cq.pack.service.AC_HANDLING;
import com.sixdimensions.wcm.cq.pack.service.PackageManagerConfig;
import com.sixdimensions.wcm.cq.pack.service.PackageManagerService;
import com.sixdimensions.wcm.cq.service.CQService;

public class LegacyPackageManagerServiceImplTest {

	private PackageManagerService packageManagerSvc;
	private Log log = new SystemStreamLog();
	private boolean executeTests = true;

	@Before
	public void init() {
		log.info("Init");

		PackageManagerConfig config = new PackageManagerConfig();
		config.setHost("http://localhost");
		config.setPort("4502");
		config.setUseLegacy(true);
		config.setUser("admin");
		config.setPassword("admin");
		config.setErrorOnFailure(true);
		config.setLog(log);
		config.setAcHandling(AC_HANDLING.valueOf("ignore"));

		log.debug("Verifying authentication");
		CQService cqSvc = CQService.Factory.getService(config);
		try {
			cqSvc.createFolder("/apps");
		} catch (Exception e) {
			log.warn("Authentication failed, skipping tests");
			executeTests = false;
		}

		packageManagerSvc = PackageManagerService.Factory.getPackageMgr(config);
		log.info("Init Complete");
	}

	@Test
	public void testUpload() throws Exception {
		log.info("Testing Upload");
		if (executeTests) {
			File f = new File(URLDecoder.decode(getClass().getClassLoader()
					.getResource("test-1.0.0.zip").getPath(), "UTF-8"));
			packageManagerSvc.upload("test/test", f);
		}
	}

	@Test
	public void testInstall() throws Exception {
		log.info("Testing Install");
		if (executeTests) {
			try {
				packageManagerSvc.install("my_packages/does-not-exist.zip");
				fail("Exception expected");
			} catch (Exception re) {
				log.info("Exception caught as expected");
			}
			packageManagerSvc.install("test/test");
		}
	}

	@Test
	public void testDelete() throws Exception {
		log.info("Testing Delete");
		if (executeTests) {
			try {
				packageManagerSvc.delete("my_packages/does-not-exist.zip");
				fail("Exception expected");
			} catch (Exception re) {
				log.info("Exception caught as expected");
			}
		}
	}

	@Test
	public void testFailures() throws Exception {
	
		log.info("Testing failures");
	
		String[] failures = new String[] { "test-bad-zip.zip",
				"test-not-package.zip", "test-bad-file.zip" };
		for (String failure : failures) {
			try {
				log.info("Testing " + failure);
				File f = new File(URLDecoder.decode(getClass().getClassLoader()
						.getResource(failure).getPath(), "UTF-8"));
				packageManagerSvc.upload("test/" + failure, f);
				packageManagerSvc.install(failure.replace(".zip", ""));
			} catch (Exception e) {
				log.error(e);
			}
		}
		
		log.info("Test Successful");
	}

}
