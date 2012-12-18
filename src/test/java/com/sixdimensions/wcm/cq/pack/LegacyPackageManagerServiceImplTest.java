package com.sixdimensions.wcm.cq.pack;

import static org.junit.Assert.fail;

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

public class LegacyPackageManagerServiceImplTest {

	private PackageManagerService packageManagerSvc;
	private final Log log = new SystemStreamLog();
	private boolean executeTests = true;

	@Before
	public void init() {
		this.log.info("Init");

		final PackageManagerConfig config = new PackageManagerConfig();
		config.setHost("http://localhost");
		config.setPort("4502");
		config.setUseLegacy(true);
		config.setUser("admin");
		config.setPassword("admin");
		config.setErrorOnFailure(true);
		config.setLog(this.log);
		config.setAcHandling(AC_HANDLING.valueOf("ignore"));

		this.log.debug("Verifying authentication");
		final CQService cqSvc = CQService.Factory.getService(config);
		try {
			cqSvc.createFolder("/apps");
		} catch (final Exception e) {
			this.log.warn("Authentication failed, skipping tests");
			this.executeTests = false;
		}

		this.packageManagerSvc = PackageManagerService.Factory
				.getPackageMgr(config);
		this.log.info("Init Complete");
	}

	@Test
	public void testDelete() throws Exception {
		this.log.info("Testing Delete");
		if (this.executeTests) {
			this.packageManagerSvc.delete("test/test");
			try {
				this.packageManagerSvc.delete("my_packages/does-not-exist.zip");
				fail("Exception expected");
			} catch (final Exception re) {
				this.log.info("Exception caught as expected");
			}
		}
	}

	@Test
	public void testFailures() throws Exception {

		this.log.info("Testing failures");

		final String[] failures = new String[] { "test-bad-zip.zip",
				"test-not-package.zip", "test-bad-file.zip" };
		for (final String failure : failures) {
			try {
				this.log.info("Testing " + failure);
				final File f = new File(URLDecoder.decode(this.getClass()
						.getClassLoader().getResource(failure).getPath(),
						"UTF-8"));
				this.packageManagerSvc.upload("test/" + failure, f);
				this.packageManagerSvc.install(failure.replace(".zip", ""));
			} catch (final Exception e) {
				this.log.error(e);
			}
		}

		this.log.info("Test Successful");
	}

	@Test
	public void testInstall() throws Exception {
		this.log.info("Testing Install");
		if (this.executeTests) {
			this.packageManagerSvc.install("test/test");
			try {
				this.packageManagerSvc
						.install("my_packages/does-not-exist.zip");
				fail("Exception expected");
			} catch (final Exception re) {
				this.log.info("Exception caught as expected");
			}
			this.packageManagerSvc.install("test/test");
		}
	}

	@Test
	public void testUpload() throws Exception {
		this.log.info("Testing Upload");
		if (this.executeTests) {
			final File f = new File(URLDecoder.decode(this.getClass()
					.getClassLoader().getResource("test-1.0.0.zip").getPath(),
					"UTF-8"));
			this.packageManagerSvc.upload("test/test", f);
		}
	}

}
