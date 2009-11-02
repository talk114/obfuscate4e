package de.partmaster.obfuscate4e;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.ExportPackageDescription;
import org.eclipse.pde.core.build.IBuild;
import org.eclipse.pde.core.build.IBuildModel;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.core.build.WorkspaceBuildModel;

public class ProjectInfo {
    private final IFile itsManifest;
	private IBuildModel itsBuildModel;

	public ProjectInfo(IFile manifest) {
        itsManifest = manifest;
    }
    
    public IFile getManifest() {
        return itsManifest;
    }

    public IProject getProject() {
        return itsManifest.getProject();
    }

    public IPluginModelBase getPluginModel() {
        IProject project = getProject();
		return PDECore.getDefault().getModelManager().findModel(project);
    }

    public List getExportEntries() {
        IPluginModelBase activeModel = getPluginModel();
        if (activeModel == null) {
            return Collections.EMPTY_LIST;
        }
        BundleDescription bundleDescription = activeModel.getBundleDescription();
        ExportPackageDescription[] selectedExports = bundleDescription.getExportPackages();
        ArrayList resultList = new ArrayList(selectedExports.length);
        for (int i = 0; i < selectedExports.length; i++) {
            resultList.add(selectedExports[i].getName());
        }
        return resultList;
    }

	public IFile getFile(String fileName) {
		IFile file = getProject().getFile(fileName);
		return file;
	}


    public IBuildModel getBuildModel() {
        if (itsBuildModel == null) {
            IPluginModelBase pluginModel = getPluginModel();
            itsBuildModel = createBuildModel(pluginModel);
        }
        return itsBuildModel;
    }

    /**
     * see org.eclipse.pde.internal.core.ClasspathUtilCore.getBuild
     */
    protected IBuildModel createBuildModel(IPluginModelBase model) {
        IProject project = model.getUnderlyingResource().getProject();
        IFile buildFile = project.getFile("build.properties"); //$NON-NLS-1$
        if (buildFile.exists()) {
            WorkspaceBuildModel buildModel = new WorkspaceBuildModel(buildFile);
            buildModel.load();
            return buildModel;
        }
        return null;
    }

    public IBuild getBuild() {
        IBuildModel buildModel = getBuildModel();
        IBuild build = buildModel.getBuild();
        return build;
    }

}
