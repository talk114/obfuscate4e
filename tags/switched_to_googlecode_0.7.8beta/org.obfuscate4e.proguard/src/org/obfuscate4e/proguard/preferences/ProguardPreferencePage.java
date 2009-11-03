package org.obfuscate4e.proguard.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.obfuscate4e.preferences.PreferenceConstants;
import org.obfuscate4e.proguard.Activator;
import org.obfuscate4e.proguard.ProguardConfigFileUpdater;


/**
 * The class for the representation of the Proguard preference page. Serves the
 * option to disable overwriting confirmation dialogs and saving a backup of the
 * old configuration file
 * 
 * @author fwo
 * 
 */
public class ProguardPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public ProguardPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Proguard preferences");
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

		Composite confirmationsGroup = getGroupWrapper(groups, "Confirmations");
	
		BooleanFieldEditor showDialogField = new BooleanFieldEditor(
				PreferenceConstants.ASK_FOR_OVERWRITING_PROGUARDCFG,
				"Ask before &overwriting "
						+ ProguardConfigFileUpdater.PROGUARD_CONFIG,
				confirmationsGroup);


		addField(showDialogField);

	}

	private Composite getGroupWrapper(Composite parent, String groupTitle){
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