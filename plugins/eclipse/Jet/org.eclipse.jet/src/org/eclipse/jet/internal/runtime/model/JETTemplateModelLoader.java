/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
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
 * /
 *******************************************************************************/
package org.eclipse.jet.internal.runtime.model;

import java.io.IOException;
import java.net.URL;

import org.eclipse.jet.core.parser.ast.JETAST;
import org.eclipse.jet.core.parser.ast.JETASTParser;
import org.eclipse.jet.core.parser.ast.JETCompilationUnit;
import org.eclipse.jet.runtime.model.IModelLoader;

/**
 * Implement the model loader for JET templates
 */
public class JETTemplateModelLoader implements IModelLoader
{

  /**
   * 
   */
  public JETTemplateModelLoader()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.runtime.model.IModelLoader#load(java.net.URL)
   */
  public Object load(URL modelUrl) throws IOException
  {
    return load(modelUrl, "jet"); //$NON-NLS-1$
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.runtime.model.IModelLoader#load(java.net.URL, java.lang.String)
   */
  public Object load(URL modelUrl, String kind) throws IOException
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.runtime.model.IModelLoader#loadFromString(java.lang.String, java.lang.String)
   */
  public Object loadFromString(String serializedModel, String kind) throws IOException
  {
    JETCompilationUnit cu = (JETCompilationUnit)new JETASTParser.Builder(JETAST.JET_SPEC_V2)
      .build()
      .parse(serializedModel.toCharArray());
    return cu;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.runtime.model.IModelLoader#canLoad(java.lang.String)
   */
  public boolean canLoad(String kind)
  {
    throw new UnsupportedOperationException();
  }

}
