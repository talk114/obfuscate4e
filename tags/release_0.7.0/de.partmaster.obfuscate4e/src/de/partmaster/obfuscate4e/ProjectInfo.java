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
import org.eclipse.pde.core.plugin.IPluginAttribute;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginObject;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.core.build.WorkspaceBuildModel;
import org.eclipse.pde.internal.core.ischema.ISchema;
import org.eclipse.pde.internal.core.ischema.ISchemaAttribute;
import org.eclipse.pde.internal.core.ischema.ISchemaElement;

public class ProjectInfo {
	private final IFile itsManifest;
	private IBuildModel itsBuildModel;
	private IPluginModelBase itsPluginModel;
	private List itsExportedPackages;
	private List itsJavaAttributeValuesList;

	public ProjectInfo(IFile manifest) {
		itsManifest = manifest;
	}

	public IFile getManifest() {
		return itsManifest;
	}

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

	public IBuild getBuild() {
		IBuildModel buildModel = getBuildModel();
		IBuild build = buildModel.getBuild();
		return build;
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
	private void addJavaAttributeValues(List resultList, ISchema schema,
			IPluginElement pluginElement) {
		ISchemaElement schemaElement = schema.findElement(pluginElement
				.getName());
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
			if (ISchemaAttribute.JAVA == schemaAttribute.getKind()) {
				resultList.add(pluginAttribute.getValue());
			}
		}
		//recurse through child plugin elements
		for (int i = 0; i < pluginElement.getChildCount(); i++) {
			IPluginObject[] children = pluginElement.getChildren();
			addJavaAttributeValues(resultList, schema, (IPluginElement) children[i]);
		}
		
	}
}
