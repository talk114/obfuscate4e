package org.obfuscate4e.proguard.generator;

import java.util.List;
import org.obfuscate4e.generator.IBuildGenerator;
import org.obfuscate4e.generator.ProjectData;
import org.obfuscate4e.generator.ProjectData.HeaderEntry;

public class ProguardConfigGenerator implements IBuildGenerator {

  protected static String nl;
  public static synchronized ProguardConfigGenerator create(String lineSeparator)
  {
    nl = lineSeparator;
    ProguardConfigGenerator result = new ProguardConfigGenerator();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "# ";
  protected final String TEXT_2 = NL + "# ";
  protected final String TEXT_3 = ": ";
  protected final String TEXT_4 = "\t\t\t" + NL + "# ";
  protected final String TEXT_5 = NL + "#" + NL + "# Template file of the proguard configuration" + NL + "# specify your custom proguard settings here" + NL + "# see: http://proguard.sourceforge.net/manual/index.html" + NL + "" + NL + "# Statements and excludes below are generated from the Eclipse project settings." + NL + "# Changes in here will be overwritten in the configuration creation process." + NL + "#" + NL + "###GENERATED###" + NL + "" + NL + "# begin o4e managed" + NL + "# MANAGED: automatic" + NL + "# OBJECT: org.osgi.framework.BundleActivator" + NL + "# CAUSE: PDE requirement" + NL + "# TYPE: API exclusion" + NL + "-keep class * implements org.osgi.framework.BundleActivator {" + NL + "\tpublic *; " + NL + "}" + NL + "#end o4e managed" + NL + "" + NL + "# begin o4e managed" + NL + "# MANAGED: automatic" + NL + "# OBJECT: org.eclipse.osgi.util.NLS" + NL + "# CAUSE: Bundle access" + NL + "# TYPE: API exclusion" + NL + "-keep class * extends org.eclipse.osgi.util.NLS {" + NL + "\tpublic *; " + NL + "}" + NL + "#end o4e managed";
  protected final String TEXT_6 = NL + NL + "# begin o4e managed" + NL + "# MANAGED: automatic" + NL + "# OBJECT: ";
  protected final String TEXT_7 = NL + "# CAUSE: extension class" + NL + "# TYPE: API exclusion" + NL + "-keep class ";
  protected final String TEXT_8 = " {" + NL + " \tpublic protected *;" + NL + "}   " + NL + "#end o4e managed";
  protected final String TEXT_9 = "\t\t\t";
  protected final String TEXT_10 = NL + NL + "# begin o4e managed" + NL + "# MANAGED: automatic" + NL + "# OBJECT: ";
  protected final String TEXT_11 = NL + "# CAUSE: shared package" + NL + "# TYPE: package exclusion" + NL + "-keep class ";
  protected final String TEXT_12 = ".* {" + NL + " \tpublic protected *;" + NL + "}   " + NL + "# end o4e managed";
  protected final String TEXT_13 = "\t\t\t" + NL + "" + NL + "# Statements below the static marker will not be affected by any updates." + NL + "# Changes in the static section will be carried with every creation process." + NL + "#" + NL + "# placeholders can be used in this configuration file to specify " + NL + "#   TEMPDIR ...... the folder used to store temporary information " + NL + "#                    provided by for obfuscate4e" + NL + "#   MAPPINGFILE .. the name of the file that contains all applied" + NL + "#                    name mappings during obfuscation. This file is" + NL + "#                    important to de-obfuscate the stacktrace in " + NL + "#                    case of errors.  " + NL + "#   CONFIGFILE ... the name of the file that contains the parsed" + NL + "#                    configuration file  " + NL + "# " + NL + "# The placeholders have to be enclosed by '@', like '@PLACEHOLDER@'." + NL + "# You can replace these placeholders by your customized paths" + NL + "#";
  protected final String TEXT_14 = NL;
  protected final String TEXT_15 = NL;

	public String generate(ProjectData argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
     List excludedPackages = argument.getExcludedPackages(); 
     List excludedClasses = argument.getExcludedClasses(); 
     List headerEntries = argument.getHeaderEntries(); 
     String staticPart = argument.getStaticPart(); 
    stringBuffer.append(TEXT_1);
    stringBuffer.append(argument.HEADER_BEGIN);
     for (int i=0; i<headerEntries.size(); i++) {
     HeaderEntry entry = (HeaderEntry) headerEntries.get(i); 
    stringBuffer.append(TEXT_2);
    stringBuffer.append(entry.itsKey);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(entry.itsValue);
     } 
    stringBuffer.append(TEXT_4);
    stringBuffer.append(argument.HEADER_END);
    stringBuffer.append(TEXT_5);
     for (int i=0; i<excludedClasses.size(); i++) {
    stringBuffer.append(TEXT_6);
    stringBuffer.append(excludedClasses.get(i));
    stringBuffer.append(TEXT_7);
    stringBuffer.append(excludedClasses.get(i));
    stringBuffer.append(TEXT_8);
     } 
    stringBuffer.append(TEXT_9);
     for (int i=0; i<excludedPackages.size(); i++) {
    stringBuffer.append(TEXT_10);
    stringBuffer.append(excludedPackages.get(i));
    stringBuffer.append(TEXT_11);
    stringBuffer.append(excludedPackages.get(i));
    stringBuffer.append(TEXT_12);
     } 
    stringBuffer.append(TEXT_13);
    stringBuffer.append(TEXT_14);
    stringBuffer.append(org.obfuscate4e.proguard.ProguardConfigFileUpdater.STATIC_MARKER);
    stringBuffer.append(TEXT_15);
    stringBuffer.append(staticPart);
    return stringBuffer.toString();
  }
}