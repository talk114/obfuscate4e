package org.obfuscate4e;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.ExportPackageDescription;
import org.eclipse.pde.core.plugin.IPluginAttribute;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginObject;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.core.ischema.IMetaAttribute;
import org.eclipse.pde.internal.core.ischema.ISchema;
import org.eclipse.pde.internal.core.ischema.ISchemaAttribute;
import org.eclipse.pde.internal.core.ischema.ISchemaElement;

/**
 * This class is the O4e representation of an eclipse PDE project. The central
 * handle is the project's MANIFEST.MF file.
 * 
 */
public class ProjectInfo {
	private IPluginModelBase itsPluginModel;
	private List itsExportedPackages;
	private List itsJavaAttributeValuesList;
	private final IProject itsProject;
	private BuildProperties itsBuildProperties;

	public ProjectInfo(final IProject project) throws FileNotFoundException {
		Assert.isNotNull(project);
		ProjectHelper.findProjectManifest(project);
		itsProject = project;
	}

	public ProjectInfo(ICompilationUnit unit) throws FileNotFoundException {
		this(ProjectHelper.findProject(unit));
	}

	/**
	 * @return the belonging Project
	 */
	public IProject getProject() {
		return itsProject;
	}

	IPluginModelBase getPluginModel() {
		if (itsPluginModel == null) {
			itsPluginModel = PDECore.getDefault().getModelManager().findModel(
					itsProject);
		}
		return itsPluginModel;
	}

	public BuildProperties getBuildProperties() {
		if (itsBuildProperties == null) {
			itsBuildProperties = new BuildProperties(this);
		}
		return itsBuildProperties;
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
}
