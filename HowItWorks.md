# How it works #

The Eclipse Build process can be highly customized. The things _obfuscate4e_ does are

  * Register an Ant task for the obfuscator (currently ProGuard) in the Eclipse platform.
  * Create an Ant script (_custombuildcallbacks.xml_) containing a set of pre-defined targets. These targets are hooks for additional build code. Per default they are left empty.
  * Implement the hooks as desired. _obfuscate4e_ puts the code to run the obfuscation process into the Ant task _post.@dot_.
  * Create the proguard.properties file containing the obfuscation rules of the plugin.
  * Register the Ant script in the _build.properties_ of the plugin.

## Requirements ##

At runtime, _obfuscate4e_ requires Java1.4.2 or better and Eclipse 3.2 or above.

You need the following to develop on the _obfuscate4e_ code base:

  * Eclise SDK 3.2 or above
  * Java SE 1.4.2 SDK or above
  * Eclipse Modeling Framework (EMF) code generator feature (`org.eclipse.emf.codegen`)