package org.obfuscate4e.preferences;

import java.util.ArrayList;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.obfuscate4e.Activator;
import org.obfuscate4e.IObfuscationConfigurator;
import org.obfuscate4e.ObfuscationConfiguratorManager;
import org.obfuscate4e.updater.IUpdater;

/**
 * Representation of the general Obfuscate4e preference page. Serves the option
 * to disable file overwriting confirmation dialogs
 * 
 * @author fwo
 * 
 */

public class Obfuscate4ePreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	private static final String ERROR_FETCHING_LIST = "Sorry, an error occured while fetching the configurators list.";

	private int itsPreferredConfiguratorIndex;
	private Combo itsConfiguratorsCombo;
	private final ArrayList itsConfiguratorsArray = new ArrayList();

	public Obfuscate4ePreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("General preferences for Obfuscate4e");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		GridData gdata;
		GridLayout glayout;
		Composite groups = new Composite(getFieldEditorParent(), SWT.NONE);

		gdata = new GridData();
		gdata.horizontalSpan = 2;
		gdata.horizontalAlignment = GridData.FILL;
		gdata.grabExcessHorizontalSpace = true;

		glayout = new GridLayout();
		glayout.numColumns = 2;
		glayout.marginWidth = 0;
		glayout.marginTop = 6;

		groups.setLayoutData(gdata);
		groups.setLayout(glayout);

		Composite wrappedDefaultObfuscatorGroup = getGroupWrapper(groups,
				"Default obfuscator");

		Composite wrappedConfirmationGroup = getGroupWrapper(groups,
				"Confirmations");
		Composite wrappedBackupGroup = getGroupWrapper(groups, "Backup");


		addField(new BooleanFieldEditor(
				PreferenceConstants.ENABLE_BUILDER_FOR_NEW_PROJECTS,
				"Enable Builder for new projects",
				wrappedDefaultObfuscatorGroup));
		
		addConfiguratorSelector(wrappedDefaultObfuscatorGroup);


		addField(new BooleanFieldEditor(
				PreferenceConstants.ASK_FOR_OVERWRITING_CUSTOMBUILDCALLBACKSXML,
				"Ask before overwriting "
						+ IUpdater.FILE_CUSTOM_BUILD_CALLBACKS,
				wrappedConfirmationGroup));

		BooleanFieldEditor backupField = new BooleanFieldEditor(
				PreferenceConstants.SAVE_BACKUP,
				"Save backup files", wrappedBackupGroup);


		StringFieldEditor fileExtensionField = new StringFieldEditor(
				PreferenceConstants.BACKUP_EXTENSION,
				"File extension of the backup files:", wrappedBackupGroup);
		
		addField(fileExtensionField);
		addField(backupField);
	}

	private void addConfiguratorSelector(Composite parent) {
		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 2;
		parent.setLayout(gLayout);
		GridData gData = new GridData();
		
		Label myLabel = new Label(parent, SWT.NONE);
		String labelText = "Select the preferred obfuscator";
		myLabel.setText(labelText);

		gData = new GridData();
		gData.widthHint = convertWidthInCharsToPixels(labelText.length());
		myLabel.setLayoutData(gData);


		Control selectionElement = createConfiguratorSelectionComboBox(parent);

		gData = new GridData();
		gData.grabExcessHorizontalSpace = true;
		gData.horizontalAlignment = GridData.FILL;
		selectionElement.setLayoutData(gData);

	}

	private Control createConfiguratorSelectionComboBox(Composite parent) {

		// fetch all registered obfuscators
		if (!ObfuscationConfiguratorManager
				.fetchAllObfuscationConfigurators(itsConfiguratorsArray)) {
			Label errorLabel = new Label(parent, SWT.NULL);
			errorLabel.setText(ERROR_FETCHING_LIST);
			return errorLabel;
		}

		itsConfiguratorsArray.add(0, "");
		itsConfiguratorsCombo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
		itsConfiguratorsCombo.add("no preference"); // the first element is the empty one

		// fill the combobox
		for (int i = 1; i < itsConfiguratorsArray.size(); i++) {
			IObfuscationConfigurator configurator = (IObfuscationConfigurator) itsConfiguratorsArray
					.get(i);
			String name = configurator.getName();
			if (name == null) {
				name = configurator.getID();
			}
			itsConfiguratorsCombo.add(name);
		}

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String configuratorId = store
				.getString(PreferenceConstants.PREFERRED_ACTIVE_CONFIGURATOR_ID);

		doSelectPreferredConfiguratorInCombo(configuratorId);

		final class ConfiguratorComboSelectionListener implements
				SelectionListener {

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				itsPreferredConfiguratorIndex = ((Combo) e.getSource())
						.getSelectionIndex();
			}
		}

		itsConfiguratorsCombo
				.addSelectionListener(new ConfiguratorComboSelectionListener());

		return itsConfiguratorsCombo;
	}

	private boolean doSelectPreferredConfiguratorInCombo(String configuratorId) {

		if (itsConfiguratorsCombo == null) {
			return false;
		}
		int activeIndex = 0;

		for (int i = 1; i < itsConfiguratorsArray.size(); i++) {
			if (((IObfuscationConfigurator) itsConfiguratorsArray.get(i))
					.getID().equals(configuratorId)) {
				activeIndex = i;
			}
		}
		itsConfiguratorsCombo.select(activeIndex);
		if(itsConfiguratorsArray.size() == 2){
			itsConfiguratorsCombo.select(1);
			performApply();
		}

		return activeIndex >= 0;
	}

	/**
	 * returns a Composite that includes a Group box <br>
	 * This is needed for a nasty workaround because the
	 * FieldEditorPreferencePage does set up the LayoutManager for the
	 * FieldEditors' parents!
	 * 
	 * @param parent
	 * @param groupTitle
	 * @return
	 */
	private Composite getGroupWrapper(Composite parent, String groupTitle) {
		Group myGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
		Composite myComposite = new Composite(myGroup, SWT.NULL);
		GridLayout glayout;
		GridData gdata;

		myGroup.setText(groupTitle);

		gdata = new GridData();
		gdata.horizontalSpan = 2;
		gdata.horizontalAlignment = GridData.FILL;
		gdata.grabExcessHorizontalSpace = true;
		myGroup.setLayoutData(gdata);

		glayout = new GridLayout();
		glayout.numColumns = 2;
		glayout.marginTop = 1;
		glayout.marginBottom = 1;
		glayout.marginLeft = 1;
		glayout.marginRight = 1;
		myGroup.setLayout(glayout);

		gdata = new GridData();
		gdata.horizontalSpan = 2;
		gdata.horizontalAlignment = GridData.FILL;
		gdata.grabExcessHorizontalSpace = true;
		myComposite.setLayoutData(gdata);

		return myComposite;
	}

	private String fetchConfiguratorIdByIndex(int index) {
		if (index == 0) {
			return "";
		}
		return ((IObfuscationConfigurator) itsConfiguratorsArray.get(index))
				.getID();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performDefaults()
	 */
    protected void performDefaults() {
    	super.performDefaults();
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String configuratorId = store
				.getDefaultString(PreferenceConstants.PREFERRED_ACTIVE_CONFIGURATOR_ID);

    	doSelectPreferredConfiguratorInCombo(configuratorId);
    }

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
	 */
	public boolean performOk() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setValue(PreferenceConstants.PREFERRED_ACTIVE_CONFIGURATOR_ID,
				fetchConfiguratorIdByIndex(itsPreferredConfiguratorIndex));

		return super.performOk();
	}

}