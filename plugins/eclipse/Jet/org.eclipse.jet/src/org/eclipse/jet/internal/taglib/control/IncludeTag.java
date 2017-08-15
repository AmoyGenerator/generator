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
package org.eclipse.jet.internal.taglib.control;


import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jet.CoreJETException;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.JETGenerate;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.AbstractEmptyTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.transform.TransformContextExtender;


/**
 * Implement the standard JET2 tag 'include'.
 *
 */
public class IncludeTag extends AbstractEmptyTag
{

  /**
   * 
   */
  public IncludeTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.EmptyTag#doAction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAction(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    String template = getAttribute("template"); //$NON-NLS-1$
    String restoreNames = getAttribute("restoreNames"); //$NON-NLS-1$
    String passVariables = getAttribute("passVariables"); //$NON-NLS-1$
    
    String useSuperStr = getAttribute("super"); //$NON-NLS-1$
    boolean useSuper = useSuperStr != null && Boolean.valueOf(useSuperStr).booleanValue();

    if(restoreNames != null && passVariables != null)
    {
      throw new JET2TagException(MessageFormat.format(JET2Messages.AnyTag_MutuallyExclusiveAttributes,
          new Object[] {"restoreNames", "passVariables"}));  //$NON-NLS-1$//$NON-NLS-2$
    }
    
    Map savedVariableValues;
    if(passVariables != null)
    {
      savedVariableValues = context.getVariables();
      context.setVariables(context.extractVariables(passVariables));
    }
    else
    {
      savedVariableValues = context.extractVariables(restoreNames);
    }

    try 
    {   
      JETGenerate.generate(template, context, out);
      TransformContextExtender tce = new TransformContextExtender(context);
      tce.execute(template, useSuper, out);
          
    }
    catch (Exception e)
    {
      
    } 
    finally
    {
      if(restoreNames != null)
      {
        context.restoreVariables(savedVariableValues);
      }
      else if(passVariables != null)
      {
        context.setVariables(savedVariableValues);
      }
    }
  }

}
