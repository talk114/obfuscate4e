package de.partmaster.obfuscate4e.updater;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import de.partmaster.obfuscate4e.ProjectInfo;

public interface IUpdater {

	public static final String FILE_CUSTOM_BUILD_CALLBACKS = "customBuildCallbacks.xml";
	
	public static final String KEY_CUSTOM_BUILD_CALLBACKS = "customBuildCallbacks";
	
	public static final String MANIFEST_BUILDER_ID = "org.eclipse.pde.ManifestBuilder";

	public abstract void run(IProgressMonitor monitor) throws CoreException;

	public ProjectInfo getProjectInfo();
}