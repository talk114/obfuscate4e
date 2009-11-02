// $Id:$

package org.obfuscate4e.updater;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.obfuscate4e.Activator;
import org.obfuscate4e.preferences.PreferenceConstants;
import org.obfuscate4e.util.ChecksumHelper;

/**
 * This class is the abstract superclass for Obfuscate4e file updaters. It
 * stores the filename and provides general backup handling.
 * 
 * @author fwo
 * 
 */
public abstract class AbstractFileUpdater extends AbstractUpdater {

	public static final String NL = System.getProperty("line.separator");

	private final String itsFilename;

	/**
	 * returns the name of the file that is to get updated
	 * 
	 * @return the fileName
	 */
	public String getFilename() {
		return itsFilename;
	}

	/**
	 * returns the present Filename with the backup extension from the
	 * Preference settings
	 * 
	 * @return the backupFilename
	 */
	protected String getBackupFilename() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String fileExtension = store
				.getString(PreferenceConstants.BACKUP_EXTENSION);

		while (fileExtension.startsWith(".")) {
			fileExtension = fileExtension.substring(1);
		}

		return getFilename() + "." + fileExtension;
	}

	/**
	 * the constructor
	 * 
	 * @param generator
	 *            the belonging generator
	 * @param filename
	 *            the name of the file to update
	 * @param description
	 *            the String that describes the kind of update
	 */
	public AbstractFileUpdater(String filename, String description) {
		super(description);
		itsFilename = filename;
	}

	/**
	 * is a general update process method:
	 * <p>
	 * in case the file already exists and the settings do enable backups, a<br>
	 * copy of the old file will get saved.<br>
	 * in case the file already exists and the settings do enable a confirmation
	 * <br>
	 * dialog, a message dialog will be shown.
	 * <p>
	 * Then generated code will be written to the filesystem.
	 */
	abstract public void run(IProgressMonitor monitor) throws CoreException;

	/**
	 * @return <b>true</b> if a backup file shall be saved.<br>
	 *         usually to be read from the specific preference settings
	 */
	protected boolean isSavingBackupFileEnabled() {
		return Activator.getDefault().getPreferenceStore().getBoolean(
				PreferenceConstants.SAVE_BACKUP);
	}

	/**
	 * Pops up a dialog if a file is about to be overwritten.
	 * 
	 * @see AbstractUpdater#isOverwriteConfirmationEnabled()
	 * @param file
	 *            the file to be overwritten
	 * @return confirmation
	 */
	public boolean overwriteDialog(IFile file) {

		if (!file.exists()) {
			return true;
		}

		if (!isOverwriteConfirmationEnabled()) {
			return true;
		}

		Shell activeShell = PlatformUI.getWorkbench().getDisplay()
				.getActiveShell();

		class MessageDialogWithPreference extends MessageDialog {
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
				rememberButton
						.setText("Don't ask in the future (for any projects)");
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

		String message = "There exists a file '" + file.getName()
				+ "' in project '" + file.getProject().getName() + "'." + NL
				+ "Update its content?";

		MessageDialogWithPreference dialog = new MessageDialogWithPreference(
				activeShell, getDescription(), null, message,
				MessageDialog.QUESTION, new String[] {
						IDialogConstants.OK_LABEL,
						IDialogConstants.CANCEL_LABEL }, 0);

		boolean confirmation = dialog.open() == 0;
		if (confirmation && dialog.rememberMyDecision) {
			rememberConfirmationDecision();
		}
		return confirmation;
	}

	/**
	 * Saves a file with the new content. If the content didn't change, the file
	 * won't be overwritten. Otherwise a backup copy will be saved (if enabled
	 * by workspace preferences).
	 * 
	 * @param fileToUpdate
	 * @param newContent
	 * @param monitor
	 * @throws CoreException
	 */
	protected void saveFile(IFile fileToUpdate, String newContent,
			IProgressMonitor monitor) throws CoreException {
		
	//	System.out.println(fileToUpdate.getName()+"'s new checksum: " + md5(newContent));
		String fileContent = "";
		InputStream newContentInputStream = new ByteArrayInputStream(newContent
				.getBytes());

		if (fileToUpdate.exists()) {
			try {
				fileContent = ChecksumHelper.inputStreamToString(fileToUpdate.getContents());
			} catch (IOException e1) {
				return;
			}
			if (fileContent.equals(newContent)) {
				return;
			}
			boolean confirmation = overwriteDialog(fileToUpdate);
			if (confirmation == false) {
				return;
			}
			if (isSavingBackupFileEnabled()) {
				IFile backupFile = getProject()
						.getFile(getBackupFilename());

				ByteArrayInputStream backupInputStream = new ByteArrayInputStream(
						fileContent.getBytes());

				if (backupFile.exists()) {
					backupFile.setContents(backupInputStream, IFile.FORCE,
							monitor);
				} else {
					backupFile.create(backupInputStream, false, monitor);
				}
			}
			fileToUpdate.setContents(newContentInputStream, IFile.FORCE,
					monitor);
		} else {
			fileToUpdate.create(newContentInputStream, false, monitor);
		}
	}

}
