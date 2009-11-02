package org.obfuscate4e.properties;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.dialogs.PropertyPage;
import org.obfuscate4e.Activator;
import org.obfuscate4e.GeneralObfuscate4eException;
import org.obfuscate4e.IObfuscationConfigurator;
import org.obfuscate4e.ObfuscationConfiguratorManager;
import org.obfuscate4e.preferences.PreferenceConstants;

/**
 * This class represents the Eclipse Property page for Obfuscate4e.<br>
 * It provides the options to select the currently active configurator from the
 * list of all registered configurators and sets the project property.
 * 
 * @author fwo
 * 
 */
public class Obfuscate4ePropertyPage extends PropertyPage {

	private class ConfiguratorComboSelectionListener implements
			SelectionListener {

		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}

		public void widgetSelected(SelectionEvent e) {
			itsActiveConfiguratorIndex = ((Combo) e.getSource())
					.getSelectionIndex();
		}
	}

	private class AddNatureButtonListener implements Listener {
		public void handleEvent(Event event) {
			ObfuscationConfiguratorManager
					.addObfuscate4eNature((IProject) getElement());
			if (ObfuscationConfiguratorManager
					.hasObfuscate4eConfigurationNature((IProject) getElement())) {
				itsAddNatureComposite.setVisible(false);
			}
		}
	}

	public static final String NL = System.getProperty("line.separator");
	private static final String ACTIVE_CONFIGURATOR = "Active Obfuscator: ";
	private static final String PATH_TITLE = "Project: ";
	private static final String ERROR_FETCHING_LIST = "Sorry, an error occured while fetching the configurators list.";
	private static final String ADD_BUILDER_DESCRIPTION = "To automatically create a configuration"
			+ NL
			+ "on changes of the plugin.xml or MANIFEST.MF,"
			+ NL
			+ "add the builder.";
	private static final String ADD_BUILDER_BUTTON_TEXT = "Add the builder";

	private int itsActiveConfiguratorIndex;
	private Combo itsConfiguratorsCombo;
	private Composite itsAddNatureComposite;
	private final ArrayList itsConfiguratorsArray = new ArrayList();

	/**
	 * Constructor for Obfuscate4e PropertyPage.
	 */
	public Obfuscate4ePropertyPage() {
		super();
	}

	private void addFirstSection(Composite parent) {
		Composite composite = createDefaultComposite(parent);

		Label pathLabel = new Label(composite, SWT.NONE);
		pathLabel.setText(PATH_TITLE + ((IProject) getElement()).getName());

	}

	private void addSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		separator.setLayoutData(gridData);
	}

	private void addConfigSelectionSection(Composite parent) {
		Composite composite = createDefaultComposite(parent);

		Label selectAConfiguratorLabel = new Label(composite, SWT.NULL);
		selectAConfiguratorLabel.setText(ACTIVE_CONFIGURATOR);
		GridData gd = new GridData();
		gd.widthHint = convertWidthInCharsToPixels(ACTIVE_CONFIGURATOR.length());
		selectAConfiguratorLabel.setLayoutData(gd);

		Control selectionElement = createConfiguratorSelectionComboBox(composite);
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		selectionElement.setLayoutData(gd);

	}

	private void addNatureActivationSection(Composite parent) {
		if (ObfuscationConfiguratorManager
				.hasObfuscate4eConfigurationNature((IProject) getElement())) {
			return;
		}
		if (itsConfiguratorsCombo != null) {
			itsAddNatureComposite = createDefaultComposite(parent);

			Label selectAConfiguratorLabel = new Label(itsAddNatureComposite,
					SWT.NULL);
			selectAConfiguratorLabel.setText(ADD_BUILDER_DESCRIPTION);
			GridData gd = new GridData();
			// gd.widthHint =
			// convertWidthInCharsToPixels(ADD_BUILDER_DESCRIPTION .length());
			selectAConfiguratorLabel.setLayoutData(gd);

			Control addNatureButton = createAddNatureButton(itsAddNatureComposite);
			gd = new GridData();
			gd.grabExcessHorizontalSpace = true;
			gd.horizontalAlignment = GridData.FILL;
			addNatureButton.setLayoutData(gd);
		}
	}

	private Control createAddNatureButton(Composite composite) {
		Button myButton = new Button(composite, SWT.PUSH);
		myButton.setText(ADD_BUILDER_BUTTON_TEXT);
		myButton.addListener(SWT.Selection, new AddNatureButtonListener());

		return myButton;
	}

	private Control createConfiguratorSelectionComboBox(Composite parent) {

		if (!ObfuscationConfiguratorManager
				.fetchAllObfuscationConfigurators(itsConfiguratorsArray)) {
			Label errorLabel = new Label(parent, SWT.NULL);
			errorLabel.setText(ERROR_FETCHING_LIST);
			return errorLabel;
		}

		if (itsConfiguratorsArray.size() < 1) {
			Label errorLabel = new Label(parent, SWT.NULL);
			errorLabel
					.setText(GeneralObfuscate4eException.ERROR_NO_CONFIGURATORS);
			return errorLabel;
		}

		// get the actually active configurator by the static extension handler
		IObfuscationConfigurator activeConfigurator = null;
		try {
			activeConfigurator = ObfuscationConfiguratorManager
					.fetchActiveObfuscationConfigurator((IProject) getElement());
		} catch (GeneralObfuscate4eException e) {
			Label errorLabel = new Label(parent, SWT.NULL);
			errorLabel.setText(e.getMessage());
			return errorLabel;
		} catch (FileNotFoundException e) {
			// project has no manifest file
			Label errorLabel = new Label(parent, SWT.NULL);
			errorLabel.setText(e.getMessage());
			return errorLabel;
		}

		itsConfiguratorsCombo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);

		// fill Combo with all known configurators
		for (int i = 0; i < itsConfiguratorsArray.size(); i++) {
			IObfuscationConfigurator configurator = (IObfuscationConfigurator) itsConfiguratorsArray
					.get(i);
			String name = configurator.getName();
			if (name == null) {
				name = configurator.getID();
			}
			itsConfiguratorsCombo.add(name);
		}

		doSelectActiveConfiguratorInComboById(activeConfigurator.getID());

		itsConfiguratorsCombo
				.addSelectionListener(new ConfiguratorComboSelectionListener());

		return itsConfiguratorsCombo;
	}

	private boolean doSelectActiveConfiguratorInComboById(String configuratorId) {

		if (itsConfiguratorsCombo == null) {
			return false;
		}

		int activeIndex = -1;

		for (int i = 0; i < itsConfiguratorsArray.size(); i++) {
			if (((IObfuscationConfigurator) itsConfiguratorsArray.get(i))
					.getID().equals(configuratorId)) {
				itsActiveConfiguratorIndex = i;
				activeIndex = i;
			}
		}
		itsConfiguratorsCombo.select(activeIndex);

		return activeIndex >= 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse
	 * .swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);

		addFirstSection(composite);
		addSeparator(composite);
		addConfigSelectionSection(composite);

		addNatureActivationSection(composite);
		return composite;
	}

	private Composite createDefaultComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		composite.setLayoutData(data);

		return composite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {
		if (itsConfiguratorsArray.size() > 0) {
			try {
				doSelectActiveConfiguratorInComboById(ObfuscationConfiguratorManager
						.fetchActiveObfuscationConfigurator(
								(IProject) getElement()).getID());
			} catch (GeneralObfuscate4eException e) {
			} catch (FileNotFoundException e) {
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	public boolean performOk() {
		if (itsConfiguratorsArray.size() > 0) {

			try {

				ObfuscationConfiguratorManager
						.fetchActiveObfuscationConfigurator((IProject) getElement());

				boolean success = ObfuscationConfiguratorManager
						.storeActiveConfigurator(
								(IObfuscationConfigurator) itsConfiguratorsArray
										.get(itsActiveConfiguratorIndex),
								(IProject) getElement());

				if (Activator.getDefault().getPreferenceStore().getBoolean(
						PreferenceConstants.ENABLE_BUILDER_FOR_NEW_PROJECTS)) {
					ObfuscationConfiguratorManager
							.addObfuscate4eNature((IProject) getElement());
				}
				return success;
			} catch (GeneralObfuscate4eException e) {
			} catch (FileNotFoundException e) {
			}

		}
		return true;

	}
}