package de.partmaster.obfuscate4e.proguard;

import java.util.List;

import de.partmaster.obfuscate4e.IObfuscationConfigurator;
import de.partmaster.obfuscate4e.ObfuscationExclusion;
import de.partmaster.obfuscate4e.generator.IBuildGenerator;
import de.partmaster.obfuscate4e.proguard.generator.ProguardCustomBuildScriptGenerator;
import de.partmaster.obfuscate4e.updater.AbstractBuildScriptUpdater;
import de.partmaster.obfuscate4e.updater.IUpdater;

/**
 * This class serves as the crosslink to the Obfuscate4e extension point. All
 * interaction with the Proguard plugin should run over this interface.
 * 
 * * @author fwo
 * 
 */
public class ProguardObfuscationConfigurator implements
		IObfuscationConfigurator {

	private ProguardConfigFileUpdater itsConfigFileUpdater;
	private AbstractBuildScriptUpdater itsBuildScriptGenerator;

	public static final String NAME = "Proguard";

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.partmaster.obfuscate4e.IObfuscationConfigurator#getUpdater()
	 */
	public IUpdater getConfigFileUpdater() {
		if (itsConfigFileUpdater == null) {
			return itsConfigFileUpdater = new ProguardConfigFileUpdater();
		} else {
			return itsConfigFileUpdater;
		}
	}

	/**
	 * adds an exclusion statement to the Proguard configuration file.
	 */
	public void exclude(ObfuscationExclusion exclusion) {
		itsConfigFileUpdater.addStaticClassExclusion(exclusion);
	}

	/**
	 * adds a set of exclusion statements to the ZKM configuration file.
	 */
	public void exclude(List exclusions) {
		// TODO Auto-generated method stub

	}

	/**
	 * returns the Proguard plugin identifier
	 * 
	 * @return the Proguard plugin identifier
	 */
	public String getID() {
		return Activator.PLUGIN_ID;
	}

	/**
	 * returns "Proguard" the obfuscator's name
	 * 
	 * @return "Proguard" the obfuscator's name
	 */
	public String getName() {
		return NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.partmaster.obfuscate4e.IObfuscationConfigurator#getBuildScriptUpdater
	 * ()
	 */
	public IUpdater getBuildScriptUpdater() {
		if (itsBuildScriptGenerator == null) {
			return itsBuildScriptGenerator = new AbstractBuildScriptUpdater() {
				public void setupGenerator() {
					setGenerator(new ProguardCustomBuildScriptGenerator());
				}
			};
		}
		return itsBuildScriptGenerator;
	}

}
