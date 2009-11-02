package de.partmaster.obfuscate4e.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import de.partmaster.obfuscate4e.BaseBuildCallbackHandler;
import de.partmaster.obfuscate4e.IObfuscationConfigurator;
import de.partmaster.obfuscate4e.ObfuscationConfiguratorManager;

/**
 * This adapter class provides some general initialization code for Obfuscate4e
 * Resource actions. Therefore inheriting classes should NOT overload the
 * run(IAction) method but instead implement the initializedRun(IAction) method
 * 
 * @see Obfuscate4eResourceActionAdapter#run(IAction)
 * @see Obfuscate4eResourceActionAdapter#initializedRun(IAction)
 * 
 * @author fwo
 * 
 */
public abstract class Obfuscate4eResourceActionAdapter implements
		IObjectActionDelegate {
	private IObfuscationConfigurator itsConfigurator;
	private BaseBuildCallbackHandler itsUpdateHandler;
	private ISelection itsSelection;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(final IAction action) {

		ISelection classSelection = getSelection();

		if (!(classSelection instanceof IStructuredSelection)) {
			return;
		}
		Object obj = ((IStructuredSelection) classSelection).getFirstElement();

		IProject project = findProject(obj);
		if (project == null) {
			return;
		}
		initProject(project);
		initializedRun(action);
	}

	private IProject findProject(Object obj) {
		IProject project = null;
		if (obj instanceof ICompilationUnit) {
			project = ((ICompilationUnit) obj).getJavaProject().getProject();
		}
		if (obj instanceof IResource) {
			project = ((IResource) obj).getProject();
		}
		return project;
	}

	private void initProject(IProject project) {
		itsConfigurator = ObfuscationConfiguratorManager
				.getActiveObfuscationConfigurator(project);
		itsUpdateHandler = new BaseBuildCallbackHandler();
		itsUpdateHandler.addUpdater(itsConfigurator.getConfigFileUpdater());
		itsUpdateHandler.addUpdater(itsConfigurator.getBuildScriptUpdater());
	}

	/**
	 * to use instead of run(IAction)<br>
	 * an obfuscation configuration handler already has been configured when
	 * this method is called
	 * 
	 * @see Obfuscate4eResourceActionAdapter#run(IAction)
	 * @param action
	 */
	protected abstract void initializedRun(final IAction action);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.
	 * action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 * .IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(final IAction action,
			final ISelection selection) {
		itsSelection = selection;
	}

	/**
	 * returns the selection
	 * 
	 * @return the selection
	 */
	protected ISelection getSelection() {
		return itsSelection;
	}

	/**
	 * returns the active configurator
	 * 
	 * @return the active configurator
	 */
	protected IObfuscationConfigurator getConfigurator() {
		return itsConfigurator;
	}

	/**
	 * returns the update handler
	 * 
	 * @return the update handler
	 */
	protected BaseBuildCallbackHandler getUpdateHandler() {
		return itsUpdateHandler;
	}

}
