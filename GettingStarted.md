

# Configure the obfuscation #

Now it's time to configure how _obfuscate4e_ and its embedded obfuscator should work. We use the ProGuard obfuscator in this sectin. The configuration of other obfuscators will differ.

Execute the action _Create Proguard obfuscation configuration_ from the context menu of the plugin _MANIFEST.MF_.

![http://obfuscate4e.googlecode.com/svn/wiki/images/generate_custombuildcallbacks.png](http://obfuscate4e.googlecode.com/svn/wiki/images/generate_custombuildcallbacks.png)

In the project folder appear two new files: _customBuildCallbacks.xml_ and _proguard.cfg_. These files are managed inside your project (and also version controlled).

![https://obfuscate4e.googlecode.com/svn/wiki/images/new_custombuildcallbacks.png](https://obfuscate4e.googlecode.com/svn/wiki/images/new_custombuildcallbacks.png)

ProGuard allows to highly customize the obfuscation process. You can add cunfiguration statements to the _proguard.cfg_ file. See <a href='http://proguard.sourceforge.net/manual/usage.html'><a href='http://proguard.sourceforge.net/manual/usage.html'>http://proguard.sourceforge.net/manual/usage.html</a></a> for valid configuration parameters.

## Excluding classes or packages from obfuscation ##

Obfuscate4e detects exported packages and and writes the corresponding obfuscation exclusion statements automatically into the file _proguard.cfg_ at your plugin's root directory.

Starting from version 0.7 of _obfuscate4e_ all classes references from plugin extensions (in _plugin.xml_) are automatically inserted into the obfuscator exclusion list. Thus the obfuscate4e configuration should be updated whenever a new extension is added to the plugin.<br>
<i>Remark: This feature works only on Eclipse 3.4 (there seems to be a bug or non-implemented feature in Eclipse 3.2 and 3.3)</i>

Of course you can add custom exclusion statements by hand. For instance If a class gets instantiated via reflection or is referenced by name in any other way, it has to be excluded.<br>
<br>
<h3>Exclude all classes in a package</h3>

By default <i>obfuscate4e</i> adds all exported packages of a plugin to the exclusion list.<br>
<br>
There are to options if you want to add other packages to the exclution list:<!--break--><br>
<br>
<ol><li>Export the package using the Manifest editor (if this is useful for your project)<br>
</li><li>Add a following lines to the plugin's <i>proguard.cfg</i> file:<br>
<pre><code>-keep class de.example.plugin.* {<br>
  public protected *; <br>
}<br>
</code></pre>
</li></ol><blockquote><i>Remark: If you modified the proguard.cfg file manually, then these changes will be lost when re-generating the configuration!</i></blockquote>

<h3>Exclude a single class</h3>

You might want to exclude a certain file from obfuscation. This might be the case if the class is used in an extension of your plugin.<!--break--><br>
<br>
To exclude a class from obfuscation you have to add a statement like<br>
<br>
<pre><code>-keep class de.example.plugin.actions.MySpecialAction {<br>
  public protected *;<br>
}<br>
</code></pre>

to the <i>proguard.cfg</i> file. All other classes in the package <i>de.example.plugin.actions</i> are completely obfuscated.<br>
<br>
<i>Remark: If you modified the proguard.cfg file manually, then these changes will be lost when re-generating the configuration!</i>

<h3>Exclude all classes of a certain kind</h3>

To exclude all classes that implement a certain interface or extend a certain class (e.g. all views, actions, perspectives, wizards, ...) in a generic way you need to exclude the appropriate interfaces or super classes:<br>
<br>
<pre><code>-keep class * extends org.eclipse.ui.part.ViewPart {<br>
  public *;<br>
}<br>
-keep class * implements org.eclipse.jface.action.IAction {<br>
  public *;<br>
}<br>
-keep class * implements org.eclipse.ui.IPerspectiveFactory {<br>
  public *;<br>
}<br>
-keep class * implements org.eclipse.equinox.app.IApplication {<br>
  public *;<br>
}<br>
-keep class * implements org.eclipse.jface.wizard.IWizard {<br>
  public *;<br>
}<br>
</code></pre>

To exclude all access methods (getters and setters) there is another generic way:<br>
<br>
<pre><code>-keep class * implements org.example.AnyInterface {<br>
  void set*(***);<br>
  void set*(int, ***);<br>
  boolean is*();<br>
  boolean is*(int);<br>
  *** get*();<br>
  *** get*(int);<br>
}<br>
</code></pre>

<i>Remark: If you modified the <code>proguard.cfg</code> file manually, then these changes will be lost when re-generating the configuration!</i>

<h1>Obfuscate your code</h1>

It's time to use your obfuscator. As it is embedded into the built process Eclipse runs the obfuscator when exporting a plugin, a project or a complete product.<br>
<br>
Export a plugin with <i>File > Export ...</i>

<img src='https://obfuscate4e.googlecode.com/svn/wiki/images/export_plugin_preview.png' />

The plugin will be obfuscated and exported.<br>
<br>
If you <a href='SolvingProblems#Run_the_obfuscator_in_verbose_mode.md'>run the obfuscation in "verbose" mode</a>, then you can see ant processing the plugin and&nbsp; check the applied names mapping in the file <i>proguard.map</i>. This file is located in the folder <i>temp.folder/</i> of the plugin. This file shows a list of processed files as well as the names of class, fields and methods after obfuscation.<br>
<br>
<img src='https://obfuscate4e.googlecode.com/svn/wiki/images/proguard_map_preview.png' />