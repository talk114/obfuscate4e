package org.obfuscate4e.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.obfuscate4e.GeneralObfuscate4eException;
import org.obfuscate4e.ObfuscationExclusion;
import org.obfuscate4e.ProjectHelper;

/**
 * This Action is used for the context menu entry<br>
 * "Exclude class from obfuscation" for *.java files
 * 
 * @author fwo
 * 
 */
public class ExcludeClassAction extends Obfuscate4eResourceActionAdapter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.obfuscate4e.actions.Obfuscate4eResourceActionAdapter#initializedRun
	 * (org.eclipse.jface.action.IAction)
	 */
	protected void initializedRun(IAction action) {

		ISelection classSelection = getSelection();

		if (!(classSelection instanceof IStructuredSelection)) {
			return;
		}

		Object obj = ((IStructuredSelection) classSelection).getFirstElement();

		if (!(obj instanceof ICompilationUnit)) {
			return;
		}
		ICompilationUnit selectedClass = (ICompilationUnit) obj;
		IProject project = ProjectHelper.findProject(selectedClass);

		String qualifier = "";
		boolean isInterface = false;

		try {
			IType[] types = selectedClass.getTypes();
			if (types != null) {
				if (types[0] != null) {
					isInterface = types[0].isInterface();
					qualifier = types[0].getFullyQualifiedName();
				}
			}
		} catch (JavaModelException e) {
		}

		if (qualifier.equals("")) {
			return;
		}

		ObfuscationExclusion exclusion;
		try {
			if (isInterface) {
				exclusion = ObfuscationExclusion
						.createInterfaceImplementationExclusion(qualifier,
								"inserted by 'Exclude class' action");
			} else {
				exclusion = ObfuscationExclusion.createSimpleClassExclusion(
						qualifier, "inserted by 'Exclude class' action");
			}
		} catch (GeneralObfuscate4eException e) {
			e.printStackTrace();
			return;
		}

		getConfigurator().exclude(exclusion);

		getUpdateHandler().runUpdaters(project);
	}

}
