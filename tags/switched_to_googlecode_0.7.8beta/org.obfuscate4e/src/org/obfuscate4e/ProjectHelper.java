package org.obfuscate4e;

import java.io.FileNotFoundException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;

public final class ProjectHelper {

	private ProjectHelper() {
	}

	/**
	 * returns the PDE project's manifest file
	 * 
	 * @param project
	 *            the PDE project
	 * @return the manifest file
	 * @throws FileNotFoundException
	 */
	public static IFile findProjectManifest(IProject project)
			throws FileNotFoundException {
		Assert.isNotNull(project);
		IFolder meta_inf_folder = project.getFolder("META-INF");
		if (!meta_inf_folder.exists()) {
			throw new FileNotFoundException("Could not find project manifest");
		}
		IFile manifest = meta_inf_folder.getFile("MANIFEST.MF");
		if (!manifest.exists()) {
			throw new FileNotFoundException("Could not find project manifest");
		}
		return manifest;
	}

	/**
	 * returns the manifest file for the belonging PDE project of the given
	 * compilation unit
	 * 
	 * @param unit
	 *            the .java file
	 * @return the manifest file
	 * @throws FileNotFoundException
	 */
	public static IFile findProjectManifest(ICompilationUnit unit)
			throws FileNotFoundException {
		Assert.isNotNull(unit);
		return findProjectManifest(findProject(unit));
	}

	/**
	 * returns the manifest file for the belonging project of the given file
	 * 
	 * @param file
	 *            the project's file
	 * @return the manifest file
	 * @throws FileNotFoundException
	 */
	public static IFile findProjectManifest(IFile file)
			throws FileNotFoundException {
		Assert.isNotNull(file);
		return findProjectManifest(file.getProject());
	}

	/**
	 * returns the project for the given compilation unit
	 * 
	 * @param unit
	 *            the .java file
	 * @return the eclipse project representation
	 */
	public static IProject findProject(ICompilationUnit unit) {
		return unit.getJavaProject().getProject();
	}

	/**
	 * returns the project for the given file
	 * 
	 * @param unit
	 *            the file
	 * @return the eclipse project representation
	 */
	public static IProject findProject(IFile file) {
		return file.getProject();
	}


}
