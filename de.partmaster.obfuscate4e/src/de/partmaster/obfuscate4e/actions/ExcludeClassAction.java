package de.partmaster.obfuscate4e.actions;

import java.io.FileNotFoundException;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import de.partmaster.obfuscate4e.ObfuscationExclusion;
import de.partmaster.obfuscate4e.ProjectInfo;

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
	 * @see de.partmaster.obfuscate4e.actions.Obfuscate4eResourceActionAdapter#initializedRun(org.eclipse.jface.action.IAction)
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

		IFile manifest;
		try {
			manifest = ProjectInfo.findProjectManifest(selectedClass);
		} catch (FileNotFoundException e1) {
			return;
		}

		String qualifier = "";

		try {
			IType[] types = selectedClass.getTypes();
			if (types != null) {
				if (types[0] != null) {
					qualifier = types[0].getFullyQualifiedName();
				}
			}
		} catch (JavaModelException e) {
		}

		if (qualifier.equals("")) {
			return;
		}

		ObfuscationExclusion exclusion = ObfuscationExclusion
				.createSimpleClassExclusion(qualifier,
						"inserted by 'Exclude class' action");
		getConfigurator().exclude(exclusion);

		getUpdateHandler().runUpdaters(manifest);
	}

}
