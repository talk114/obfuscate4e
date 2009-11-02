// $Id:$

package de.partmaster.obfuscate4e.updater;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import de.partmaster.obfuscate4e.Activator;
import de.partmaster.obfuscate4e.generator.IBuildGenerator;
import de.partmaster.obfuscate4e.generator.ProjectData;
import de.partmaster.obfuscate4e.preferences.PreferenceConstants;

/**
 * The updater for the CustomBuildCallbacks.xml
 * 
 * @author fwo
 * 
 */
abstract public class AbstractBuildScriptUpdater extends AbstractFileUpdater {

	public static final String DESCRIPTION = "Create or update '"
			+ FILE_CUSTOM_BUILD_CALLBACKS + "'";
	private IBuildGenerator itsGenerator;

	/**
	 * The constructor
	 * 
	 * @param generator
	 */
	public AbstractBuildScriptUpdater() {
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
		IFile file = getProjectInfo().getFile(getFilename());
		String backupCode = "";

		List excludePackages = getProjectInfo().getExportedPackages();
		List excludeClasses = getProjectInfo().getClassesInExtensions();
		String generatedCode = getGenerator().generate(
				new ProjectData(excludePackages, excludeClasses, ""));

		if (file.exists()) {
			try {
				InputStream contents = file.getContents();
				backupCode = inputStreamToString(contents);
			} catch (IOException e) {
				return;
			}
			if (generatedCode.equals(backupCode)) {
				return;
			}

			boolean confirmation = overwriteDialog(file);

			if (confirmation == false) {
				return;
			}
			file.delete(true, monitor);
			saveBackupCopy(backupCode, monitor);
		}

		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				generatedCode.getBytes());

		file.create(inputStream, false, monitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.partmaster.obfuscate4e.updater.AbstractUpdater#
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
	 * @seede.partmaster.obfuscate4e.updater.AbstractFileUpdater#
	 * isSavingBackupFileEnabled()
	 */
	protected boolean isSavingBackupFileEnabled() {
		return true; // save customBuildCallback.xml backup file anyway
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.partmaster.obfuscate4e.updater.AbstractUpdater#
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
