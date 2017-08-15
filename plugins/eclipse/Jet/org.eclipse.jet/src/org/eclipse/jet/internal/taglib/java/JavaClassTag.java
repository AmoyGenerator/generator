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
package org.eclipse.jet.internal.taglib.java;


import org.eclipse.jet.taglib.JET2TagException;


/**
 * Implement the standard JET2 Java tag 'class'.
 */
public class JavaClassTag extends AbstractJavaFileTag
{

  /**
   * 
   */
  public JavaClassTag()
  {
    super();
  }

  protected String getFileName() throws JET2TagException
  {
    return getAttribute("name") + ".java";  //$NON-NLS-1$//$NON-NLS-2$
  }

 
}
