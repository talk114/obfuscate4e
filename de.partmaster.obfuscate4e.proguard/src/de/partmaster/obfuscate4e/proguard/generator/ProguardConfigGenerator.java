package de.partmaster.obfuscate4e.proguard.generator;

import java.util.List;
import de.partmaster.obfuscate4e.generator.IBuildGenerator;
import de.partmaster.obfuscate4e.generator.ProjectData;

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
  protected final String TEXT_1 = NL + "# Template file of the proguard configuration" + NL + "# specify your custom proguard settings here" + NL + "# see: http://proguard.sourceforge.net/manual/index.html" + NL + "" + NL + "-dontusemixedcaseclassnames" + NL + "" + NL + "-keep class * implements org.osgi.framework.BundleActivator {" + NL + "\tpublic *; " + NL + "}" + NL + "" + NL + "-keep class * extends org.eclipse.osgi.util.NLS {" + NL + "\tpublic *; " + NL + "}" + NL;
  protected final String TEXT_2 = NL + "-keep class ";
  protected final String TEXT_3 = " {" + NL + " \tpublic protected *;" + NL + "}   ";
  protected final String TEXT_4 = "\t\t\t" + NL;
  protected final String TEXT_5 = NL + "-keep class ";
  protected final String TEXT_6 = ".* {" + NL + " \tpublic protected *;" + NL + "}   ";
  protected final String TEXT_7 = "\t\t\t";
  protected final String TEXT_8 = NL;

	public String generate(ProjectData argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
     List excludePackages = argument.getExcludePackages(); 
     List excludeClasses = argument.getExcludeClasses(); 
    stringBuffer.append(TEXT_1);
     for (int i=0; i<excludeClasses.size(); i++) {
    stringBuffer.append(TEXT_2);
    stringBuffer.append(excludeClasses.get(i));
    stringBuffer.append(TEXT_3);
     } 
    stringBuffer.append(TEXT_4);
     for (int i=0; i<excludePackages.size(); i++) {
    stringBuffer.append(TEXT_5);
    stringBuffer.append(excludePackages.get(i));
    stringBuffer.append(TEXT_6);
     } 
    stringBuffer.append(TEXT_7);
    stringBuffer.append(TEXT_8);
    return stringBuffer.toString();
  }
}