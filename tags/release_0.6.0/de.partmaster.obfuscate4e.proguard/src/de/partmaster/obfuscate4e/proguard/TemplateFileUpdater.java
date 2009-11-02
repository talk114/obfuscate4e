package de.partmaster.obfuscate4e.proguard;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.pde.core.plugin.IExtensions;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginObject;
import org.eclipse.pde.core.plugin.IPluginParent;
import org.eclipse.pde.core.plugin.ISharedExtensionsModel;
import org.eclipse.pde.internal.core.ibundle.IBundlePluginModelBase;
import org.eclipse.pde.internal.core.text.IDocumentNode;

import de.partmaster.obfuscate4e.ProjectFileUpdater;
import de.partmaster.obfuscate4e.generator.IBuildGenerator;

public class TemplateFileUpdater extends ProjectFileUpdater {

	public TemplateFileUpdater(IBuildGenerator generator, IFile manifest) {
		super(generator, manifest, "proguard.cfg.tpl");
	}

	protected List getExportEntries() {
		IBundlePluginModelBase pluginModel = (IBundlePluginModelBase) getPluginModel();
		// TODO: pluginModel can be "null", if the project does not
		// define any extensions
		if (pluginModel != null) {
			ISharedExtensionsModel extensionsModel = pluginModel
					.getExtensionsModel();
			if (extensionsModel != null) {
				try {
					extensionsModel.load();
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				IExtensions extensions2 = extensionsModel.getExtensions(true);
				// IExtensions extensions2 = pluginModel.getExtensions(true);
				IPluginExtension[] extensions = extensions2.getExtensions();
				for (int i = 0; i < extensions.length; i++) {
					printAttributes(extensions[i]);
				}
			}
		}
		return super.getExportEntries();
	}

	private void printAttributes(IPluginObject extension) {
		System.out.println(extension.getClass().getName());
		if (!(extension instanceof IPluginParent)) {
			return;
		}
		IPluginParent parent = (IPluginParent) extension;
		System.out.println(parent.getName());
		System.out.println(parent.getChildCount());
		IPluginObject[] children = parent.getChildren();
		for (int i = 0; i < children.length; i++) {
			printAttributes(children[i]);
		}
	}
}
