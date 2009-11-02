package de.partmaster.obfuscate4e.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.partmaster.obfuscate4e.Activator;
import de.partmaster.obfuscate4e.updater.IUpdater;

/**
 * Representation of the general Obfuscate4e preference page. Serves the option
 * to disable file overwriting confirmation dialogs
 * 
 * @author fwo
 * 
 */

public class Obfuscate4ePreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

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

		Composite wrappedConfirmationGroup = getGroupWrapper(groups,
				"Confirmations");
		Composite wrappedBackupGroup = getGroupWrapper(groups, "Backup");
		addField(new BooleanFieldEditor(
				PreferenceConstants.ASK_FOR_OVERWRITING_BUILD_PROPERTIES,
				"Ask before overwriting &build properties",
				wrappedConfirmationGroup));

		addField(new BooleanFieldEditor(
				PreferenceConstants.ASK_FOR_OVERWRITING_CUSTOMBUILDCALLBACKSXML,
				"Ask before overwriting "
						+ IUpdater.FILE_CUSTOM_BUILD_CALLBACKS,
				wrappedConfirmationGroup));

		StringFieldEditor fileExtensionField = new StringFieldEditor(
				PreferenceConstants.BACKUP_EXTENSION,
				"File extension of the backup files:", wrappedBackupGroup);
		addField(fileExtensionField);
	}

	/**
	 * returns a Composite that includes a Group box <br>
	 * This is needed for a nasty workaround because the FieldEditorPreferencePage does set up
	 * the LayoutManager for the FieldEditors' parents!
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
}