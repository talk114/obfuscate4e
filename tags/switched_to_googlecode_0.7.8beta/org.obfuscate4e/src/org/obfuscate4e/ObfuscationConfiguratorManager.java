package org.obfuscate4e;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.obfuscate4e.builder.Obfuscate4eConfigurationBuilder;
import org.obfuscate4e.builder.Obfuscate4eConfigurationNature;
import org.obfuscate4e.preferences.PreferenceConstants;
import org.obfuscate4e.BuildProperties;

/**
 * This class is the enabler for the Obfuscate4e configurator extensions. It
 * provides the methods to get all registered configurators as well as setting
 * the active one.
 * 
 * @author fwo
 * 
 */
public class ObfuscationConfiguratorManager {

	private static final String CONFIGURATOR_ID = "org.obfuscate4e.ObfuscationConfigurator";
	private static final String NL = System.getProperty("line.separator");

	public static final String ACTIVE_CONFIGURATOR_PROPERTY_KEY = "obfuscate4e.activeConfiguratorID";
	public static final String ADD_BUILDER_DIALOG_TITLE = "Add the builder";
	public static final String ERROR_MESSAGE_TITLE = "Error while adding the builder";


	/**
	 * Fills a List with all registered configurators that implement the
	 * extension interface
	 * 
	 * @see IObfuscationConfigurator
	 * @param configList
	 *            the List to be filled, elements implement
	 *            {@link IObfuscationConfigurator}
	 * @return <b>true</b> if no error occurred, "good fences" strategy
	 */
	public static boolean fetchAllObfuscationConfigurators(final List configList) {
		try {
			final IConfigurationElement[] configurators = Platform
					.getExtensionRegistry().getConfigurationElementsFor(
							CONFIGURATOR_ID);

			for (int i = 0; i < configurators.length; i++) {

				IConfigurationElement e = configurators[i];

				final Object o = e.createExecutableExtension("class");

				if (o instanceof IObfuscationConfigurator) {
					configList.add(o);
				}
			}
		} catch (final Exception ex) {
			return false;
		}
		return true;
	}

	/**
	 * Fetches the currently active configurator from the project properties. If
	 * no property was set yet, the first found configurator will be returned
	 * and the project property will get set.
	 * 
	 * @param project
	 *            the PDE project in which the active configurator property is
	 *            stored / is to be stored
	 * @return the active configurator
	 * @throws GeneralObfuscate4eException
	 */
	public static IObfuscationConfigurator fetchActiveObfuscationConfigurator(
				IProject project) throws GeneralObfuscate4eException, FileNotFoundException {
		ProjectInfo projectInfo = null;

		projectInfo = new ProjectInfo(project);
		
		IObfuscationConfigurator configurator = null;

		configurator = fetchStoredConfigurator(projectInfo);
		if (configurator != null) {
			return configurator;
		}

		configurator = fetchPreferredConfigurator();
		if (configurator != null) {
			if (storeActiveConfigurator(configurator, project)) {
				return configurator;
			} else {
				throw new GeneralObfuscate4eException(
						"the project's active configurator is not available");
			}
		}

		ArrayList configList = new ArrayList();
		fetchAllObfuscationConfigurators(configList);
		if (configList.size() == 0) {
			throw new GeneralObfuscate4eException(
					GeneralObfuscate4eException.ERROR_NO_CONFIGURATORS);
		}
		if (configList.size() == 1) {
			configurator = (IObfuscationConfigurator) configList.get(0);
			storeActiveConfigurator(configurator, project);
			IPreferenceStore store = Activator.getDefault()
					.getPreferenceStore();
			store.setValue(
					PreferenceConstants.PREFERRED_ACTIVE_CONFIGURATOR_ID,
					configurator.getID());
			return configurator;
		}

		throw new GeneralObfuscate4eException(
				GeneralObfuscate4eException.ERROR_NO_PREFERENCE);

	}

	private static IObfuscationConfigurator fetchPreferredConfigurator() {
		IObfuscationConfigurator configurator;
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String configuratorId = store
				.getString(PreferenceConstants.PREFERRED_ACTIVE_CONFIGURATOR_ID);
		configurator = fetchConfiguratorById(configuratorId);
		if (configurator != null) {
			return configurator;
		}
		return configurator;
	}

	private static IObfuscationConfigurator fetchStoredConfigurator(
			ProjectInfo projectInfo) {
		IObfuscationConfigurator configurator;
		BuildProperties buildProperties = new BuildProperties(projectInfo);

		if (buildProperties.hasBuildEntry(ACTIVE_CONFIGURATOR_PROPERTY_KEY)) {

			configurator = fetchConfiguratorById(buildProperties
					.getProperties(ACTIVE_CONFIGURATOR_PROPERTY_KEY)[0]);
			if (configurator != null) {
				return configurator;
			}
		}
		return null;
	}

	/**
	 * stores the given configurator as an entry in the 'build.properties' of
	 * the project
	 * 
	 * @param configurator
	 *            the configurator to be set as the active one
	 * @param project
	 *            the project for which the configurator is to set
	 */
	public static boolean storeActiveConfigurator(
			IObfuscationConfigurator configurator, IProject project) {

		if (configurator == null || project == null) {
			return false;
		}

		ProjectInfo projectInfo = null;

		try {

			projectInfo = new ProjectInfo(project);
			BuildProperties buildProperties = new BuildProperties(projectInfo);
			return buildProperties.addBuildEntry(ACTIVE_CONFIGURATOR_PROPERTY_KEY,
					configurator.getID());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * returns a registered configurator for the given identifier.
	 * 
	 * @param configuratorId
	 * @return the configurator
	 */
	public static IObfuscationConfigurator fetchConfiguratorById(
			String configuratorId) {
		List configurators = new ArrayList();
		if (!fetchAllObfuscationConfigurators(configurators)) {
			return null;
		}
		for (Iterator i = configurators.iterator(); i.hasNext();) {
			IObfuscationConfigurator configurator = (IObfuscationConfigurator) i
					.next();
			if (configurator.getID().equals(configuratorId)) {
				return configurator;
			}
		}
		return null;
	}

	/**
	 * checks whether the given project has the Obfuscate4eConfigurationNature,
	 * which is needed for the configuration builder
	 * 
	 * @see Obfuscate4eConfigurationNature
	 * @see Obfuscate4eConfigurationBuilder
	 * @param project
	 *            the project to check
	 * @return <b>true</b> if the project has the nature
	 */
	public static boolean hasObfuscate4eConfigurationNature(IProject project) {
		IProjectDescription description;
		try {
			description = project.getDescription();
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}
		String[] natures = description.getNatureIds();

		for (int i = 0; i < natures.length; ++i) {
			if (Obfuscate4eConfigurationNature.NATURE_ID.equals(natures[i])) {
				return true;
			}
		}
		return false;
	}
	static class MessageDialogWithPreference extends MessageDialog {
		private boolean rememberMyDecision = false;

		public MessageDialogWithPreference(Shell parentShell,
				String dialogTitle, Image dialogTitleImage,
				String dialogMessage, int dialogImageType,
				String[] dialogButtonLabels, int defaultIndex) {
			super(parentShell, dialogTitle, dialogTitleImage,
					dialogMessage, dialogImageType, dialogButtonLabels,
					defaultIndex);
		}

		protected Control createCustomArea(Composite parent) {
			Button rememberButton = new Button(parent, SWT.CHECK);
			rememberButton.setText("Do not ask again for adding the builder in the future.");
			rememberButton.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
					rememberMyDecision = ((Button) e.widget).getSelection();
				}

				public void widgetSelected(SelectionEvent e) {
					rememberMyDecision = ((Button) e.widget).getSelection();
				}
			});
			return rememberButton;
		}
	}
	;

	public static void addObfuscate4eNature(IProject project) {
		if (hasObfuscate4eConfigurationNature(project)) {
			return;
		}
		Shell activeShell = PlatformUI.getWorkbench().getDisplay()
				.getActiveShell();
		String message = "";
		
		try {
			message = "Do you want to add the builder?" + NL
					+ "The currently active Obfuscator is "
					+ fetchActiveObfuscationConfigurator(project).getName();
		} catch (GeneralObfuscate4eException e1) {
			MessageDialog.openError(Display.getDefault()
					.getActiveShell(), ERROR_MESSAGE_TITLE, e1
					.getMessage());

			return;
		} catch (FileNotFoundException e) {
			// project has no Manifest file
			return;
		}

		MessageDialogWithPreference dialog = new MessageDialogWithPreference(activeShell,
				ADD_BUILDER_DIALOG_TITLE, null, message,
				MessageDialog.QUESTION,
				new String[] { IDialogConstants.OK_LABEL,
						IDialogConstants.CANCEL_LABEL }, 0);

		int dialogReturnCode = dialog.open();

		if(dialog.rememberMyDecision){
			Activator.getDefault().getPreferenceStore().setValue(
					PreferenceConstants.ENABLE_BUILDER_FOR_NEW_PROJECTS,false);
		}

		if (dialogReturnCode != MessageDialog.OK) {
			return;
		}

		try {
			IProjectDescription description = project.getDescription();
			String[] natures = description.getNatureIds();
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[natures.length] = Obfuscate4eConfigurationNature.NATURE_ID;
			description.setNatureIds(newNatures);
			project.setDescription(description, null);
		} catch (CoreException e) {
		}

	}
}
