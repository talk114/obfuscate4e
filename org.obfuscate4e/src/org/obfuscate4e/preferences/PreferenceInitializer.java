package org.obfuscate4e.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.obfuscate4e.Activator;


/**
 * Class used to initialize the Obfuscate4e preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/**
	 * initializes the default Obfuscate4e preference values 
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.ASK_FOR_OVERWRITING_CUSTOMBUILDCALLBACKSXML, true);
		store.setDefault(PreferenceConstants.BACKUP_EXTENSION, ".bak");
		store.setDefault(PreferenceConstants.SAVE_BACKUP, true);
		store.setDefault(PreferenceConstants.ASK_FOR_OVERWRITING_PROGUARDCFG, true);
		store.setDefault(PreferenceConstants.ASK_FOR_OVERWRITING_ZKMCONF, true);
		store.setDefault(PreferenceConstants.PREFERRED_ACTIVE_CONFIGURATOR_ID, "");
		store.setDefault(PreferenceConstants.ENABLE_BUILDER_FOR_NEW_PROJECTS, true);
	}

}
