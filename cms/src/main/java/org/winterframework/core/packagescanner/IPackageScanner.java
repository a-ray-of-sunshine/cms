package org.winterframework.core.packagescanner;

import java.util.List;
import java.util.Map;


public interface IPackageScanner {

	void init();
	List<Map<String, Map<String, Object>>> getHandlerMapper();
}
