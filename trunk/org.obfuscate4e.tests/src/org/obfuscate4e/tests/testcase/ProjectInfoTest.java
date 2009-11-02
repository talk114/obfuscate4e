package org.obfuscate4e.tests.testcase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import junit.framework.TestCase;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.util.BusyIndicatorRunnableContext;
import org.eclipse.jdt.internal.ui.wizards.buildpaths.BuildPathsBlock;
import org.obfuscate4e.ProjectInfo;
import org.obfuscate4e.tests.ProjectDependingTests;

public class ProjectInfoTest extends TestCase {

	private static final String FILENAME_PROJECT = ".project"; //$NON-NLS-1$
	private static final String FILENAME_CLASSPATH = ".classpath"; //$NON-NLS-1$

	public ProjectInfoTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();

	}

	public void testProjectInfoICompilationUnit() {

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		IProject project = root.getProject(ProjectDependingTests.TESTPROJECT_NAME);

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
			new ProjectInfo(lwCompilationUnit);
		} catch (FileNotFoundException e) {
			fail("cannot instantiate ProjectInfo by an ICompilationUnit");
		}

	}

	public void testProjectInfoIProject() {

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		IProject project = root.getProject(ProjectDependingTests.TESTPROJECT_NAME);

		try {
			project.open(null /* IProgressMonitor */);
		} catch (CoreException e) {
			fail(ProjectDependingTests.TESTPROJECT_NAME
					+ " cannot be found. use the launch configuration file.");
		}

		try {
			new ProjectInfo(project);
		} catch (FileNotFoundException e) {
			fail("cannot instantiate ProjectInfo by an IProject");
		}

	}

	public void testProjectInfoNullParameter() {

		try {
			new ProjectInfo((ICompilationUnit) null);
			fail("did not throw exception");
		} catch (Exception e) {
		}

		try {
			new ProjectInfo((IProject) null);
			fail("did not throw exception");
		} catch (Exception e) {
		}

	}

	public void testProjectInfoNoManifestProject() {

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		IProject project = root
				.getProject(ProjectDependingTests.TESTPROJECT_NAME_NO_MANIFEST);

		try {
			project.open(null /* IProgressMonitor */);
		} catch (CoreException e) {
			fail(ProjectDependingTests.TESTPROJECT_NAME_NO_MANIFEST
					+ " cannot be found. use the launch configuration file.");
		}

		IJavaProject javaProject = JavaCore.create(project);

		IType lwType = null;

		try {
			lwType = javaProject.findType(ProjectDependingTests.TESTPROJECT_NAME
					.toLowerCase()
					+ ".Activator");
		} catch (JavaModelException e) {
		}

		ICompilationUnit lwCompilationUnit = lwType.getCompilationUnit();

		try {
			new ProjectInfo(lwCompilationUnit);
			fail("did not throw exception");
		} catch (Exception e) {
		}

		try {
			new ProjectInfo(project);
			fail("did not throw exception");
		} catch (Exception e) {
		}

	}

	private void copyFile(IFileStore source, File target) throws IOException,
			CoreException {
		InputStream is = source.openInputStream(EFS.NONE, null);
		FileOutputStream os = new FileOutputStream(target);
		copyFile(is, os);
	}

	private void copyFile(InputStream is, OutputStream os) throws IOException {
		try {
			byte[] buffer = new byte[8192];
			while (true) {
				int bytesRead = is.read(buffer);
				if (bytesRead == -1)
					break;

				os.write(buffer, 0, bytesRead);
			}
		} finally {
			try {
				is.close();
			} finally {
				os.close();
			}
		}
	}

	public void init(final IJavaProject jproject, IPath defaultOutputLocation,
			IClasspathEntry[] defaultEntries,
			boolean defaultsOverrideExistingClasspath) {
		if (!defaultsOverrideExistingClasspath && jproject.exists()
				&& jproject.getProject().getFile(".classpath").exists()) { //$NON-NLS-1$
			defaultOutputLocation = null;
			defaultEntries = null;
		}
		getBuildPathsBlock().init(jproject, defaultOutputLocation,
				defaultEntries);
	}

	private BuildPathsBlock getBuildPathsBlock() {
		return new BuildPathsBlock(new BusyIndicatorRunnableContext(), null, 0,
				false, null);
	}

}
