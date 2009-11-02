// $Id:$

package de.partmaster.obfuscate4e.updater;

import de.partmaster.obfuscate4e.ProjectInfo;
import de.partmaster.obfuscate4e.generator.IBuildGenerator;

public class CustomBuildScriptUpdater extends AbstractFileUpdater {

	public CustomBuildScriptUpdater(final IBuildGenerator generator,
			final ProjectInfo projectInfo) {
		super(generator, projectInfo, FILE_CUSTOM_BUILD_CALLBACKS);
	}
}
