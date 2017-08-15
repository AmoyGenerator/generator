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
 * $Id: ErrorRedirectingCoreElementDelegate.java,v 1.2 2007/04/12 18:02:43 pelder Exp $
 * /
 *******************************************************************************/
package org.eclipse.jet.internal.core.parser.jasper;

import org.eclipse.jet.core.parser.IProblem;
import org.eclipse.jet.core.parser.ProblemSeverity;

/**
 * A JETCoreElement that delegates its accept the the passed core element, but records any JETException
 * as errors on a JETParseEventListener2.
 */
public class ErrorRedirectingCoreElementDelegate implements JETCoreElement
{

  private final JETCoreElement delegate;

  public ErrorRedirectingCoreElementDelegate(JETCoreElement delegate) {
    this.delegate = delegate;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.parser.JETCoreElement#accept(org.eclipse.jet.internal.parser.JETParseEventListener, org.eclipse.jet.internal.parser.JETReader, org.eclipse.jet.internal.parser.JETParser)
   */
  public boolean accept(JETParseEventListener listener, JETReader reader, JETParser parser)
  {
    boolean isAccepted = true;
    JETMark start = reader.mark();
    try {
      isAccepted = delegate.accept(listener, reader, parser);
    }
    catch(JETException e) {
      if(listener instanceof JETParseEventListener2)
      {
        JETParseEventListener2 listener2 = (JETParseEventListener2)listener;
        listener2.recordProblem(ProblemSeverity.ERROR, IProblem.JETException, e.getLocalizedMessage(), new Object[0], 
          start.getCursor(), reader.mark().getCursor(), start.getLine(), start.getCol());
      }
    }
    return isAccepted;
  }

}
