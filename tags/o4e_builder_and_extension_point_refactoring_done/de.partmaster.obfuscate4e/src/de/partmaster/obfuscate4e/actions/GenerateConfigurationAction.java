// $Id:$

package de.partmaster.obfuscate4e.actions;

import java.io.FileNotFoundException;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;

import de.partmaster.obfuscate4e.ProjectInfo;

/**
 * this class is used by the "Generate obfuscation configuration" menu entry in
 * the context menu of the MANIFEST.MF file
 * 
 * code based on org.eclipse.pde.internal.ui.build.BaseBuildAction
 */
public class GenerateConfigurationAction extends
		Obfuscate4eResourceActionAdapter implements IObjectActionDelegate {

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.partmaster.obfuscate4e.actions.Obfuscate4eResourceActionAdapter#
	 * initializedRun(org.eclipse.jface.action.IAction)
	 */
	protected void initializedRun(IAction action) {
		if (!(getSelection() instanceof IStructuredSelection)) {
			return;
		}
		Object obj = ((IStructuredSelection) getSelection()).getFirstElement();
		IFile manifestFile;
		try {
			manifestFile = ProjectInfo.findProjectManifest((IFile) obj);
		} catch (FileNotFoundException e) {
			return;
		}

		getUpdateHandler().runUpdaters(manifestFile);
	}
}
