// $Id:$

package de.partmaster.obfuscate4e;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.Assert;
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

	private final List itsUpdaters = new ArrayList();
	private ISelection itsSelection;

	public BaseBuildCallbacksAction(final IBuildGenerator buildGenerator) {
		Assert.isNotNull(buildGenerator);
		addUpdater(new CustomBuildScriptUpdater(buildGenerator));
		addUpdater(new BuildPropertiesUpdater());
	}

	public void setActivePart(final IAction action,
			final IWorkbenchPart targetPart) {
	}

	protected void addUpdater(final IUpdater updater) {
		itsUpdaters.add(updater);
	}

	public void run(final IAction action) {
		if (!(itsSelection instanceof IStructuredSelection)) {
			return;
		}
		Object obj = ((IStructuredSelection) itsSelection).getFirstElement();
		if (!(obj instanceof IFile)) {
			return;
		}
		ProjectInfo projectInfo = new ProjectInfo((IFile) obj);
		setupProject(projectInfo);
		if (!projectInfo.getManifest().exists()) {
			return;
		}

		IRunnableWithProgress op = new IRunnableWithProgress() {

			public void run(final IProgressMonitor monitor) {
				IWorkspaceRunnable wop = new IWorkspaceRunnable() {

					public void run(final IProgressMonitor monitor)
							throws CoreException {
						for (Iterator iterator = itsUpdaters.iterator(); iterator
								.hasNext();) {
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

	public void selectionChanged(final IAction action,
			final ISelection selection) {
		itsSelection = selection;
	}

	private void setupProject(final ProjectInfo projectInfo) {
		for (Iterator iterator = itsUpdaters.iterator(); iterator.hasNext();) {
			IUpdater updater = (IUpdater) iterator.next();
			updater.setProjectInfo(projectInfo);
		}
	}
}
