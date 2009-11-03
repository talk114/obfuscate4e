package org.obfuscate4e.tests.testcase;

import java.io.FileNotFoundException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.obfuscate4e.ProjectHelper;
import org.obfuscate4e.tests.ProjectDependingTests;

import junit.framework.TestCase;

public class ProjectHelperTest extends TestCase {

	public ProjectHelperTest(String name) {
		super(name);
	}

	public void testFindProjectManifestIProject() {

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		IProject project = root
				.getProject(ProjectDependingTests.TESTPROJECT_NAME);

		try {
			ProjectHelper.findProjectManifest(project);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		IProject projectWithoutManifest = root
				.getProject(ProjectDependingTests.TESTPROJECT_NAME_NO_MANIFEST);

		try {
			ProjectHelper.findProjectManifest(projectWithoutManifest);
			fail("no exception fired");
		} catch (FileNotFoundException e) {
		}

		try {
			ProjectHelper.findProjectManifest((IProject) null);
			fail("did not throw exception");
		} catch (Exception e) {
		}

	}

	public void testFindProjectManifestICompilationUnit() {

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		// positive case

		IProject project = root
				.getProject(ProjectDependingTests.TESTPROJECT_NAME);

		try {
			project.open(null /* IProgressMonitor */);
		} catch (CoreException e) {
			fail(ProjectDependingTests.TESTPROJECT_NAME
					+ " cannot be found. use the launch configuration file.");
		}

		IJavaProject javaProject = JavaCore.create(project);

		IType lwType = null;

		try {
			lwType = javaProject.findType("testproject.Activator");
		} catch (JavaModelException e) {
		}

		ICompilationUnit lwCompilationUnit = lwType.getCompilationUnit();

		try {
			ProjectHelper.findProjectManifest(lwCompilationUnit);
		} catch (FileNotFoundException e) {
		}

		// negative case

		IProject projectWithoutManifest = root
				.getProject(ProjectDependingTests.TESTPROJECT_NAME_NO_MANIFEST);

		try {
			projectWithoutManifest.open(null /* IProgressMonitor */);
		} catch (CoreException e) {
			fail(ProjectDependingTests.TESTPROJECT_NAME_NO_MANIFEST
					+ " cannot be found. use the launch configuration file.");
		}

		IJavaProject javaProjectWithoutManifest = JavaCore
				.create(projectWithoutManifest);

		try {
			lwType = javaProjectWithoutManifest
					.findType("testproject.Activator");
		} catch (JavaModelException e) {
		}

		lwCompilationUnit = lwType.getCompilationUnit();

		try {
			ProjectHelper.findProjectManifest(lwCompilationUnit);
			fail("no exception fired");
		} catch (FileNotFoundException e) {
		}

		try {
			ProjectHelper.findProjectManifest((ICompilationUnit) null);
			fail("no exception fired");
		} catch (Exception e) {
		}

	}

	public void testFindProjectManifestIFile() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		// positive case

		IProject project = root
				.getProject(ProjectDependingTests.TESTPROJECT_NAME);

		try {
			project.open(null /* IProgressMonitor */);
		} catch (CoreException e) {
			fail(ProjectDependingTests.TESTPROJECT_NAME
					+ " cannot be found. use the launch configuration file.");
		}

		IFile someFile = project.getFile("plugin.xml");

		try {
			ProjectHelper.findProjectManifest(someFile);
		} catch (FileNotFoundException e) {
		}

		// negative case

		project = root
				.getProject(ProjectDependingTests.TESTPROJECT_NAME_NO_MANIFEST);

		try {
			project.open(null /* IProgressMonitor */);
		} catch (CoreException e) {
			fail(ProjectDependingTests.TESTPROJECT_NAME_NO_MANIFEST
					+ " cannot be found. use the launch configuration file.");
		}

		someFile = project.getFile("plugin.xml");

		try {
			ProjectHelper.findProjectManifest(someFile);
			fail("did not throw exception");
		} catch (FileNotFoundException e) {
		}

		// null case

		try {
			ProjectHelper.findProjectManifest((IFile) null);
			fail("did not throw exception");
		} catch (Exception e) {
		}

	}

	public void testFindProjectICompilationUnit() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		IProject project = root
				.getProject(ProjectDependingTests.TESTPROJECT_NAME);

		try {
			project.open(null /* IProgressMonitor */);
		} catch (CoreException e) {
			fail(ProjectDependingTests.TESTPROJECT_NAME
					+ " cannot be found. use the launch configuration file.");
		}
		IJavaProject javaProject = JavaCore.create(project);

		IType lwType = null;

		try {
			lwType = javaProject.findType("testproject.Activator");
		} catch (JavaModelException e) {
		}

		ICompilationUnit lwCompilationUnit = lwType.getCompilationUnit();

		assertEquals(project, ProjectHelper.findProject(lwCompilationUnit));

		try {
			ProjectHelper.findProject((ICompilationUnit) null);
			fail("did not throw exception");
		} catch (Exception e) {
		}

	}

	public void testFindProjectIFile() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		IProject project = root
				.getProject(ProjectDependingTests.TESTPROJECT_NAME);

		try {
			project.open(null /* IProgressMonitor */);
		} catch (CoreException e) {
			fail(ProjectDependingTests.TESTPROJECT_NAME
					+ " cannot be found. use the launch configuration file.");
		}

		IFile someFile = project.getFile("plugin.xml");

		assertEquals(project, ProjectHelper.findProject(someFile));

		try {
			ProjectHelper.findProject((IFile) null);
			fail("did not throw exception");
		} catch (Exception e) {
		}

	}

}
