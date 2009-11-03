// $Id:$

package org.obfuscate4e.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.obfuscate4e.Activator;
import org.obfuscate4e.ObfuscationConfiguratorManager;
import org.obfuscate4e.ProjectHelper;
import org.obfuscate4e.preferences.PreferenceConstants;

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
	 * @seeorg.obfuscate4e.actions.Obfuscate4eResourceActionAdapter#
	 * initializedRun(org.eclipse.jface.action.IAction)
	 */
	protected void initializedRun(IAction action) {
		if (!(getSelection() instanceof IStructuredSelection)) {
			return;
		}
		Object obj = ((IStructuredSelection) getSelection()).getFirstElement();
		IProject project = ProjectHelper.findProject((IFile) obj);

		if (Activator.getDefault().getPreferenceStore().getBoolean(
				PreferenceConstants.ENABLE_BUILDER_FOR_NEW_PROJECTS)) {
			ObfuscationConfiguratorManager.addObfuscate4eNature(ProjectHelper
					.findProject((IFile) obj));
		}
		getUpdateHandler().runUpdaters(project);
	}
}
