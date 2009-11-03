package org.obfuscate4e.updater;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.obfuscate4e.ProjectInfo;


/**
 * The interface for all Obfuscate4e updater
 * 
 * @author fwo
 * 
 */
public interface IUpdater extends IWorkspaceRunnable {

	public static final String FILE_CUSTOM_BUILD_CALLBACKS = "customBuildCallbacks.xml";

	public static final String KEY_CUSTOM_BUILD_CALLBACKS = "customBuildCallbacks";

	public static final String MANIFEST_BUILDER_ID = "org.eclipse.pde.ManifestBuilder";

	/**
	 * sets the ProjectInfo for the updater
	 * 
	 * @param projectInfo
	 */
	public void configureProjectSettings(ProjectInfo projectInfo);
}