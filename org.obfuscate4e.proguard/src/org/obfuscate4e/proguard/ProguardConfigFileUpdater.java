package org.obfuscate4e.proguard;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.obfuscate4e.ObfuscationExclusion;
import org.obfuscate4e.ProjectInfo;
import org.obfuscate4e.generator.ProjectData;
import org.obfuscate4e.preferences.PreferenceConstants;
import org.obfuscate4e.proguard.generator.ProguardConfigGenerator;
import org.obfuscate4e.updater.AbstractFileUpdater;
import org.obfuscate4e.util.ChecksumHelper;

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

	public static final String DESCRIPTION = "Update '" + PROGUARD_CONFIG + "'";

	public static final String CONFIG_STATEMENTS = NL
			+ "-dontusemixedcaseclassnames" + NL + "-verbose" + NL
			+ "-ignorewarnings" + NL
			+ "-printmapping \"@TEMPDIR@/@MAPPINGFILE@\"" + NL
			+ "-printconfiguration \"@TEMPDIR@/@CONFIGFILE@\"";

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

		IFile configFile = getProject().getFile(getFilename());

		if (configFile.exists()) {
			itsStaticStatements += extractStaticExcludesFromProguardConfigFile(configFile);
		} else {
			itsStaticStatements += CONFIG_STATEMENTS;
		}

		ProjectInfo projectInfo = getProjectInfo();
		ProjectData projectData = createProjectData(projectInfo);
		ProguardConfigGenerator generator = new ProguardConfigGenerator();
		String generatedCode = generator.generate(projectData);

		saveFile(configFile, generatedCode, monitor);

		itsStaticStatements = "";

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
			proguardConfigFileContent = ChecksumHelper.inputStreamToString(proguardConfigFile
					.getContents());
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
			staticExcludes = CONFIG_STATEMENTS
					+ NL
					+ NL
					+ "# No marker was found in the previous version of this .cfg file."
					+ NL + "# The complete file has been copied." + NL
					+ "# TODO : remove duplications" + NL + NL + NL + fileTail;
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
		String type = "class";
		if (exclusion.getType() == ObfuscationExclusion.INTERFACE) {
			type = "interface";
		}

		itsStaticStatements += NL + //
				"# begin o4e managed" + NL + //
				"# MANAGED: manual" + NL + //
				"# OBJECT: " + exclusion.getPath() + NL + //
				"# CAUSE: user action" + NL + //
				"# COMMENT: " + exclusion.getDescription() + NL + //
				"# TYPE: " + type + " exclusion" + NL + //
				"-keep class " + exclusion.getPath() + " {" + NL + //
				TAB + "public protected *;" + NL + "}" + NL + //
				"# end o4e managed" + NL;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.obfuscate4e.updater.AbstractUpdater#
	 * isOverwriteConfirmationEnabled()
	 */
	protected boolean isOverwriteConfirmationEnabled() {
		return Activator.getDefault().getPreferenceStore().getBoolean(
				PreferenceConstants.ASK_FOR_OVERWRITING_PROGUARDCFG);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.obfuscate4e.updater.AbstractUpdater#
	 * rememberConfirmationDecision()
	 */
	protected void rememberConfirmationDecision() {
		Activator.getDefault().getPreferenceStore().setValue(
				PreferenceConstants.ASK_FOR_OVERWRITING_PROGUARDCFG, false);
	}

}
