package de.partmaster.obfuscate4e.properties;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

import de.partmaster.obfuscate4e.IObfuscationConfigurator;
import de.partmaster.obfuscate4e.ObfuscationConfiguratorManager;

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

	private static final String SELECT_A_CONFIG = "Select Obfuscator: ";
	public static final String ACTIVE_CONFIGURATOR_PROPERTY = "ACTIVE_CONFIGURATOR";
	private static final String PATH_TITLE = "Project:";
	private static final String ERROR_FETCHING_LIST = "Sorry, an error occured while fetching the configurators list.";
	private static final String ERROR_EMPTY_LIST = "Sorry, there are currently no obfuscators registered.";

	private int itsActiveConfiguratorIndex;
	private Combo itsConfiguratorsCombo;
	private final ArrayList itsConfiguratorsArray = new ArrayList();

	/**
	 * Constructor for Obfuscate4e PropertyPage.
	 */
	public Obfuscate4ePropertyPage() {
		super();
	}

	private void addFirstSection(Composite parent) {
		Composite composite = createDefaultComposite(parent);

		// Label for path field
		Label pathLabel = new Label(composite, SWT.NONE);
		pathLabel.setText(PATH_TITLE);

		// Path text field
		Text projectName = new Text(composite, SWT.WRAP | SWT.READ_ONLY);
		projectName.setText(((IProject) getElement()).getName());
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
		selectAConfiguratorLabel.setText(SELECT_A_CONFIG);
		GridData gd = new GridData();
		gd.widthHint = convertWidthInCharsToPixels(SELECT_A_CONFIG.length());
		selectAConfiguratorLabel.setLayoutData(gd);

		Control selectionElement = getConfiguratorSelectionComboBox(composite);
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		selectionElement.setLayoutData(gd);

	}

	private Control getConfiguratorSelectionComboBox(Composite parent) {

		if (!ObfuscationConfiguratorManager
				.getAllObfuscationConfigurators(itsConfiguratorsArray)) {
			Label errorLabel = new Label(parent, SWT.NULL);
			errorLabel.setText(ERROR_FETCHING_LIST);
			return errorLabel;
		}

		if (itsConfiguratorsArray.size() < 1) {
			Label errorLabel = new Label(parent, SWT.NULL);
			errorLabel.setText(ERROR_EMPTY_LIST);
			return errorLabel;
		}

		// get the actually active configurator by the static extension handler
		IObfuscationConfigurator activeConfigurator = ObfuscationConfiguratorManager
				.getActiveObfuscationConfigurator((IProject) getElement());

		itsConfiguratorsCombo = new Combo(parent, SWT.DROP_DOWN);

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
		// addSecondSection(composite);
		addConfigSelectionSection(composite);
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
		doSelectActiveConfiguratorInComboById(ObfuscationConfiguratorManager
				.getActiveObfuscationConfigurator((IProject) getElement())
				.getID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	public boolean performOk() {
		try {
			((IResource) getElement()).setPersistentProperty(new QualifiedName(
					"", ACTIVE_CONFIGURATOR_PROPERTY),
					((IObfuscationConfigurator) itsConfiguratorsArray
							.get(itsActiveConfiguratorIndex)).getID());
			return true;
		} catch (CoreException e) {
			return false;
		}
	}

}