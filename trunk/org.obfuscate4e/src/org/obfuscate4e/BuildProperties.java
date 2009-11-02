package org.obfuscate4e;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.pde.core.IEditableModel;
import org.eclipse.pde.core.build.IBuild;
import org.eclipse.pde.core.build.IBuildEntry;
import org.eclipse.pde.core.build.IBuildModel;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.core.build.WorkspaceBuildModel;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.obfuscate4e.updater.IUpdater;

public class BuildProperties {
	public static final String OVERWRITE_BUILD_PROPERTY = "overwrite build property?";
	public static final String NL = System.getProperty("line.separator");

	private final ProjectInfo itsProjectInfo;
	private IBuildModel itsBuildModel;

	BuildProperties(ProjectInfo info) {
		itsProjectInfo = info;
	}

	/**
	 * adds or overwrites an entry in the 'build.properties'.<br>
	 * returns <b>true</b> if adding was successful,<br>
	 * <b>false</b> is dialog did not confirm adding
	 * 
	 * @param key
	 *            the build property's name
	 * @param value
	 *            the build property's value
	 * @return <b>true</b> if adding was successful,<br>
	 *         <b>false</b> is dialog did not confirm adding
	 * 
	 * @throws CoreException
	 */
	public boolean addBuildEntry(String key, String value) throws CoreException {
		Assert.isNotNull(key);
		Assert.isNotNull(value);
		boolean writeEntry = true;
		if (hasBuildEntry(key)) {
			if (getBuild().getEntry(key).contains(value)) {
				return true;
			}

			String oldValue = getBuild().getEntry(key).getTokens()[0];
			writeEntry = confirmOverwritingBuildProperty(key, oldValue, value);

			if (writeEntry) {
				removeExistingBuildEntry(key);
			}
		}

		if (writeEntry) {
			IBuildModel buildModel = getBuildModel();
			IBuildEntry buildEntry = buildModel.getFactory().createEntry(key);
			buildEntry.addToken(value);
			getBuild().add(buildEntry);

			((IEditableModel) buildModel).save();
		}
		return writeEntry;
	}

	private boolean confirmOverwritingBuildProperty(String key,
			String oldValue, String value) {

		Shell activeShell = PlatformUI.getWorkbench().getDisplay()
				.getActiveShell();

		String message = "overwrite build property entry \"" + key + "\"?" + NL
				+ "old value: " + oldValue + NL + "new value: " + value;

		MessageDialog dialog = new MessageDialog(activeShell,
				OVERWRITE_BUILD_PROPERTY, null, message,
				MessageDialog.QUESTION, new String[] {
						IDialogConstants.OK_LABEL,
						IDialogConstants.CANCEL_LABEL }, 0);

		return dialog.open() == 0;
	}

	/**
	 * removes an entry with the given name from the 'build.properties'
	 * 
	 * @param key
	 *            the build property's name
	 * @param build
	 *            the representation of the 'build.properties'
	 * @throws CoreException
	 */
	public void removeExistingBuildEntry(String key) throws CoreException {
		IBuildEntry entry = getBuild().getEntry(key);
		if (entry != null) {
			getBuild().remove(entry);
		}
	}

	/**
	 * returns whether an entry exists for the given key
	 * 
	 * @param key
	 *            the build property's name
	 * @param build
	 *            the representation of the 'build.properties'
	 * @return
	 */
	public boolean hasBuildEntry(final String key) {
		return getBuild().getEntry(key) != null;
	}

	public boolean isCustomBuildCallbacksRegistered() {
		if (hasBuildEntry(IUpdater.KEY_CUSTOM_BUILD_CALLBACKS)) {
			return getProperties(IUpdater.KEY_CUSTOM_BUILD_CALLBACKS)[0]
					.equals(IUpdater.FILE_CUSTOM_BUILD_CALLBACKS);
		}
		return false;
	}

	private IBuild getBuild() {
		IBuildModel buildModel = getBuildModel();
		if (buildModel == null) {
			return null;
		}
		return buildModel.getBuild();
	}


	public String[] getProperties(String key) {
		return getBuild().getEntry(key).getTokens();
	}

	public void registerCustomBuildCallbacks() throws CoreException {
		addBuildEntry(IUpdater.KEY_CUSTOM_BUILD_CALLBACKS,
				IUpdater.FILE_CUSTOM_BUILD_CALLBACKS);
	}
	
	/**
	 * returns the Eclipse BuildModel
	 * 
	 * @return the Eclipse BuildModel
	 */
	private IBuildModel getBuildModel() {
		if (itsBuildModel == null) {
			IPluginModelBase pluginModel = itsProjectInfo.getPluginModel();
			itsBuildModel = createBuildModel(pluginModel);
		}
		return itsBuildModel;
	}


	/**
	 * see org.eclipse.pde.internal.core.ClasspathUtilCore.getBuild
	 */
	private IBuildModel createBuildModel(final IPluginModelBase model) {
		IProject project = model.getUnderlyingResource().getProject();
		IFile buildFile = project.getFile("build.properties"); //$NON-NLS-1$
		if (!buildFile.exists()) {
			return null;
		}
		WorkspaceBuildModel buildModel = new WorkspaceBuildModel(buildFile);
		buildModel.load();
		return buildModel;
	}

}
