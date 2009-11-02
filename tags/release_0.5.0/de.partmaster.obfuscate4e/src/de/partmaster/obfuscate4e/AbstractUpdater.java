// $Id:$
package de.partmaster.obfuscate4e;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.core.PDECore;

public abstract class AbstractUpdater implements IWorkspaceRunnable {

    protected static final String FILE_CUSTOM_BUILD_CALLBACKS = "customBuildCallbacks.xml";
    
    private final IFile itsManifest;

    public AbstractUpdater(IFile manifest) {
        itsManifest = manifest;
    }
    
    public IFile getManifest() {
        return itsManifest;
    }
    
    protected IPluginModelBase getPluginModel(IProject project) {
        return PDECore.getDefault().getModelManager().findModel(project);
    }

    public boolean isValidManifest(IProgressMonitor monitor) throws CoreException {
        // Force the build if autobuild is off
        IProject project = itsManifest.getProject();
        if (!project.getWorkspace().isAutoBuilding()) {
            String builderID = "org.eclipse.pde.ManifestBuilder"; // =PDE.MANIFEST_BUILDER_ID;
            project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, builderID, null, monitor);
        }
    
        return hasErrors(itsManifest);
    }

    public boolean hasErrors(IFile file) throws CoreException {
        IMarker[] markers = file.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ZERO);
        for (int i = 0; i < markers.length; i++) {
            Object att = markers[i].getAttribute(IMarker.SEVERITY);
            if (att != null && att instanceof Integer) {
                if (((Integer) att).intValue() == IMarker.SEVERITY_ERROR)
                    return true;
            }
        }
        return false;
    }

    public IProject getProject() {
        return itsManifest.getProject();
    }


}