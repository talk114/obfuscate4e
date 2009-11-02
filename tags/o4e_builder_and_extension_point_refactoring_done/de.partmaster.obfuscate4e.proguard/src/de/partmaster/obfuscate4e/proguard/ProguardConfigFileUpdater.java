package de.partmaster.obfuscate4e.proguard;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;

import de.partmaster.obfuscate4e.ObfuscationExclusion;
import de.partmaster.obfuscate4e.ProjectInfo;
import de.partmaster.obfuscate4e.generator.ProjectData;
import de.partmaster.obfuscate4e.preferences.PreferenceConstants;
import de.partmaster.obfuscate4e.proguard.generator.ProguardConfigGenerator;
import de.partmaster.obfuscate4e.updater.AbstractFileUpdater;

/**
 * 
 * The Proguard configuration file updater provides the methods to generate the
 * proguard.cfg file using the Proguard generator.
 * 
 * @see ProguardConfigGenerator
 * @author fwo
 * 
 */
public class ProguardConfigFileUpdater extends AbstractFileUpdater {

	private static final String NL = System.getProperty("line.separator");
	public static final String TAB = "\t";
	public static final String PROGUARD_CONFIG = "proguard.cfg";

	public static final String DESCRIPTION = "Create or update '"
			+ PROGUARD_CONFIG + "'";

	public static final String STATIC_MARKER = "###STATIC###";

	private String itsStaticStatements = "";

	/**
	 * the constructor for the Proguard configuration file updater
	 */
	public ProguardConfigFileUpdater() {
		super(PROGUARD_CONFIG, DESCRIPTION);
	}

	/**
	 * starts the Proguard generator and writes the generated configuration to
	 * the file.
	 */
	public void run(IProgressMonitor monitor) throws CoreException {

		IFile backupFile = getProjectInfo().getFile(getBackupFilename());
		IFile configFile = getProjectInfo().getFile(getFilename());

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		boolean saveBackup = store
				.getBoolean(PreferenceConstants.SAVE_PROGUARD_CONFIG_BACKUP);

		if (configFile.exists()) {
			itsStaticStatements += extractStaticExcludesFromProguardConfigFile(configFile);
		}else{
			itsStaticStatements += NL + "-verbose" + NL + "-ignorewarnings"
			+ NL + "-printmapping \"@TEMPDIR@/@MAPPINGFILE@\"" + NL
			+ "-printconfiguration \"@TEMPDIR@/@CONFIGFILE@\"" + NL;
		}

		ProjectInfo projectInfo = getProjectInfo();
		ProjectData projectData = createProjectData(projectInfo);
		ProguardConfigGenerator generator = new ProguardConfigGenerator();
		String generatedCode = generator.generate(projectData);

		String fileContent = "";

		if (configFile.exists()) {
			// fetch old content
			try {
				fileContent = inputStreamToString(configFile.getContents())+NL;
				if (fileContent.equals(generatedCode)) {
					// in case there is no change, abort
					return;
				}
			} catch (IOException e1) {
				return;
			}

			// save backup
			if (saveBackup) {
				if (backupFile.exists()) {
					backupFile.delete(true, monitor);
				}
				backupFile.create(configFile.getContents(), true, monitor);
			}

			// ask for confirmation
			boolean confirmation = overwriteDialog(configFile);
			if (confirmation == false) {
				return;
			}
			configFile.delete(true, monitor);
		}

		InputStream inputStream = new ByteArrayInputStream(generatedCode
				.getBytes());

		configFile.create(inputStream, false, monitor);
		itsStaticStatements = ""; // reset

	}

	private ProjectData createProjectData(ProjectInfo projectInfo) {
		List excludePackages = projectInfo.getExportedPackages();
		List excludeClasses = projectInfo.getClassesInExtensions();

		return new ProjectData(excludePackages, excludeClasses,
				itsStaticStatements);
	}


	private String extractStaticExcludesFromProguardConfigFile(
			IFile proguardConfigFile) throws CoreException {
		String proguardConfigFileContent = "";
		String staticExcludes = "";
		int beginIndex = -1;

		try {
			proguardConfigFileContent = inputStreamToString(proguardConfigFile
					.getContents())
					+ NL;
		} catch (IOException e) {
			return "";
		}

		if (proguardConfigFileContent.indexOf(STATIC_MARKER) >= 0) {
			// marker found
			beginIndex = proguardConfigFileContent.indexOf(STATIC_MARKER);

			if (proguardConfigFileContent.substring(beginIndex).indexOf(NL) >= 0) {
				beginIndex += proguardConfigFileContent.substring(beginIndex)
						.indexOf(NL)
						+ NL.length();
				staticExcludes = proguardConfigFileContent
						.substring(beginIndex);

			} else {
				return "";
			}
			staticExcludes = proguardConfigFileContent.substring(beginIndex);
		} else {
			// no marker found
			String fileTail = cutOffHeader(proguardConfigFileContent);
			staticExcludes = NL
					+ "# No marker was found in the previous version of this .cfg file."
					+ NL + "# The complete file has been copied." + NL
					+ "# TODO : remove duplications" + NL + NL + fileTail;
		}
		return staticExcludes;
	}

	private String cutOffHeader(String fileContent) {
		int index = fileContent.indexOf(NL + "-") + NL.length();
		return fileContent.substring(index);
	}

	/**
	 * Adds a class exclusion statement to the ProjectInfo static statements
	 * field.
	 * 
	 * @param qualifier
	 *            The fully qualified path to the class
	 * @param comment
	 *            A description
	 */
	public void addStaticClassExclusion(ObfuscationExclusion exclusion) {
		itsStaticStatements += NL + // 1.
				"# " + exclusion.getDescription() + NL + // 2.
				"-keep class " + exclusion.getPath() + " {" + NL + // 3.
				TAB + "public protected *;" + NL + "}" + NL;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.partmaster.obfuscate4e.updater.AbstractUpdater#
	 * isOverwriteConfirmationEnabled()
	 */
	protected boolean isOverwriteConfirmationEnabled() {
		return Activator.getDefault().getPreferenceStore().getBoolean(
				PreferenceConstants.ASK_FOR_OVERWRITING_PROGUARDCFG);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.partmaster.obfuscate4e.updater.AbstractFileUpdater#
	 * isSavingBackupFileEnabled()
	 */
	protected boolean isSavingBackupFileEnabled() {
		return Activator.getDefault().getPreferenceStore().getBoolean(
				PreferenceConstants.SAVE_PROGUARD_CONFIG_BACKUP);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.partmaster.obfuscate4e.updater.AbstractUpdater#
	 * rememberConfirmationDecision()
	 */
	protected void rememberConfirmationDecision() {
		Activator.getDefault().getPreferenceStore().setValue(
				PreferenceConstants.ASK_FOR_OVERWRITING_PROGUARDCFG, false);
	}

}
