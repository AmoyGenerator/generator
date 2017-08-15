/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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

package org.eclipse.jet.internal.taglib.java;


import org.eclipse.emf.codegen.util.ImportManager;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.taglib.AbstractEmptyTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.taglib.java.JavaActionsUtil;


/**
 * Implementation of Standard JET2 Java tag 'impliedImport'.
 *
 */
public class ImpliedImportTag extends AbstractEmptyTag
{

  /**
   * 
   */
  public ImpliedImportTag()
  {
    super();
    // TODO Auto-generated constructor stub
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.EmptyTag#doAction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAction(TagInfo tc, JET2Context context, JET2Writer out) throws JET2TagException
  {
    String name = getAttribute("name"); //$NON-NLS-1$
    ImportManager importManager = JavaActionsUtil.getImportManager(getOut());
    importManager.addPseudoImport(name);
  }

}
