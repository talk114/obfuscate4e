// $Id:$

package de.partmaster.obfuscate4e.generator;

/**
 * The interface for Obfuscate4e configuration generators
 * 
 * @author fwo
 * 
 */
public interface IBuildGenerator {

	/**
	 * generates the configuration file content as a String
	 * 
	 * @see ProjectData
	 * @param exclusionData
	 *            the ProjectData object containing all class and package
	 *            exclusions
	 * @return the configuration file content as a String
	 */
	String generate(ProjectData exclusionData);
}
