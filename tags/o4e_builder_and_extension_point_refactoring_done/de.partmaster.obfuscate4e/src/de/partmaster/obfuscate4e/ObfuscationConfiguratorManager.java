package de.partmaster.obfuscate4e;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;

import de.partmaster.obfuscate4e.properties.Obfuscate4ePropertyPage;

/**
 * This class is the enabler for the Obfuscate4e configurator extensions. It
 * provides the methods to get all registered configurators as well as setting
 * the active one.
 * 
 * @author fwo
 * 
 */
public class ObfuscationConfiguratorManager {

	private static final String CONFIGURATOR_ID = "de.partmaster.obfuscate4e.ObfuscationConfigurator";

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
	public static boolean getAllObfuscationConfigurators(final List configList) {
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
	 */
	public static IObfuscationConfigurator getActiveObfuscationConfigurator(
			IProject project) {

		if (project != null) {
			String configuratorId = null;
			try {
				configuratorId = project
						.getPersistentProperty(new QualifiedName(
								"",
								Obfuscate4ePropertyPage.ACTIVE_CONFIGURATOR_PROPERTY));
			} catch (CoreException e) {
			}
			if (configuratorId != null) {
				IObfuscationConfigurator configurator = getConfiguratorById(configuratorId);
				if (configurator != null) {
					try {
						project
								.setPersistentProperty(
										new QualifiedName(
												"",
												Obfuscate4ePropertyPage.ACTIVE_CONFIGURATOR_PROPERTY),
										configurator.getID());
					} catch (CoreException e) {
					}
					return configurator;
				}
			}
		}
		
		List configurators = new ArrayList();

		if (!getAllObfuscationConfigurators(configurators)) {
			return null;
		}

		IObfuscationConfigurator activeObfuscationConfigurator = (IObfuscationConfigurator) configurators
				.get(0);

		try {
			project.setPersistentProperty(new QualifiedName("",
					Obfuscate4ePropertyPage.ACTIVE_CONFIGURATOR_PROPERTY),
					activeObfuscationConfigurator.getID());
		} catch (CoreException e) {
			return null;
		}

		return activeObfuscationConfigurator;
	}

	/**
	 * returns a registered configurator for the given identifier.
	 * 
	 * @param configuratorId
	 * @return the configurator
	 */
	public static IObfuscationConfigurator getConfiguratorById(
			String configuratorId) {
		List configurators = new ArrayList();
		if (!getAllObfuscationConfigurators(configurators)) {
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
}
