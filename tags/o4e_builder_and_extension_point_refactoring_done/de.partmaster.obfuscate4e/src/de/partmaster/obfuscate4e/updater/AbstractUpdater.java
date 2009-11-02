// $Id:$
package de.partmaster.obfuscate4e.updater;

import de.partmaster.obfuscate4e.ProjectInfo;

/**
 * This abstract class provides the most basic functionality for updaters for
 * Obfuscate4e.<br>
 * It holds the ProjectInfo and the description.
 * 
 * @author fwo
 * 
 */
public abstract class AbstractUpdater implements IUpdater {

	private final String itsDescription;
	private ProjectInfo itsProjectInfo;

	/**
	 * the constructor
	 */
	public AbstractUpdater(String description) {
		itsDescription = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.partmaster.obfuscate4e.updater.IUpdater#configureProjectSettings(de
	 * .partmaster.obfuscate4e.ProjectInfo)
	 */
	public void configureProjectSettings(final ProjectInfo projectInfo) {
		itsProjectInfo = projectInfo;
	}

	/**
	 * returns the description what this updater does
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return itsDescription;
	}

	/**
	 * Is called when "remember my decision" was selected in the confirmation
	 * dialog. Inheriting concrete classes may implement a preference storage.
	 * Should affect <b>isOverwriteConfirmationEnabled()</b> to return
	 * <b>false</b>
	 */
	protected abstract void rememberConfirmationDecision();

	/**
	 * @return <b>true</b> if dialog boxes are wanted. <b>false</b> if no dialog
	 *         boxes shall appear.
	 */
	protected abstract boolean isOverwriteConfirmationEnabled();

	/**
	 * returns the ProjectInfo
	 * 
	 * @see ProjectInfo
	 * @return the ProjectInfo
	 */
	protected ProjectInfo getProjectInfo() {
		return itsProjectInfo;
	}
}