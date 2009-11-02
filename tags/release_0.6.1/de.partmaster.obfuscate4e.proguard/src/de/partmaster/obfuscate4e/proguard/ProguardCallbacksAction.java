// $Id:$

package de.partmaster.obfuscate4e.proguard;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;

import de.partmaster.obfuscate4e.BaseBuildCallbacksAction;
import de.partmaster.obfuscate4e.proguard.generator.ProguardBuildTemplate;
import de.partmaster.obfuscate4e.proguard.generator.ProguardConfigTemplate;

public class ProguardCallbacksAction extends BaseBuildCallbacksAction {

    public ProguardCallbacksAction() {
        super(new ProguardBuildTemplate());
    }

    protected void internalRun(IProgressMonitor monitor) throws CoreException {
    	super.internalRun(monitor);
		TemplateFileUpdater updater = new TemplateFileUpdater(new ProguardConfigTemplate(),
				fManifestFile);
		if (updater.exists()) {
			IFile file = updater.getFile();
			if (MessageDialog.openConfirm(getTargetPart().getSite().getShell(),
					"Confirm", file.getFullPath() + " exists, overwrite?")) {
				updater.remove(monitor);
				updater.write(monitor);
			}
		} else {
			updater.write(monitor);
		}

	}
}
