// $Id:$

package de.partmaster.obfuscate4e;

import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.ExportPackageDescription;
import org.eclipse.pde.core.plugin.IPluginModelBase;

import de.partmaster.obfuscate4e.generator.IBuildGenerator;

public class ProjectFileUpdater extends AbstractUpdater {

    private final IBuildGenerator itsGenerator;
	private final String itsFilename;

    public ProjectFileUpdater(IBuildGenerator generator, IFile manifest, String filename) {
        super(manifest);
        itsGenerator = generator;
		this.itsFilename = filename;
    }

    public void run(IProgressMonitor monitor) throws CoreException {
        write(monitor);
    }
       
    public void write(IProgressMonitor monitor) throws CoreException {
        IFile file = getFile();
        List exportEntries = getExportEntries();
        StringBufferInputStream inputStream = new StringBufferInputStream(itsGenerator.generate(exportEntries));
        file.create(inputStream, false, monitor);
    }


    public IFile getFile() {
        IFile file = getProject().getFile(itsFilename);
        return file;
    }

    public boolean exists() {
        return getFile().exists();
    }
    
    public void remove(IProgressMonitor monitor) throws CoreException {
        getFile().delete(true, monitor);
    }
    
    protected List getExportEntries() {
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

}
