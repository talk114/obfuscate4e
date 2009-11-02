package de.partmaster.obfuscate4e.builder;

import java.io.FileNotFoundException;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import de.partmaster.obfuscate4e.BaseBuildCallbackHandler;
import de.partmaster.obfuscate4e.IObfuscationConfigurator;
import de.partmaster.obfuscate4e.ObfuscationConfiguratorManager;
import de.partmaster.obfuscate4e.ProjectInfo;
import de.partmaster.obfuscate4e.updater.IUpdater;

/**
 * this class represents the Obfuscate4e configuration builder.<br>
 * Projects need to be of the Obfuscate4e configuration nature to have the
 * builder activated.<br>
 * This builder listens for changes in the plugin.xml and MANIFEST.MF and starts
 * an automatic configuration creation process.
 * 
 * @see Obfuscate4eConfigurationNature
 * @author fwo
 * 
 */
public class Obfuscate4eConfigurationBuilder extends IncrementalProjectBuilder {

	class Obfuscate4eDeltaVisitor implements IResourceDeltaVisitor {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse
		 * .core.resources.IResourceDelta)
		 */
		public boolean visit(IResourceDelta delta) throws CoreException {

			IResource resource = delta.getResource();

			String name = resource.getName();

			if (name.equals("MANIFEST.MF")) {
				itsManifestChanged = true;
			}

			if (name.equals("plugin.xml")) {
				itsPluginXmlChanged = true;
			}

			// return true to continue visiting children.
			return true;
		}
	}

	class Obfuscate4eResourceVisitor implements IResourceVisitor {

		public boolean visit(IResource resource) {
			String name = resource.getName();
			if (name.equals("MANIFEST.MF")) {
				itsManifestChanged = true;
			}
			if (name.equals("plugin.xml")) {
				itsPluginXmlChanged = true;
			}
			// return true to continue visiting children.
			return true;
		}
	}

	public static final String BUILDER_ID = "de.partmaster.obfuscate4e.configurationBuilder";

	private boolean itsManifestChanged;

	private boolean itsPluginXmlChanged;


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int,
	 * java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {

		initBuild();

		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}

		finalizeBuild();

		return null;
	}


	private void initBuild() {
		itsManifestChanged = false;
		itsPluginXmlChanged = false;
	}

	/**
	 * this method is to be called after the visitor has run over the files.<br>
	 * in the case that either the MANIFEST.MF or the plugin.xml have changed,
	 * it starts the configuration process
	 */
	private void finalizeBuild() {
		if (itsManifestChanged || itsPluginXmlChanged) {

			IFile manifestFile;
			try {
				manifestFile = ProjectInfo.findProjectManifest(getProject());
			} catch (FileNotFoundException e) {
				return;
			}

			IObfuscationConfigurator configurator = ObfuscationConfiguratorManager
					.getActiveObfuscationConfigurator(getProject());

			if (configurator != null) {

				BaseBuildCallbackHandler buildUpdateHandler = new BaseBuildCallbackHandler();

				IUpdater customConfigurationFileUpdater = configurator.getConfigFileUpdater();
				IUpdater customBuildScriptUpdater = configurator.getBuildScriptUpdater();
				
				buildUpdateHandler.addUpdater(customBuildScriptUpdater);
				buildUpdateHandler.addUpdater(customConfigurationFileUpdater);
				
				buildUpdateHandler.runUpdaters(manifestFile);

			}
		}
	}

	/**
	 * is to be called when the builder was applied to the project for the first time
	 * 
	 * @param monitor
	 * @throws CoreException
	 */
	private void fullBuild(final IProgressMonitor monitor)
			throws CoreException {
		try {
			getProject().accept(new Obfuscate4eResourceVisitor());
		} catch (CoreException e) {
		}
	}

	/**
	 * is to be called when one or more files in the project have been changed
	 *  
	 * @param delta
	 * @param monitor
	 * @throws CoreException
	 */
	private void incrementalBuild(IResourceDelta delta,
			IProgressMonitor monitor) throws CoreException {
		delta.accept(new Obfuscate4eDeltaVisitor());
	}

}
