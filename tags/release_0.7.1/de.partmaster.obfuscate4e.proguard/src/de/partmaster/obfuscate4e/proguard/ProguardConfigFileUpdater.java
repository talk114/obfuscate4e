package de.partmaster.obfuscate4e.proguard;

import de.partmaster.obfuscate4e.proguard.generator.ProguardConfigGenerator;
import de.partmaster.obfuscate4e.updater.AbstractFileUpdater;

public class ProguardConfigFileUpdater extends AbstractFileUpdater {

	public static final String PROGUARD_CONFIG = "proguard.cfg";

	public static final String DESCRIPTION = "Create or update '"
			+ PROGUARD_CONFIG + "'";

	public ProguardConfigFileUpdater() {
		super(new ProguardConfigGenerator(), PROGUARD_CONFIG,
				DESCRIPTION);
	}
}
