// $Id:$

package de.partmaster.obfuscate4e.proguard;

import de.partmaster.obfuscate4e.BaseBuildCallbacksAction;
import de.partmaster.obfuscate4e.proguard.generator.ProguardCustomBuildScriptGenerator;

/**
 * Callback action that updates the proguard configuration.
 */
public class ProguardCallbacksAction extends BaseBuildCallbacksAction {

	public ProguardCallbacksAction() {
        super(new ProguardCustomBuildScriptGenerator());
        addUpdater(new ProguardConfigFileUpdater());
    }
}
