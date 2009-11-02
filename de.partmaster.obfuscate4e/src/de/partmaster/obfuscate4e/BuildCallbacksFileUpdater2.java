// $Id:$

package de.partmaster.obfuscate4e;

import org.eclipse.core.resources.IFile;
 
import de.partmaster.obfuscate4e.generator.IBuildGenerator;

public class BuildCallbacksFileUpdater2 extends ProjectFileUpdater {

    public BuildCallbacksFileUpdater2(IBuildGenerator generator, IFile manifest) {
        super(generator, manifest, FILE_CUSTOM_BUILD_CALLBACKS);
    }
}
