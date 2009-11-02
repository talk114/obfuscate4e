// $Id:$
package de.partmaster.obfuscate4e.updater;

import de.partmaster.obfuscate4e.ProjectInfo;

public abstract class AbstractUpdater implements IUpdater {

	protected final String itsDescription;
	private ProjectInfo itsProjectInfo;

	public AbstractUpdater(String description) {
		itsDescription = description;
	}

	public void setProjectInfo(ProjectInfo projectInfo) {
		itsProjectInfo = projectInfo;
	}
	public ProjectInfo getProjectInfo() {
		return itsProjectInfo;
	}
	

//	public boolean isValidManifest(IProgressMonitor monitor)
//			throws CoreException {
//		// Force the build if autobuild is off
//		IProject project = itsProjectInfo.getProject();
//		if (!project.getWorkspace().isAutoBuilding()) {
//			project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD,
//					MANIFEST_BUILDER_ID, null, monitor);
//		}
//		return isFileContainsErrors(itsProjectInfo.getManifest());
//	}
//
//	public boolean isFileContainsErrors(IFile file) throws CoreException {
//		IMarker[] markers = file.findMarkers(IMarker.PROBLEM, true,
//				IResource.DEPTH_ZERO);
//		for (int i = 0; i < markers.length; i++) {
//			Object attribute = markers[i].getAttribute(IMarker.SEVERITY);
//			if (attribute != null && attribute instanceof Integer) {
//				if (((Integer) attribute).intValue() == IMarker.SEVERITY_ERROR)
//					return true;
//			}
//		}
//		return false;
//	}

	public String getDescription() {
		return itsDescription;
	}
}