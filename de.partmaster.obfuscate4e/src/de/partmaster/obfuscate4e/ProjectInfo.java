package de.partmaster.obfuscate4e;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.ExportPackageDescription;
import org.eclipse.pde.core.build.IBuild;
import org.eclipse.pde.core.build.IBuildModel;
import org.eclipse.pde.core.plugin.IPluginAttribute;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginObject;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.core.build.WorkspaceBuildModel;
import org.eclipse.pde.internal.core.ischema.IMetaAttribute;
import org.eclipse.pde.internal.core.ischema.ISchema;
import org.eclipse.pde.internal.core.ischema.ISchemaAttribute;
import org.eclipse.pde.internal.core.ischema.ISchemaElement;

public class ProjectInfo {
	private final IFile itsManifest;
	private IBuildModel itsBuildModel;
	private IPluginModelBase itsPluginModel;
	private List itsExportedPackages;
	private List itsJavaAttributeValuesList;

	/**
	 * @param manifest
	 */
	public ProjectInfo(final IFile manifest) throws FileNotFoundException {
		Assert.isNotNull(manifest);
		if (!isManifest(manifest)) {
			throw new FileNotFoundException("no valid manifest file:"
					+ manifest.getFullPath());
		}
		itsManifest = manifest;
	}

	ProjectInfo(ICompilationUnit unit) throws FileNotFoundException {
		IFile manifest = findProjectManifest(unit);
		itsManifest = manifest;
	}

	// FIXME: Write Tests!
	private boolean isManifest(IFile file) {
		IFile projectManifest;
		try {
			projectManifest = findProjectManifest(file);
		} catch (FileNotFoundException e) {
			return false;
		}
		return projectManifest.getFullPath().toString().equals(
				file.getFullPath().toString());
	}

	/**
	 * returns the MANIFEST.MF file
	 * 
	 * @return the MANIFEST.MF file
	 */
	public IFile getManifest() {
		return itsManifest;
	}

	/**
	 * returns the belonging Project
	 * 
	 * @return the belonging Project
	 */
	public IProject getProject() {
		return itsManifest.getProject();
	}

	private IPluginModelBase getPluginModel() {
		if (itsPluginModel != null) {
			return itsPluginModel;
		}
		IProject project = getProject();
		itsPluginModel = PDECore.getDefault().getModelManager().findModel(
				project);
		return itsPluginModel;
	}

	/**
	 * returns the packages that are to be excluded from the obfuscation
	 * 
	 * @return the packages that are to be excluded from the obfuscation
	 */
	public List getExportedPackages() {
		if (itsExportedPackages != null) {
			return itsExportedPackages;
		}
		IPluginModelBase activeModel = getPluginModel();
		if (activeModel == null) {
			return Collections.EMPTY_LIST;
		}
		BundleDescription bundleDescription = activeModel
				.getBundleDescription();
		ExportPackageDescription[] selectedExports = bundleDescription
				.getExportPackages();
		ArrayList resultList = new ArrayList(selectedExports.length);
		for (int i = 0; i < selectedExports.length; i++) {
			resultList.add(selectedExports[i].getName());
		}
		itsExportedPackages = Collections.unmodifiableList(resultList);
		return itsExportedPackages;
	}

	/**
	 * returns a file accordingly to its fileName
	 * 
	 * @param fileName
	 *            the fileName
	 * @return the file
	 */
	public IFile getFile(final String fileName) {
		IFile file = getProject().getFile(fileName);
		return file;
	}

	/**
	 * returns the Eclipse BuildModel
	 * 
	 * @return the Eclipse BuildModel
	 */
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

	/*
	 * 
	 */
	public IBuild getBuild() {
		IBuildModel buildModel = getBuildModel();
		if (buildModel == null) {
			return null;
		}
		return buildModel.getBuild();
	}

	/**
	 * Finds all classes that are declared in extension definitions (in
	 * attributes of java kind).
	 * 
	 * @return list of names of found java classes (as string)
	 */
	public List/* <String> */getClassesInExtensions() {
		if (itsJavaAttributeValuesList != null) {
			return itsJavaAttributeValuesList;
		}
		ArrayList resultList = new ArrayList();
		IPluginExtension[] extensions = getPluginModel().getExtensions(true)
				.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			IPluginExtension extension = extensions[i];
			ISchema schema = PDECore.getDefault().getSchemaRegistry()
					.getSchema(extension.getPoint());
			if (schema == null) {
				continue;
			}
			IPluginObject[] children = extension.getChildren();
			for (int index = 0; index < children.length; index++) {
				IPluginElement pluginElement = (IPluginElement) children[index];
				addJavaAttributeValues(resultList, schema, pluginElement);
			}
		}
		itsJavaAttributeValuesList = Collections.unmodifiableList(resultList);
		return itsJavaAttributeValuesList;
	}

	/**
	 * Checks the schema definition for given pluginElement's attributes for
	 * java kind. If it finds a java kind attribute, then it adds the
	 * attribute's value to the given resultList.
	 */
	private void addJavaAttributeValues(final List resultList,
			final ISchema schema, final IPluginElement pluginElement) {
		String elementName = pluginElement.getName();
		ISchemaElement schemaElement = schema.findElement(elementName);
		if (schemaElement == null) {
			return;
		}
		IPluginAttribute[] pluginAttributes = pluginElement.getAttributes();
		for (int attributeIndex = 0; attributeIndex < pluginAttributes.length; attributeIndex++) {
			IPluginAttribute pluginAttribute = pluginAttributes[attributeIndex];
			ISchemaAttribute schemaAttribute = schemaElement
					.getAttribute(pluginAttribute.getName());
			if (schemaAttribute == null) {
				continue;
			}
			if (IMetaAttribute.JAVA == schemaAttribute.getKind()) {
				resultList.add(pluginAttribute.getValue());
			}
		}
		// recurse through child plugin elements
		for (int i = 0; i < pluginElement.getChildCount(); i++) {
			IPluginObject[] children = pluginElement.getChildren();
			addJavaAttributeValues(resultList, schema,
					(IPluginElement) children[i]);
		}

	}

	public static IFile findProjectManifest(IProject project)
			throws FileNotFoundException {
		Assert.isNotNull(project);
		IFolder meta_inf_folder = project.getFolder("META-INF");
		if (!meta_inf_folder.exists()) {
			throw new FileNotFoundException("Could not find project manifest");
		}
		IFile manifest = meta_inf_folder.getFile("MANIFEST.MF");
		if (!manifest.exists()) {
			throw new FileNotFoundException("Could not find project manifest");
		}
		return manifest;
	}

	public static IFile findProjectManifest(ICompilationUnit unit)
			throws FileNotFoundException {
		Assert.isNotNull(unit);
		return findProjectManifest(findProject(unit));
	}

	public static IFile findProjectManifest(IFile file)
			throws FileNotFoundException {
		Assert.isNotNull(file);
		return findProjectManifest(file.getProject());
	}

	public static IProject findProject(ICompilationUnit unit) {
		return unit.getJavaProject().getProject();
	}
}
