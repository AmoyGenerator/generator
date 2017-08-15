/**
 * <copyright>
 *
 * Copyright (c) 2009 IBM Corporation and others.
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
 * $Id: BundleTag.java,v 1.1 2009/03/06 20:53:09 pelder Exp $
 */
package org.eclipse.jet.internal.taglib.format;

import java.util.ResourceBundle;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.taglib.AbstractContainerTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;

/**
 * Implement &lt;f:bundle&;gt> tag
 */
public class BundleTag extends AbstractContainerTag
{

  private ResourceBundle bundle = null;

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doBeforeBody(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doBeforeBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    final String baseName = getAttribute("basename"); //$NON-NLS-1$
    bundle = ResourceBundleUtil.loadBundle(context, baseName);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doAfterBody(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAfterBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    // increase changes bundle gets GC'd quickly
    bundle = null;
  }

  public final ResourceBundle getBundle()
  {
    return bundle;
  }

  
}
