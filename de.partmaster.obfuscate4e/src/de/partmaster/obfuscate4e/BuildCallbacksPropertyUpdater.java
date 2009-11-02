// $Id:$

package de.partmaster.obfuscate4e;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.pde.core.IEditableModel;
import org.eclipse.pde.core.build.IBuild;
import org.eclipse.pde.core.build.IBuildEntry;
import org.eclipse.pde.core.build.IBuildModel;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.core.build.WorkspaceBuildModel;

public class BuildCallbacksPropertyUpdater extends AbstractUpdater {

    private static final String KEY_CUSTOM_BUILD_CALLBACKS = "customBuildCallbacks";
    private IBuildModel itsBuildModel;

    public BuildCallbacksPropertyUpdater(IFile manifest) {
        super(manifest);
    }

    public void run(IProgressMonitor monitor) throws CoreException {
        if (existsKey()) {
            remove();
        }
        write();
        save();
    }

    public void write() throws CoreException {
        IBuildModel buildModel = getBuildModel();
        getBuild().add(makeCallBacksEntry(buildModel));
        save();
    }

    public void save() {
        ((IEditableModel) getBuildModel()).save();
    }

    public void remove() throws CoreException {
        IBuild build = getBuild();
        build.remove(getBuildEntry());
    }

    private IBuild getBuild() {
        IBuildModel buildModel = getBuildModel();
        IBuild build = buildModel.getBuild();
        return build;
    }

    private IBuildEntry getBuildEntry() {
        IBuild build = getBuild();
        return build.getEntry(KEY_CUSTOM_BUILD_CALLBACKS);
    }

    public boolean existsKey() {
        return getBuildEntry() != null;
    }

    private IBuildEntry makeCallBacksEntry(IBuildModel buildModel) throws CoreException {
        IBuildEntry customCallbacksBuildEntry = buildModel.getFactory().createEntry(KEY_CUSTOM_BUILD_CALLBACKS);
        customCallbacksBuildEntry.addToken(FILE_CUSTOM_BUILD_CALLBACKS);
        return customCallbacksBuildEntry;
    }

    public boolean isEditable() {
        IBuildModel buildModel = getBuildModel();
        return buildModel instanceof IEditableModel;
    }

    private IBuildModel getBuildModel() {
        if (itsBuildModel == null) {
            IProject project = getProject();
            IPluginModelBase pluginModel = getPluginModel(project);
            itsBuildModel = createBuildModel(pluginModel);
        }
        return itsBuildModel;
    }

    public String getKey() {
        return KEY_CUSTOM_BUILD_CALLBACKS;
    }

    /**
     * see org.eclipse.pde.internal.core.ClasspathUtilCore.getBuild
     */
    private IBuildModel createBuildModel(IPluginModelBase model) {
        IProject project = model.getUnderlyingResource().getProject();
        IFile buildFile = project.getFile("build.properties"); //$NON-NLS-1$
        if (buildFile.exists()) {
            WorkspaceBuildModel buildModel = new WorkspaceBuildModel(buildFile);
            buildModel.load();
            return buildModel;
        }
        return null;
    }

}
