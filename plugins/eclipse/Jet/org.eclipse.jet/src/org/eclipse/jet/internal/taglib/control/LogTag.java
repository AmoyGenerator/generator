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


import java.text.MessageFormat;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.AbstractFunctionTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;


/**
 * Implement the Standard JET2 Control tag 'log'.
 *
 */
public class LogTag extends AbstractFunctionTag
{

  /**
   * 
   */
  public LogTag()
  {
    super();
    // TODO Auto-generated constructor stub
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.FunctionTag#doFunction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, java.lang.String)
   */
  public String doFunction(TagInfo td, JET2Context context, String bodyContent) throws JET2TagException
  {
    String severity = getAttribute("severity"); //$NON-NLS-1$
    if ("error".equalsIgnoreCase(severity)) { //$NON-NLS-1$
      context.logError(bodyContent);
    }
    else if ("warning".equalsIgnoreCase(severity)) { //$NON-NLS-1$
      context.logWarning(bodyContent);
    }
    else if ("info".equalsIgnoreCase(severity) || severity == null) { //$NON-NLS-1$
      context.logInfo(bodyContent);
    }
    else
    {
      throw new JET2TagException(MessageFormat.format(JET2Messages.LogTag_BadSeverity, new Object []{ severity }));
    }
    return ""; //$NON-NLS-1$
  }

}
