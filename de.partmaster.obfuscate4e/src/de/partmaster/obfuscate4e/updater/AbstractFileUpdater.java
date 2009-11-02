// $Id:$

package de.partmaster.obfuscate4e.updater;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import de.partmaster.obfuscate4e.ProjectInfo;
import de.partmaster.obfuscate4e.generator.IBuildGenerator;

public abstract class AbstractFileUpdater extends AbstractUpdater {

    private final IBuildGenerator itsGenerator;
	private final String itsFilename;
	
    public AbstractFileUpdater(IBuildGenerator generator, ProjectInfo projectInfo, String filename) {
        super(projectInfo);
        itsGenerator = generator;
		itsFilename = filename;
    }

    public void run(IProgressMonitor monitor) throws CoreException {
        IFile file = getProjectInfo().getFile(itsFilename);
		if (file.exists()) {
			Shell activeShell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
			if (! MessageDialog.openConfirm(activeShell,
					"Confirm", file.getFullPath() + " exists, overwrite?")) {
				return;
			}
			file.delete(true, monitor);
		}

        List exportEntries = getProjectInfo().getExportEntries();
        String generatedCode = itsGenerator.generate(exportEntries);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(generatedCode.getBytes());
        file.create(inputStream, false, monitor);
    }

}
