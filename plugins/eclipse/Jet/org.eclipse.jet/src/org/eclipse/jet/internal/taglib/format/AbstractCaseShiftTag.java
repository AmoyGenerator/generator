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
package org.eclipse.jet.internal.taglib.format;

import java.text.MessageFormat;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.AbstractFunctionTag;
import org.eclipse.jet.taglib.FunctionTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;

/**
 * Abstract tag that serves as common ancester to all case-shifting tags (upper-case, lower-case ...).
 * <p>
 * Each tag is assumed to be a Function tag with two optional attributes 'offset' and 'length'. Implementors
 * must provide an implementation of doCaseShift
 * </p>
 */
public abstract class AbstractCaseShiftTag extends AbstractFunctionTag implements FunctionTag
{

  private static final String LENGTH_ATTR = "length"; //$NON-NLS-1$

  private static final String OFFSET_ATTR = "offset"; //$NON-NLS-1$


  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.FunctionTag#doFunction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, java.lang.String)
   */
  public final String doFunction(TagInfo td, JET2Context context, String bodyContent) throws JET2TagException
  {
    int offset = 0;
    if (td.hasAttribute(OFFSET_ATTR))
    {
      try
      {
        offset = Integer.valueOf(getAttribute(OFFSET_ATTR)).intValue();
        if(offset < 0) 
        {
          throw new JET2TagException(MessageFormat.format(JET2Messages.AnyTag_AttributeMustBeInteger, new Object []{ OFFSET_ATTR }));
        }
      }
      catch (NumberFormatException e)
      {
        throw new JET2TagException(MessageFormat.format(JET2Messages.AnyTag_AttributeMustBeInteger, new Object []{ OFFSET_ATTR }), e);
      }
    }

    int length = bodyContent.length() - offset;
    if (td.hasAttribute(LENGTH_ATTR))
    {
      try
      {
        length = Integer.valueOf(getAttribute(LENGTH_ATTR)).intValue();
        if(length < 0) 
        {
          throw new JET2TagException(MessageFormat.format(JET2Messages.AnyTag_AttributeMustBeInteger, new Object []{ LENGTH_ATTR }));
        }
      }
      catch (NumberFormatException e)
      {
        throw new JET2TagException(MessageFormat.format(JET2Messages.AnyTag_AttributeMustBeInteger, new Object []{ LENGTH_ATTR }), e);
      }
    }

    StringBuffer buffer = new StringBuffer(bodyContent);

    // fix offset and length to avoid out of bounds exceptions
    if(length < 0) length = 0;
    if(length > buffer.length() - offset) length = buffer.length() - offset;
    
    if(offset < buffer.length())
    {
      final String textToCaseShift = bodyContent.substring(offset, offset + length);
      buffer.replace(offset, offset + length, doCaseShift(textToCaseShift));
    }
    return buffer.toString();
  }


  protected abstract String doCaseShift(String textToCaseShift);

}
