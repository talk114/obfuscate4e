package de.partmaster.obfuscate4e.proguard;

import de.partmaster.obfuscate4e.ProjectInfo;
import de.partmaster.obfuscate4e.proguard.generator.ProguardConfigGenerator;
import de.partmaster.obfuscate4e.updater.AbstractFileUpdater;

public class ProguardConfigFileUpdater extends AbstractFileUpdater {

	public static final String PROGUARD_CONFIG = "proguard.cfg";

	public static final String DESCRIPTION = "Create or update '"
			+ PROGUARD_CONFIG + "'";

	public ProguardConfigFileUpdater(ProjectInfo projectInfo) {
		super(new ProguardConfigGenerator(), projectInfo, PROGUARD_CONFIG,
				DESCRIPTION);
	}
}
