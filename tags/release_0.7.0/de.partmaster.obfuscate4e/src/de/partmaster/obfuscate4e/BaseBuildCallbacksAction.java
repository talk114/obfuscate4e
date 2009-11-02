// $Id:$

package de.partmaster.obfuscate4e;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import de.partmaster.obfuscate4e.generator.IBuildGenerator;
import de.partmaster.obfuscate4e.updater.BuildPropertiesUpdater;
import de.partmaster.obfuscate4e.updater.CustomBuildScriptUpdater;
import de.partmaster.obfuscate4e.updater.IUpdater;

/**
 * code based on org.eclipse.pde.internal.ui.build.BaseBuildAction
 */
public class BaseBuildCallbacksAction implements IObjectActionDelegate {

	private IBuildGenerator itsBuildGenerator;
	private IWorkbenchPart itsTargetPart;
	protected ProjectInfo itsProjectInfo;
	private List itsUpdaters = new ArrayList();
	private BuildPropertiesUpdater itsPropertyUpdater;
	
	public BaseBuildCallbacksAction(IBuildGenerator buildGenerator) {
		itsBuildGenerator = buildGenerator;
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		itsTargetPart = targetPart;
	}

	protected IWorkbenchPart getTargetPart() {
		return itsTargetPart;
	}

	protected void addUpdater(IUpdater updater) {
		itsUpdaters.add(updater);
	}
	
	public void run(IAction action) {
		if (!itsProjectInfo.getManifest().exists())
			return;

		IRunnableWithProgress op = new IRunnableWithProgress() {

			public void run(IProgressMonitor monitor) {
				IWorkspaceRunnable wop = new IWorkspaceRunnable() {

					public void run(IProgressMonitor monitor)
							throws CoreException {
						for (Iterator iterator = itsUpdaters.iterator(); iterator.hasNext();) {
							IUpdater updater = (IUpdater) iterator.next();
							updater.run(monitor);
						}
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

	protected void runUpdaters(IProgressMonitor progressMonitor)
			throws CoreException {
	}

	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			if (obj != null && obj instanceof IFile) {
				try {
					setupProject(new ProjectInfo((IFile) obj));
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}

	}

	protected void setupProject(ProjectInfo projectInfo) throws CoreException {
		itsProjectInfo =  projectInfo;
		itsUpdaters = new ArrayList();
		itsUpdaters.add(new CustomBuildScriptUpdater(
				itsBuildGenerator, itsProjectInfo));

        setPropertyUpdater(new BuildPropertiesUpdater(
				itsProjectInfo));
	}

	protected void setPropertyUpdater(final BuildPropertiesUpdater updater) {
		if (itsUpdaters.contains(getPropertyUpdater())) {
			itsUpdaters.remove(getPropertyUpdater());
		}
		itsPropertyUpdater = updater;
		addUpdater(updater);
	}

	protected BuildPropertiesUpdater getPropertyUpdater() {
		return itsPropertyUpdater;
	}
}
