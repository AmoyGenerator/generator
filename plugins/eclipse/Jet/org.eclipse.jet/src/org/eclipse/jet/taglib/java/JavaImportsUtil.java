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
package org.eclipse.jet.taglib.java;



import org.eclipse.emf.codegen.util.ImportManager;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.taglib.JET2TagException;


/**
 * Utility function for sharing access to {@link ImportManager} created by the &lt;java:importsManager;&gt; tags.
 * <p>
 * This class is not intended to be instantiated or extended by clients.
 * </p>
 */
public final class JavaImportsUtil
{

  /**
   * 
   */
  private JavaImportsUtil()
  {
    super();
  }

  /**
   * Return the imports manager installed in the current writer.
   * @param writer the current writer. Cannot be <code>null</code>.
   * @return the imports manager installed on the writer.
   * @throws JET2TagException if no &lt;java:importsLocation&gt; tag has executed on the writer.
   * @throws NullPointerException if <code>writer</code> is <code>null</code>.
   * @deprecated Use {@link JavaActionsUtil#getImportManager(JET2Writer)} instead
   */
  public static ImportManager getImportManager(JET2Writer writer) throws JET2TagException
  {
    return JavaActionsUtil.getImportManager(writer);
  }

}
