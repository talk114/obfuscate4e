// $Id:$

package de.partmaster.obfuscate4e.updater;

import de.partmaster.obfuscate4e.generator.IBuildGenerator;

public class CustomBuildScriptUpdater extends AbstractFileUpdater {

	public static final String DESCRIPTION = "Create or update '"
			+ FILE_CUSTOM_BUILD_CALLBACKS + "'";

	public CustomBuildScriptUpdater(final IBuildGenerator generator) {
		super(generator, FILE_CUSTOM_BUILD_CALLBACKS, DESCRIPTION);
	}
}
