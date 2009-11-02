// $Id:$
package de.partmaster.obfuscate4e.updater;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import de.partmaster.obfuscate4e.ProjectInfo;

public abstract class AbstractUpdater implements IWorkspaceRunnable, IUpdater {

	private final ProjectInfo itsProjectInfo;
	protected final String itsDescription;

	public AbstractUpdater(ProjectInfo projectInfo, String description) {
		itsProjectInfo = projectInfo;
		itsDescription = description;
	}

	public ProjectInfo getProjectInfo() {
		return itsProjectInfo;
	}
	

	protected Shell getShell() {
		return PlatformUI.getWorkbench().getDisplay().getActiveShell();
	}

	public boolean isValidManifest(IProgressMonitor monitor)
			throws CoreException {
		// Force the build if autobuild is off
		IProject project = itsProjectInfo.getProject();
		if (!project.getWorkspace().isAutoBuilding()) {
			project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD,
					MANIFEST_BUILDER_ID, null, monitor);
		}
		return isFileContainsErrors(itsProjectInfo.getManifest());
	}

	public boolean isFileContainsErrors(IFile file) throws CoreException {
		IMarker[] markers = file.findMarkers(IMarker.PROBLEM, true,
				IResource.DEPTH_ZERO);
		for (int i = 0; i < markers.length; i++) {
			Object attribute = markers[i].getAttribute(IMarker.SEVERITY);
			if (attribute != null && attribute instanceof Integer) {
				if (((Integer) attribute).intValue() == IMarker.SEVERITY_ERROR)
					return true;
			}
		}
		return false;
	}

	public String getDescription() {
		return itsDescription;
	}
}