// $Id:$

package de.partmaster.obfuscate4e;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * code based on org.eclipse.pde.internal.ui.PDEPlugin
 */
public class Activator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "de.partmaster.obfuscate4e";

    // The shared instance
    private static Activator plugin;

    /**
     * The constructor
     */
    public Activator() {
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    public static void log(IStatus status) {
        ResourcesPlugin.getPlugin().getLog().log(status);
    }

    public static void logErrorMessage(String message) {
        log(new Status(IStatus.ERROR, getPluginId(), IStatus.ERROR, message, null));
    }

    public static String getPluginId() {
        return getDefault().getBundle().getSymbolicName();
    }
    
    public static IWorkbenchWindow getActiveWorkbenchWindow() {
        return getDefault().getWorkbench().getActiveWorkbenchWindow();
    }

    public static IWorkspace getWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }

    public static void logException(Throwable e, final String title, String message) {
        if (e instanceof InvocationTargetException) {
            e = ((InvocationTargetException) e).getTargetException();
        }
        IStatus status = null;
        if (e instanceof CoreException)
            status = ((CoreException) e).getStatus();
        else {
            if (message == null)
                message = e.getMessage();
            if (message == null)
                message = e.toString();
            status = new Status(IStatus.ERROR, getPluginId(), IStatus.OK, message, e);
        }
        ResourcesPlugin.getPlugin().getLog().log(status);
        Display display = getStandardDisplay();
        final IStatus fstatus = status;
        display.asyncExec(new Runnable() {

            public void run() {
                ErrorDialog.openError(null, title, null, fstatus);
            }
        });
    }

    private static Display getStandardDisplay() {
        Display display;
        display = Display.getCurrent();
        if (display == null)
            display = Display.getDefault();
        return display;
    }

    public static void logException(Throwable e) {
        logException(e, null, null);
    }

    public static void log(Throwable e) {
        if (e instanceof InvocationTargetException)
            e = ((InvocationTargetException) e).getTargetException();
        IStatus status = null;
        if (e instanceof CoreException)
            status = ((CoreException) e).getStatus();
        else
            status = new Status(IStatus.ERROR, getPluginId(), IStatus.OK, e.getMessage(), e);
        log(status);
    }

}
