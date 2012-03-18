package com.sixdimensions.wcm.cq.pack;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Before;
import org.junit.Test;

import com.sixdimensions.wcm.cq.pack.service.PackageManagerConfig;
import com.sixdimensions.wcm.cq.pack.service.PackageManagerService;

public class PackageManagerServiceImplTest {

	
	private PackageManagerService packageManagerSvc;
	private Log log = new SystemStreamLog();
	
	@Before
	public void init(){
		log.info("Init");

		PackageManagerConfig config = new PackageManagerConfig();
		config.setHost("http://poc.crownpartners.com");
		config.setPort("4502");
		config.setUseLegacy(true);
		config.setUser("admin");
		config.setPassword("bob");
		config.setErrorOnFailure(false);
		config.setLog(log);
		
		packageManagerSvc = 
				PackageManagerService.Factory.getPackageMgr(config);
		log.info("Init Complete");
	}
	
	@Test
	public void testPreview() throws Exception{

		log.info("Testing Preview");
		packageManagerSvc.preview("/etc/packages/day/cq540/product/cq-content-5.4.0.20110218.zip");
	}
	
	@Test
	public void testDelete() throws Exception{
		log.info("Testing Delete");
		packageManagerSvc.delete("my_packages/export-app-1.0.zip");
	}
}
