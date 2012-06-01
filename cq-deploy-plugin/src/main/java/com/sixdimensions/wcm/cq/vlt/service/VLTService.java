package com.sixdimensions.wcm.cq.vlt.service;

import com.sixdimensions.wcm.cq.pack.service.PackageManagerConfig;
import com.sixdimensions.wcm.cq.pack.service.PackageManagerService;
import com.sixdimensions.wcm.cq.pack.service.impl.LegacyPackageManagerServiceImpl;
import com.sixdimensions.wcm.cq.pack.service.impl.PackageManagerServiceImpl;
import com.sixdimensions.wcm.cq.vlt.CQVLTServiceConfig;
import com.sixdimensions.wcm.cq.vlt.service.impl.VLTServiceImpl;

public interface VLTService {

	/**
	 * Factory for creating instances of the PackageManagerService. Using this
	 * you don't need to have any knowledge of the implementing class.
	 */
	static class Factory {
		/**
		 * Get an instance of the Package Manager Service.
		 * 
		 * @param config
		 *            the configuration used it instantiate the Package Manager
		 *            Service.
		 * @return the instance of the Package Manager Service
		 */
		public static VLTService getService(
				CQVLTServiceConfig config) {
			return new VLTServiceImpl(config);
		}
	}
}
