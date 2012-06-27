package com.sixdimensions.wcm.cq.pack;

import java.io.File;
import java.net.URLDecoder;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Before;
import org.junit.Test;

import com.sixdimensions.wcm.cq.pack.service.AC_HANDLING;
import com.sixdimensions.wcm.cq.pack.service.PackageManagerConfig;
import com.sixdimensions.wcm.cq.pack.service.PackageManagerService;
import com.sixdimensions.wcm.cq.service.CQService;

public class PackageManagerServiceImplTest {

	private boolean executeTests = true;
	private Log log = new SystemStreamLog();
	private PackageManagerService packageManagerSvc;

	@Before
	public void init() {
		log.info("Init");

		PackageManagerConfig config = new PackageManagerConfig();
		config.setHost("http://localhost");
		config.setPort("4502");
		config.setUseLegacy(false);
		config.setUser("admin");
		config.setPassword("admin");
		config.setErrorOnFailure(true);
		config.setLog(log);
		config.setAutosave(1024);
		config.setAcHandling(AC_HANDLING.valueOf("ignore"));

		packageManagerSvc = PackageManagerService.Factory.getPackageMgr(config);

		log.debug("Verifying authentication");
		CQService cqSvc = CQService.Factory.getService(config);
		try {
			cqSvc.createFolder("/apps");
		} catch (Exception e) {
			log.warn("Authentication failed, skipping tests");
			executeTests = false;
		}

		log.info("Init Complete");
	}


	@Test
	public void testFailures() throws Exception {
		log.info("Testing failures");
		if (executeTests) {
			String[] failures = new String[] { "test-bad-zip.zip",
					"test-not-package.zip", "test-bad-file.zip" };
			for (String failure : failures) {
				try {
					log.info("Testing " + failure);
					File f = new File(URLDecoder.decode(getClass()
							.getClassLoader().getResource(failure).getPath(),
							"UTF-8"));
					packageManagerSvc.upload("test/" + failure, f);
					packageManagerSvc.install(failure);
				} catch (Exception e) {
					try{
						packageManagerSvc.delete(failure);
					}catch(Exception ee){
						log.debug("Caught exception deleting test failure package: "+ee);
					}
					log.error(e);
				}
			}
		}
		log.info("Test Successful");
	}

	@Test
	public void testUpload() throws Exception {
		log.info("Testing Upload");
		if (executeTests) {
			File f = new File(URLDecoder.decode(getClass().getClassLoader()
					.getResource("test-1.0.0.zip").getPath(), "UTF-8"));
			packageManagerSvc.upload("test/test-1.0.0.zip", f);
		}
		log.info("Test Successful");
	}
	
	@Test
	public void testDryRun() throws Exception {
		log.info("Testing Dry Run");
		if (executeTests) {
			packageManagerSvc.dryRun("test/test-1.0.0.zip");
		}
		log.info("Test Successful");
	}

	@Test
	public void testInstall() throws Exception {
		log.info("Testing Install");
		if (executeTests) {
			packageManagerSvc.install("test/test-1.0.0.zip");
		}

		log.info("Test Successful");
	}
	
	@Test
	public void testDelete() throws Exception {
		log.info("Testing Delete");
		if (executeTests) {
			packageManagerSvc.delete("test/test-1.0.0.zip");
		}
		log.info("Test Successful");
	}

}
