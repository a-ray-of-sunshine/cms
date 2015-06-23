package org.winterframework.core.packagescanner;

import java.io.File;
import java.util.Map;

@SuppressWarnings(value = {"rawtypes"})
public interface IPackageScanner {

	Map getMappingHandler(File packageFolder);
}
