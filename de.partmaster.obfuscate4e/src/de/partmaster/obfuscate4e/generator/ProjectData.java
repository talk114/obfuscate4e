package de.partmaster.obfuscate4e.generator;

import java.util.List;

import org.eclipse.core.runtime.Assert;

public class ProjectData {

	private final List excludePackages;
	private final List excludeClasses;

	public ProjectData(List excludePackages, List excludeClasses) {
		Assert.isNotNull(excludePackages);
		Assert.isNotNull(excludeClasses);
		this.excludePackages = excludePackages;
		this.excludeClasses = excludeClasses;
	}

	public List getExcludeClasses() {
		return excludeClasses;
	}

	public List getExcludePackages() {
		return excludePackages;
	}
}
