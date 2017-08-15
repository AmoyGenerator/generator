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
 * $Id: ParamTag.java,v 1.1 2009/03/06 20:53:09 pelder Exp $
 */
package org.eclipse.jet.internal.taglib.format;

import org.eclipse.jet.BufferedJET2Writer;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.taglib.AbstractContainerTag;
import org.eclipse.jet.taglib.CustomTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.osgi.util.NLS;

/**
 * Implement &lt;f:param [value="...value..."]&gt; tag
 */
public class ParamTag extends AbstractContainerTag
{

  private String value;

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doBeforeBody(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doBeforeBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    value = getAttribute("value"); //$NON-NLS-1$
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.AbstractContainerTag#setBodyContent(org.eclipse.jet.JET2Writer)
   */
  public void setBodyContent(JET2Writer bodyContent)
  {
    // don't write body content...
    if(value == null && bodyContent instanceof BufferedJET2Writer) {
      value = ((BufferedJET2Writer)bodyContent).getContent();
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.ContainerTag#doAfterBody(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAfterBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    final CustomTag parent = getParent();
    if(!(parent instanceof MessageTag)) {
      throw new JET2TagException(NLS.bind(Messages.ParamTag_MustBeContainedByMessageTag,
        getMessageTagName(td)));
    }
    if(value == null) {
      throw new JET2TagException(Messages.ParamTag_NoValueSpecified);
    }
    
    MessageTag messageTag = (MessageTag)parent;
    messageTag.addParam(value);
  }

  private String getMessageTagName(TagInfo td)
  {
    final StringBuffer name = new StringBuffer(20);
    name.append('<');
    final String paramTagName = td.getTagName();
    final int colonPos = paramTagName.indexOf(':');
    if(colonPos > 0) {
      // the prefix...
      name.append(paramTagName.substring(0, colonPos));
      name.append(':');
    }
    name.append("message"); //$NON-NLS-1$
    name.append('>');
    return name.toString();
  }

}
