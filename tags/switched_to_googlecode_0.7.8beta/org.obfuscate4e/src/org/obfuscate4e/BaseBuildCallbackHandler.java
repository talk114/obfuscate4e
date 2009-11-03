package org.obfuscate4e;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.obfuscate4e.updater.BuildPropertiesUpdater;
import org.obfuscate4e.updater.IUpdater;


/**
 * This class provides the general configuration building logic for Obfuscate4e.
 * 
 * @author fwo
 *
 */
public class BaseBuildCallbackHandler {

	private final List itsUpdaters = new ArrayList();

	/**
	 * the constructor
	 * 
	 * @param buildGenerator
	 */
	public BaseBuildCallbackHandler() {
		addUpdater(new BuildPropertiesUpdater());
	}

	/**
	 * adds an updater to the callback handler's updater List
	 * 
	 * @param updater
	 */
	public void addUpdater(final IUpdater updater) {
		itsUpdaters.add(updater);
	}

	/**
	 * clears the updater List
	 */
	public void removeAllUpdaters() {
		itsUpdaters.clear();
	}

	/**
	 * starts a new thread and calls asynchronously all updaters's run methods
	 * 
	 * @param project
	 */
	public void runUpdaters(IProject project) {
		ProjectInfo projectInfo;
		try {
			projectInfo = new ProjectInfo(project);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return;
		}
		setupProject(projectInfo);

		final IRunnableWithProgress op = new IRunnableWithProgress() {

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
		Display display = Display.getDefault();
		Assert.isNotNull(display);
		display.asyncExec(new Runnable() {
			public void run() {
				startJob(op);
			}
		});
	}

	private void startJob(IRunnableWithProgress op) {
		try {
			PlatformUI.getWorkbench().getProgressService().runInUI(
					Activator.getActiveWorkbenchWindow(), op,
					Activator.getWorkspace().getRoot());
		} catch (InterruptedException e) {
		} catch (InvocationTargetException e) {
			Activator.logException(e);
		}
	}

	/**
	 * sets projectInfo on all registered updaters
	 * 
	 * @param projectInfo
	 */
	public void setupProject(final ProjectInfo projectInfo) {
		for (Iterator iterator = itsUpdaters.iterator(); iterator.hasNext();) {
			IUpdater updater = (IUpdater) iterator.next();
			updater.configureProjectSettings(projectInfo);
		}
	}

}
