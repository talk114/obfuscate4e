// $Id:$

package de.partmaster.obfuscate4e.updater;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.pde.core.IEditableModel;
import org.eclipse.pde.core.build.IBuild;
import org.eclipse.pde.core.build.IBuildEntry;
import org.eclipse.pde.core.build.IBuildModel;

import de.partmaster.obfuscate4e.ProjectInfo;

public class BuildPropertiesUpdater extends AbstractUpdater {

	private boolean itsModelIsModified = false;

	public BuildPropertiesUpdater(ProjectInfo projectInfo) {
		super(projectInfo);
	}

	public void run(IProgressMonitor monitor) throws CoreException {
		addBuildEntry(IUpdater.KEY_CUSTOM_BUILD_CALLBACKS,
				IUpdater.FILE_CUSTOM_BUILD_CALLBACKS);
		if (itsModelIsModified) {
			saveBuildModel();
		}
	}

	public void addBuildEntry(final String key, final String value)
			throws CoreException {
		if (existsBuildEntry(key)) {
			if (!MessageDialog.openConfirm(getShell(), "Confirm", "Key '" + key
					+ "' exists in build.properties: \nOverwrite?")) {
				return;
			}
			removeBuildEntry(key);
		}
		IBuildModel buildModel = getProjectInfo().getBuildModel();

		IBuildEntry buildEntry = buildModel.getFactory().createEntry(key);
		buildEntry.addToken(value);
		getProjectInfo().getBuild().add(buildEntry);
		itsModelIsModified = true;
	}

	public void saveBuildModel() {
		((IEditableModel) getProjectInfo().getBuildModel()).save();
		itsModelIsModified = false;
	}

	public void removeBuildEntry(final String key) throws CoreException {
		IBuild build = getProjectInfo().getBuild();
		build.remove(getBuildEntry(key));
		itsModelIsModified = true;
	}

	private IBuildEntry getBuildEntry(final String key) {
		IBuild build = getProjectInfo().getBuild();
		return build.getEntry(key);
	}

	public boolean existsBuildEntry(final String key) {
		return getBuildEntry(key) != null;
	}

	public boolean isBuildModelEditable() {
		IBuildModel buildModel = getProjectInfo().getBuildModel();
		return buildModel instanceof IEditableModel;
	}

}
