// $Id:$

package de.partmaster.obfuscate4e.proguard;

import de.partmaster.obfuscate4e.BaseBuildCallbacksAction;
import de.partmaster.obfuscate4e.proguard.generator.ProguardBuildTemplate;

public class ProguardCallbacksAction extends BaseBuildCallbacksAction {

    public ProguardCallbacksAction() {
        super(new ProguardBuildTemplate());
    }

}
