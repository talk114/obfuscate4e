package org.obfuscate4e.generator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;

/**
 * This class defines the data type to give the exclusions to the configuration
 * generators.<br>
 * 
 * @see IBuildGenerator
 * 
 * @author fwo
 * 
 */
public class ProjectData {

	public class HeaderEntry {
		public String itsKey;
		public String itsValue;

		public HeaderEntry(String key, String value) {
			itsKey = key;
			itsValue = value;
		}
	}

	private final List itsExcludedPackages;
	private final List itsExcludedClasses;
	private String itsStaticPart;

	public static final String HEADER_BEGIN = "begin o4e header";
	public static final String HEADER_END = "end o4e header";

	private final List itsHeaderEntries;

	/**
	 * the ProjectData constructor
	 * 
	 * @param excludedPackages
	 * @param excludeClasses
	 * @param staticPart
	 */
	public ProjectData(final List excludedPackages, final List excludeClasses,
			final String staticPart) {
		Assert.isNotNull(excludedPackages);
		Assert.isNotNull(excludeClasses);
		itsExcludedPackages = excludedPackages;
		itsExcludedClasses = excludeClasses;
		itsStaticPart = staticPart;
		itsHeaderEntries = new ArrayList();
		itsHeaderEntries.add(new HeaderEntry("ConfigFileVersion", "1.0"));
	}

	/**
	 * returns a list of classes that have to get excluded from the obfuscation.<br>
	 * usually this list contains the set of classes which extend other PDE
	 * plugin's extension points and is generated from the plugin.xml
	 * 
	 * @return the list of classes
	 */
	public List getExcludedClasses() {
		return itsExcludedClasses;
	}

	/**
	 * returns a list of packages that have to get excluded from the
	 * obfuscation.<br>
	 * usually this list contains the set of packages exported to other PDE
	 * plugins and is generated from the plugin.xml
	 * 
	 * 
	 * @return the list of packages
	 */
	public List getExcludedPackages() {
		return itsExcludedPackages;
	}

	/**
	 * in case the config file has already existed before, this method delivers
	 * the old file's static content
	 * 
	 * @return the static statements
	 */
	public String getStaticPart() {
		return itsStaticPart;
	}

	/**
	 * returns a list of key-value pairs for the o4e header
	 * 
	 * @return the list of header entries
	 */
	public List getHeaderEntries() {
		return itsHeaderEntries;
	}
}
