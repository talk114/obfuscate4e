// $Id:$

package de.partmaster.obfuscate4e.proguard;

import org.eclipse.core.runtime.CoreException;

import de.partmaster.obfuscate4e.BaseBuildCallbacksAction;
import de.partmaster.obfuscate4e.ProjectInfo;
import de.partmaster.obfuscate4e.proguard.generator.ProguardCustomBuildScriptGenerator;

public class ProguardCallbacksAction extends BaseBuildCallbacksAction {

	public ProguardCallbacksAction() {
        super(new ProguardCustomBuildScriptGenerator());
    }
	
	protected void setupProject(ProjectInfo projectInfo) throws CoreException {
		super.setupProject(projectInfo);
		addUpdater(new ProguardConfigFileUpdater(itsProjectInfo));
	}
}
