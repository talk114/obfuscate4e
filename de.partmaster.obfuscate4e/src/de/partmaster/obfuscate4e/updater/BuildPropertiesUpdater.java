// $Id:$

package de.partmaster.obfuscate4e.updater;

import java.text.MessageFormat;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.pde.core.IEditableModel;
import org.eclipse.pde.core.build.IBuild;
import org.eclipse.pde.core.build.IBuildEntry;
import org.eclipse.pde.core.build.IBuildModel;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class BuildPropertiesUpdater extends AbstractUpdater {

	public static final String EXISTING_KEY_MESSAGE = "Key ''{0}'' exists in build.properties: \nOverwrite?";

	public static final String DESCRIPTION = "Update build.properties";

	public BuildPropertiesUpdater() {
		super(DESCRIPTION);
	}

	public void run(IProgressMonitor monitor) throws CoreException {
		addBuildEntry(IUpdater.KEY_CUSTOM_BUILD_CALLBACKS,
				IUpdater.FILE_CUSTOM_BUILD_CALLBACKS);
	}

	private Shell getShell() {
		return PlatformUI.getWorkbench().getDisplay().getActiveShell();
	}

	private void addBuildEntry(final String key, final String value)
			throws CoreException {
		Assert.isNotNull(key);
		Assert.isNotNull(value);
		IBuildModel buildModel = getProjectInfo().getBuildModel();
		IBuild build = buildModel.getBuild();
		if (hasBuildEntry(key, build)) {
			String message = MessageFormat.format(EXISTING_KEY_MESSAGE,
					new String[] { key });
			if (!MessageDialog.openConfirm(getShell(), getDescription(),
					message)) {
				return;
			}
		}
		removeExistingBuildEntry(key, build);

		IBuildEntry buildEntry = buildModel.getFactory().createEntry(key);
		buildEntry.addToken(value);
		build.add(buildEntry);

		((IEditableModel) buildModel).save();
	}

	private void removeExistingBuildEntry(final String key, IBuild build)
			throws CoreException {
		IBuildEntry entry = build.getEntry(key);
		if (entry != null) {
			build.remove(entry);
		}
	}

	private boolean hasBuildEntry(final String key, IBuild build) {
		return build.getEntry(key) != null;
	}

}
