// $Id:$

package org.obfuscate4e.updater;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.obfuscate4e.Activator;
import org.obfuscate4e.generator.IBuildGenerator;
import org.obfuscate4e.generator.ProjectData;
import org.obfuscate4e.preferences.PreferenceConstants;


/**
 * The updater for the CustomBuildCallbacks.xml
 * 
 * @author fwo
 * 
 */
abstract public class CustomBuildScriptUpdaterAdapter extends AbstractFileUpdater {

	public static final String DESCRIPTION = "Update '"
			+ FILE_CUSTOM_BUILD_CALLBACKS + "'";
	private IBuildGenerator itsGenerator;

	/**
	 * The constructor
	 * 
	 * @param generator
	 */
	public CustomBuildScriptUpdaterAdapter() {
		super(FILE_CUSTOM_BUILD_CALLBACKS, DESCRIPTION);

	}

	/**
	 * is a general update process method:
	 * <p>
	 * in case the file already exists and the settings do enable backups, a<br>
	 * copy of the old file will get saved.<br>
	 * in case the file already exists and the settings do enable a confirmation
	 * <br>
	 * dialog, a message dialog will be shown.
	 * <p>
	 * Then generated code will be written to the filesystem.
	 */
	public void run(IProgressMonitor monitor) throws CoreException {
		IFile file = getProject().getFile(getFilename());

		List excludePackages = getProjectInfo().getExportedPackages();
		List excludeClasses = getProjectInfo().getClassesInExtensions();
		String generatedCode = getGenerator().generate(
				new ProjectData(excludePackages, excludeClasses, ""));
		
		saveFile(file, generatedCode, monitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.obfuscate4e.updater.AbstractUpdater#
	 * isOverwriteConfirmationEnabled()
	 */
	protected boolean isOverwriteConfirmationEnabled() {
		return Activator
				.getDefault()
				.getPreferenceStore()
				.getBoolean(
						PreferenceConstants.ASK_FOR_OVERWRITING_CUSTOMBUILDCALLBACKSXML);
	}

	protected void setGenerator(IBuildGenerator generator) {
		itsGenerator = generator;
	}

	/**
	 * returns the generator
	 * 
	 * @return the generator
	 */
	protected IBuildGenerator getGenerator() {
		if (itsGenerator == null) {
			setupGenerator();
		}
		return itsGenerator;
	}

	/**
	 * sets up the specific generator for the build script updater
	 */
	abstract protected void setupGenerator();

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.obfuscate4e.updater.AbstractUpdater#
	 * rememberConfirmationDecision()
	 */
	protected void rememberConfirmationDecision() {
		Activator
				.getDefault()
				.getPreferenceStore()
				.setValue(
						PreferenceConstants.ASK_FOR_OVERWRITING_CUSTOMBUILDCALLBACKSXML,
						false);
	}

}
