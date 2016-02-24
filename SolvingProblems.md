# Solving problems #



If the _obfuscate4e_ plugin does not work as expected, then you have some possibilities at hand.

  * If the obfuscation works manually at first run, but not at subsequent runs: Refresh the Eclipse workspace and check if there is a folder <tt>@dot</tt> or <tt>temp.folder</tt> in the plugin project. If they are, then remove it. Eclipse _PDE_ build doesn&acirc;&euro;&trade;t run the customBuildCallbacks if they exist.
  * Check the obfuscation setting in the _proguard.cfg_ file in your plugin project. The syntax of this file is explained at the [Proguard website](http://proguard.sourceforge.net/) (look at the Usage section).
  * Run the obfuscation in verbose mode. Ant produces some output which might show the problem.
  * Go to the [bug tracker](http://code.google.com/p/obfuscate4e/issues/list), there might be solutions matching your problem.
  * If you encountered a bug, then you might submit a [bug report](http://code.google.com/p/obfuscate4e/issues/list).

Please submit the following information:

  * The version of your _Java_ and _Eclipse_ installations,
  * The contents of _proguard.cfg_ and _temp.folder/proguard.map_, if available,
  * The output of the _verbose_ mode Ant run (see next section for details),
  * optionally the _MANIFEST.MF_ of the plugin (to ease the check of the proguard configuration).

## Inspect the obfuscated product ##

It is quite easy to look how _obfuscate4e_ processed your product. You need a
de-compiler and an exported Eclipse _RCP_ product. I&acirc;&euro;&trade;ll
describe the procedure on a linux box.
I use [Jode](http://jode.sourceforge.net/). This Project seems
&acirc;&euro;&oelig;dead&acirc;&euro;, at least there were no new releases
[since 2004](https://sourceforge.net/projects/jode/files/). But it works well
for this job.

Download the [https://sourceforge.net/projects/jode/files/ latest jode
distribution] and extract the file ```
jode*.jar```. Then open a shell an
go to the folder where you exported the product to. Now execute the command
shown below:

```
java -jar ~/Apps/jode-1.1.2-pre1.jar $(echo plugins/*  | tr " " ",")
```

The ```
$(echo plugins/* | tr " " ",")``` selects all jars and folders inside the ```
plugins/``` directory and compiles tthem into a comma separated list. That&acirc;&euro;&trade;s how _jode_ expects the resources to be inspected.

A window pops up and you can traverse the class structure and look at the obfuscated build.

![https://obfuscate4e.googlecode.com/svn/wiki/images/screenshot-jode.png](https://obfuscate4e.googlecode.com/svn/wiki/images/screenshot-jode.png)

## Run the obfuscator in verbose mode ##

It might happen that the obfuscation does not work as expected. In this case
there may be some "strange" folders in your project and the generated plugin
jar contains non-obfuscated classes.

But the build process runs in headless mode and no errors or warnings are
printed out in the console view.

The easiest way to debug the obfuscation process is to create and execute a
build.xml file for the plugin.

At first generate a build.xml file by clicking on _PDE Tools > Create Ant Build
File_ in the context menu of the project or the _META-INF/MANIFEST.MF_ file.
Then run the build.xml file by clicking the _Run As > Ant Build_ entry of the
context menu of the _build.xml_ file.<br> The output of the plugin build is<br>
shown in the console view. Warnings and error messages are highlighted.<br>
<br>
<img src='https://obfuscate4e.googlecode.com/svn/wiki/images/run-build_xml_preview.png' />

The target <i>post.@dot</i> contains the messages relevant to the obfuscation. In<br>
the above screenshot there is a error message:<br>
<br>
<pre><code>[subant] Failure for target 'post.@dot' of:<br>
         /prj/de.partmaster.mytest/customBuildCallbacks.xml<br>
[subant] The following error occurred while executing this line:<br>
[subant] /prj/de.partmaster.mytest/customBuildCallbacks.xml:119:<br>
         Problem: failed to create task or type proguard<br>
[subant] Cause: The name is undefined.<br>
[subant] Action: Check the spelling.<br>
[subant] Action: Check that any custom tasks/types have been declared.<br>
[subant] Action: Check that any &lt;presetdef&gt;/&lt;macrodef&gt; <br>
         declarations have taken place.<br>
</code></pre>

In this case <i>Ant</i> could not find the declaration of the <i>proguard</i> task. This<br>
should not happen in practice - otherwise check your Eclipse installation in<br>
<i>Help > Software Updates > Manage configuration</i> for broken plugin<br>
dependencies.<br>
As a rule of thumb you should follow make sure that you<br>
<br>
<ol><li>rebuild the <i>customBuildCallbacks.xml</i> file if you change the package structure of the plugin,<br>
</li><li>rebuild the <i>build.xml</i> file if you change anything in <i>customBuildCallbacks.xml</i>.<br>
</li><li>run the <i>build.xml</i> script in verbose mode if you cant find the error.<br>
</li><li>Attach the verbose console output together with the file proguard.properties to any <a href='https://sourceforge.net/tracker/?group_id=202507'>bug report</a> and name the version of Java and Eclipse you are using.