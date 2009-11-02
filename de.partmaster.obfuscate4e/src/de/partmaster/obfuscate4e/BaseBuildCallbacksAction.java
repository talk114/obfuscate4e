// $Id:$

package de.partmaster.obfuscate4e;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import de.partmaster.obfuscate4e.generator.IBuildGenerator;

/**
 * code based on org.eclipse.pde.internal.ui.build.BaseBuildAction
 */
public class BaseBuildCallbacksAction implements IObjectActionDelegate {

	protected IFile fManifestFile;
	private IBuildGenerator itsBuildGenerator;
	private IWorkbenchPart itsTargetPart;

	public BaseBuildCallbacksAction(IBuildGenerator buildGenerator) {
		itsBuildGenerator = buildGenerator;
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		itsTargetPart = targetPart;
	}
	
	protected IWorkbenchPart getTargetPart() {
		return itsTargetPart;
	}

	public void run(IAction action) {
		if (!fManifestFile.exists())
			return;

		IRunnableWithProgress op = new IRunnableWithProgress() {

			public void run(IProgressMonitor monitor) {
				IWorkspaceRunnable wop = new IWorkspaceRunnable() {

					public void run(IProgressMonitor progressMonitor)
							throws CoreException {
						internalRun(progressMonitor);
					}
				};
				try {
					Activator.getWorkspace().run(wop, monitor);
				} catch (CoreException e) {
					Activator.logException(e);
				}
			}
		};
		try {
			PlatformUI.getWorkbench().getProgressService().runInUI(
					Activator.getActiveWorkbenchWindow(), op,
					Activator.getWorkspace().getRoot());
		} catch (InterruptedException e) {
		} catch (InvocationTargetException e) {
			Activator.logException(e);
		}

	}

	protected void internalRun(IProgressMonitor progressMonitor) throws CoreException {
		BuildCallbacksFileUpdater2 fileUpdater = new BuildCallbacksFileUpdater2(
				itsBuildGenerator, fManifestFile);
		if (fileUpdater.exists()) {
			IFile file = fileUpdater.getFile();
			if (MessageDialog.openConfirm(itsTargetPart.getSite().getShell(),
					"Confirm", file.getFullPath() + " exists, overwrite?")) {
				fileUpdater.remove(progressMonitor);
				fileUpdater.write(progressMonitor);
			}
		} else {
			fileUpdater.write(progressMonitor);
		}
		BuildCallbacksPropertyUpdater propertyUpdater = new BuildCallbacksPropertyUpdater(
				fManifestFile);
		if (propertyUpdater.existsKey()) {
			if (MessageDialog.openConfirm(itsTargetPart.getSite().getShell(),
					"Confirm", "Build property key exists: "
							+ propertyUpdater.getKey() + "\nOverwrite?")) {
				propertyUpdater.remove();
				propertyUpdater.write();
				propertyUpdater.save();
			}
		} else {
			propertyUpdater.write();
			propertyUpdater.save();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			if (obj != null && obj instanceof IFile) {
				this.fManifestFile = (IFile) obj;
			}
		}

	}

	// protected void refreshLocal(IProgressMonitor monitor) throws
	// CoreException {
	// IProject project = fManifestFile.getProject();
	// project.refreshLocal(IResource.DEPTH_ONE, monitor);
	// }

}
