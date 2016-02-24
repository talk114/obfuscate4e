# Releases #

## Version 0.7.1 ##

Released on 06/03/2009

Today we released version 0.7.1 of the _obfuscate4e_ feature.

This version is a maintenance release and closes some Eclipse 3.5 issues as
well as minor bugs. Additionally the Eclipse help stuff was separated into it's
own plugin _de.partmaster.obfuscate4e.help_.
Obfuscate4e is ready for _Eclipse Galileo_ by now!

Obfuscate4e runs on Eclipse 3.4 and above (and with degraded functionality also
on Eclipse 3.2 / 3.3) and needs Java 1.4 or higher. It can be installed via the
update site at [http://obfuscate4e.org/updates/
http://obfuscate4e.org/updates/] and from an "archived update site", available
at the [download section](https://sourceforge.net/projects/obfuscate4e/files/)
of this project.

_Remark:_ Obfuscate4e now creates it's own temporary folder "temp.obfuscate4e/"
to store interim build artefacts.

> ## Version 0.7.0 ##

Released on 05/05/2009

Today we released version 0.7.0 of the <em>obfuscate4e</em> feature.

This version adds automatic detection (and exclusion from obfuscation) of
classes used in the plugin's extensions. This feature requires Eclipse 3.4 (or
above) to work correctly. We also included an Obfuscate4e section for Eclipse's
online help.

Obfuscate4e runs on Eclipse 3.2 and above and needs Java 1.4 or higher. It can
be installed via the update site at [http://obfuscate4e.org/updates/
http://obfuscate4e.org/updates/] and from an "archived update site", available
at the [download section](https://sourceforge.net/projects/obfuscate4e/files/)
of this project.

> ## Version 0.6.3 ##

Released on 01/13/2009

We just released version 0.6.3 of obfuscate4e plugin. This version fixes a
"bug":/blog/critical-bug-in-version-0-6-2 introduced in version 0.6.2.

This release is avaliable at the
[download section](https://sourceforge.net/projects/obfuscate4e/files/) at
sourceforge.net as well as via the [http://obfuscate4e.org/updates update
site].

## Version 0.6.2 ##

Released on 12/29/2008

We used the christmas holidays to work on obfuscate4e a bit. So it's time for a new release!
This version features:

  * in de.partmaster.obfuscate4e.plugin and de.partmaster.obfuscate4e.proguard.plugin:
    * Streamlined Plugin code base and generated Ant code,
    * Defined <em>J2SE-1.4</em> as minimum execution environment

  * in de.partmaster.obfuscate4e.proguard.plugin:
    * Moved Proguard code into it's own plugin (separation of obfuscate4e and 3rd party code base)
    * Keep all classes that extend <em>org.eclipse.osgi.util.NLS</em> (Language bundles)
    * inserted flag "-dontusemixedcaseclassnames" to make the obfuscator robust on non-casesensitive file systems
    * replaced home-grown Ant mapper by standard ant magic, optimized Ant scripting.
    * All temporary files go into <em>&lt;plugin&gt;/temp.folder/</em>
    * Processed <em>proguard.cfg</em> and generated <em>proguard.map</em> will be included in PDE build log.zip archive

  * in proguard.plugin:
    * Updated embedded proguard.jar to version [4.3](https://sourceforge.net/projects/proguard/files/proguard/4.3/)

Obfuscate4e runs on Eclipse 3.2 and above and needs Java 1.4 or higher. It can
be installed via the update site at [http://obfuscate4e.org/updates/
http://obfuscate4e.org/updates/] and from an "archived updatesite", available
at the [download section](https://sourceforge.net/projects/obfuscate4e/files/)
of this project.

## Release: 0.6.1 ##

Released on 05/09/2008

This release provides a better management of the build process and fixes the
spaces-in-filenames-bug. The proguard configuration file contains only
obfuscation settings, all path definitions were removed.

Obfuscate4e runs on Eclipse 3.2 and above and needs Java 1.4 or higher. It can
be installed via the update site at [http://obfuscate4e.org/updates/
http://obfuscate4e.org/updates/] and from an "archived updatesite", available
at the [download section](https://sourceforge.net/projects/obfuscate4e/files/)
of this project.

## Release: 0.6.0 ##

Released on 04/28/2008

This version provides an improved Proguard obfuscator integration and an
[updated version](https://sourceforge.net/projects/proguard) of the obfuscator.
The configuration for the obfuscator is generated into its own config file and
can be tuned without manipulating the build scripts.

The proguard-specific code has been moved to its own feature. This eases the
integration of other obfuscators into obfuscate4e.<!--break-->

Obfuscate4e runs on Eclipse 3.2 and above and needs Java 1.4 or higher. It can
be installed via the update site at [http://obfuscate4e.org/updates/
http://obfuscate4e.org/updates/] and from an "archived updatesite", available
at the [download section](https://sourceforge.net/projects/obfuscate4e/files/)
of this project.

## Release: 0.5.0.1 ##

Released on 12/19/2007

This release modifies the downloadable Zip archive. It can now be used as an
archived update site in the Eclipse update manager.

The program code of this release has not changed since release 0.5.0.

### Whats new: ###

This release modifies the downloadable Zip archive. It can now be used as an
archived update site in the Eclipse update manager.<br>The program code of this<br>
release has not changed since release 0.5.0.<br>
<br>
<h3>Update site:</h3>

<a href='http://obfuscate4e.org/updates'>http://obfuscate4e.org/updates</a>

<h3>Download:</h3>

<a href='https://sourceforge.net/projects/obfuscate4e/files/'>https://sourceforge.net/projects/obfuscate4e/files/</a>

<h2>Release: 0.5.0</h2>

Released on 12/12/2007<br>
<br>
This is the initial public release.<br>
<br>
<h3>Whats new:</h3>

<b>Built-in support for ProGuard Obfuscator.</b>

<h3>Issues:</h3>

<b>The classpath for ProGuard might not contain spaces (like</b><em>c:\documents<br>
<blockquote>and settings\drue\Desktop</em>. You need a proper Eclipse install location<br>
and workspace location</blockquote>

<h3>Update site:</h3>

<a href='http://obfuscate4e.org/updates'>http://obfuscate4e.org/updates</a>

<h3>Download:</h3>

<a href='http://sourceforge.net/projects/obfuscate4e/files/'>http://sourceforge.net/projects/obfuscate4e/files/</a>