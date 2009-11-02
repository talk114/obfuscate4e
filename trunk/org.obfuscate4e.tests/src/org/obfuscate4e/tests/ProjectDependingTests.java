package org.obfuscate4e.tests;

import java.util.zip.ZipFile;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.internal.ui.wizards.buildpaths.BuildPathsBlock;
import org.obfuscate4e.tests.testcase.ProjectHelperTest;
import org.obfuscate4e.tests.testcase.ProjectInfoTest;
import org.obfuscate4e.tests.util.Unzipper;

public class ProjectDependingTests extends TestSuite {

	public static final String CONTAINER_PROJECT_NAME = "zipFile";
	public static final String CONTAINER_FILENAME = "projects.zip";

	public static final String TESTPROJECT_NAME = "testproject";
	public static final String TESTPROJECT_NAME_NO_MANIFEST = TESTPROJECT_NAME
			+ "WithoutManifest";

	public static Test suite() {

		TestSuite suite = new TestSuite();

		suite.addTestSuite(ProjectInfoTest.class);
		suite.addTestSuite(ProjectHelperTest.class);

		TestSetup wrapper = new TestSetup(suite) {
			protected void setUp() {
				singularSetUp();
			}

			protected void tearDown() {
				singularTearDown();
			}
		};

		return wrapper;
	}

	public static void singularSetUp() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		IProject project = root.getProject(CONTAINER_PROJECT_NAME);

		try {

			project.open(null);

			ZipFile zippedProjects = new ZipFile(project.getFile(
					CONTAINER_FILENAME).getLocation().toOSString());

			String location = project.getLocation().removeLastSegments(1)
					.toOSString();

			Unzipper.unzip(zippedProjects, location);
			
			// we're assuming the projects got unzipped in the workspace root

			createProjectFromExistingSource(root, TESTPROJECT_NAME);

			createProjectFromExistingSource(root, TESTPROJECT_NAME_NO_MANIFEST);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void createProjectFromExistingSource(IWorkspaceRoot root,
			String projectName) throws CoreException {

		IProject project = root.getProject(projectName);

		if (!project.exists()) {

			BuildPathsBlock.createProject(project, root.getLocationURI(), null);

		}

	}

	public static void singularTearDown() {

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		deleteProject(root, TESTPROJECT_NAME);
		deleteProject(root, TESTPROJECT_NAME_NO_MANIFEST);

	}

	private static void deleteProject(IWorkspaceRoot root, String projectName) {
		try {
			root.getProject(projectName).delete(true, true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

}
