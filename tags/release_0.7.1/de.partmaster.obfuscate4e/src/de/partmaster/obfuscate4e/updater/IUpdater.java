package de.partmaster.obfuscate4e.updater;

import org.eclipse.core.resources.IWorkspaceRunnable;

import de.partmaster.obfuscate4e.ProjectInfo;

public interface IUpdater extends IWorkspaceRunnable {

	public static final String FILE_CUSTOM_BUILD_CALLBACKS = "customBuildCallbacks.xml";
	
	public static final String KEY_CUSTOM_BUILD_CALLBACKS = "customBuildCallbacks";
	
	public static final String MANIFEST_BUILDER_ID = "org.eclipse.pde.ManifestBuilder";

	public ProjectInfo getProjectInfo();
	
	public void setProjectInfo(ProjectInfo projectInfo);
}