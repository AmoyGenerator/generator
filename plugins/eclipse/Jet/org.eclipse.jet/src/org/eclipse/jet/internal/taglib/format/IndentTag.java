/**
 * <copyright>
 *
 * Copyright (c) 2008, 2009 IBM Corporation and others.
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
 * $Id: IndentTag.java,v 1.3 2009/04/13 17:22:07 pelder Exp $
 */
package org.eclipse.jet.internal.taglib.format;

import org.eclipse.jet.BufferedJET2Writer;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.internal.taglib.DocumentHelper;
import org.eclipse.jet.taglib.AbstractContainerTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.osgi.util.NLS;

/**
 * Implement &lt;f:indent text="..." [depth="n"]&gt;content&lt;/f:indent&gt;
 */
public class IndentTag extends AbstractContainerTag
{

  private static final String TEXT_ATTR = "text"; //$NON-NLS-1$
  private static final String DEPTH_ATTR = "depth"; //$NON-NLS-1$
  
  public void doBeforeBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    // do nothing
  }
  

  public void setBodyContent(JET2Writer bodyContent)
  {
    final String indentText = getAttribute(TEXT_ATTR);
    final String depthValue = getAttribute(DEPTH_ATTR);
    try
    {
      final int depth = depthValue == null ? 1 : Integer.decode(depthValue).intValue();
      DocumentHelper.indent((BufferedJET2Writer)bodyContent, depth, indentText);
      
    }
    catch (NumberFormatException e)
    {
      throw new JET2TagException(NLS.bind(JET2Messages.IndentTag_AttributeValueMustBeInteger, DEPTH_ATTR, depthValue));
    }
    finally {
      super.setBodyContent(bodyContent);
    }
  }
  
  public void doAfterBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    // do nothing
  }


}
