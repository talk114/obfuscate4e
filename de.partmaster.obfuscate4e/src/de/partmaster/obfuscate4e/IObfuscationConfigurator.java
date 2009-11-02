/**
 * 
 */
package de.partmaster.obfuscate4e;

import java.util.List;

import de.partmaster.obfuscate4e.updater.IUpdater;

/**
 * This interface defines the API for the obfuscation configurator extension
 * point. It serves as the general gateway for Obfuscate4e to delegate
 * configurations to the specific obfuscators. Furthermore it provides the
 * necessary custom updater and build generator for a specific configuration
 * generation process.
 * 
 * @author fwo
 * 
 */
public interface IObfuscationConfigurator {

	/**
	 * returns the custom obfuscator's name
	 * 
	 * @return the custom obfuscator's name
	 */
	public String getName();

	/**
	 * returns the custom obfuscator's configuration file updater
	 * 
	 * @return the custom obfuscator's configuration file updater
	 */
	public IUpdater getConfigFileUpdater();

	/**
	 * returns the custom obfuscator's build script updater
	 * 
	 * @return the custom obfuscator's build script updater
	 */
	public IUpdater getBuildScriptUpdater();

	/**
	 * adds an exclusion statement to to configuration
	 * 
	 * @param exclusion
	 *            the exclusion
	 */
	public void exclude(ObfuscationExclusion exclusion);

	/**
	 * adds a set of exclusions to the configuration
	 * 
	 * @param exclusions
	 *            the exclusions
	 */
	public void exclude(List exclusions);

	/**
	 * returns the plugin's identifier
	 * 
	 * @return the obfuscator plugin's identifier
	 */
	public String getID();

}
