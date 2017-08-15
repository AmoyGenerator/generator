/**
 * <copyright>
 *
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 *
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.jet.internal.runtime;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Information class on OSGi Bundle Jars.
 */
public class BundleJarInfo
{
  /**
   * Factory for create a BundleJarInfo instance
   * @param jarName the jar file name
   * @return the BundleJarInfo instance, or <code>null</code> if <code>jarName</code> is not a bundle jar.
   */
  public static BundleJarInfo createBundleJarInfo(String jarName)
  {
    final Matcher m = jarPattern.matcher(jarName);
    
    
    return m.matches() ? new BundleJarInfo(jarName, m.group(1), m.group(2)) : null;
  }

  private final String jarName;
  
  private static final String VERSION = "\\d+\\.\\d+\\.\\d+"; //$NON-NLS-1$
  /**
   * Pattern for pulling symbolic name and version from an exported JAR which is of the form
   * <p><code>symbolicName_version.jar</code></p>
   * <p>
   * Note that plug-in is can contain numbers, and that the 4th component may contain underscores, so
   * you can have a plug-in such as:
   * </p>
   * <p><code>symbolic name: a.b.c_1.2.3.x_yz</code></p>
   * <p><code>version: 4.5.6.a_b_c</code></p>
   * <p><code>jar file name: a.b.c_1.2.3.x_yz_4.5.6.a_b_c.jar</code></p>
   */
  private final static Pattern jarPattern = Pattern.compile(
    "((?:\\w|\\.|\\_)+)" // group 1: capture symbolic name  //$NON-NLS-1$
    + "_(" + VERSION + "(?:\\..*)?)" // group 2: capture version  //$NON-NLS-1$ //$NON-NLS-2$
    + "(?!" + VERSION + ").jar" // negative look-ahead for VERSION unsures we get the right parts //$NON-NLS-1$ //$NON-NLS-2$
    );

  private final String symbolicName;

  private final String version;

  private BundleJarInfo(String jarName, String symbolicName, String version)
  {
    this.jarName = jarName;
    this.symbolicName = symbolicName;
    this.version = version;
  }

  public String getBundleSymbolicName() 
  {
    return symbolicName;
  }
  
  public String getVersion()
  {
    return version; 
  }

  public final String getJarName()
  {
    return jarName;
  }
  
  
}
