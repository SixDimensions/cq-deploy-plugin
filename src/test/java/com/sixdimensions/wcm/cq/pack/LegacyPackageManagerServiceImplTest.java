package com.sixdimensions.wcm.cq.pack;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URLDecoder;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Before;
import org.junit.Test;

import com.sixdimensions.wcm.cq.pack.service.PackageManagerConfig;
import com.sixdimensions.wcm.cq.pack.service.PackageManagerService;

public class LegacyPackageManagerServiceImplTest {

	private PackageManagerService packageManagerSvc;
	private Log log = new SystemStreamLog();

	@Before
	public void init() {
		log.info("Init");

		PackageManagerConfig config = new PackageManagerConfig();
		config.setHost("http://poc.crownpartners.com");
		config.setPort("4502");
		config.setUseLegacy(true);
		config.setUser("admin");
		config.setPassword("admin");
		config.setErrorOnFailure(true);
		config.setLog(log);

		packageManagerSvc = PackageManagerService.Factory.getPackageMgr(config);
		log.info("Init Complete");
	}

	@Test
	public void testUpload() throws Exception {
		log.info("Testing Upload");
		File f = new File(URLDecoder.decode(getClass().getClassLoader()
				.getResource("test-1.0.0.zip").getPath(), "UTF-8"));
		packageManagerSvc.upload("test/test", f);
	}
	

	@Test
	public void testInstall() throws Exception {
		log.info("Testing Install");
		try {
			packageManagerSvc.install("my_packages/does-not-exist.zip");
			fail("Exception expected");
		} catch (Exception re) {
			log.info("Exception caught as expected");
		}
		packageManagerSvc.install("test/test");
	}
	
	
	@Test
	public void testDelete() throws Exception {
		log.info("Testing Delete");
		try {
			packageManagerSvc.delete("my_packages/does-not-exist.zip");
			fail("Exception expected");
		} catch (Exception re) {
			log.info("Exception caught as expected");
		}

	}

}
