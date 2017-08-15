/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
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

package org.eclipse.jet.internal.runtime;


import java.text.MessageFormat;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jet.BufferedJET2Writer;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.FunctionTag;


/**
 * Safe wrapper for {@link FunctionTag}.
 *
 */
public class SafeFunctionRuntimeTag extends SafeCustomRuntimeTag
{

  private boolean bodyProcessed = false;

  /**
   * Used exclusively by {@link #handleBodyContent(JET2Writer)} to overcome restrictions
   * of using a nested (and anonymous) class.
   */
  private String functionResult;

  public SafeFunctionRuntimeTag(FunctionTag tag)
  {
    super(tag);
  }

  public void doStart(JET2Context startContext, JET2Writer startOut)
  {
    super.doStart(startContext, startOut);

    bodyProcessed = false;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.RuntimeTagElement#okToProcessBody()
   */
  public boolean okToProcessBody()
  {
    return !bodyProcessed;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.RuntimeTagElement#handleBodyContent(org.eclipse.jet.JET2Writer)
   */
  public void handleBodyContent(final JET2Writer bodyContent)
  {
    bodyProcessed = true;

    /* Note: functionResult is a field rather than a local to 
     * avoid compiler restrictions on setting local variables within a nested class.
     */
    functionResult = ""; //$NON-NLS-1$
    Platform.run(new TagSafeRunnable()
      {

        public void doRun() throws Exception
        {
          FunctionTag untrustedTag = (FunctionTag)getUntrustedTag();
          functionResult = untrustedTag.doFunction(getTagInfo(), getContext(), ((BufferedJET2Writer)bodyContent).getContent());
        }
      });
    RuntimeLoggerContextExtender.log(getContext(), MessageFormat.format(
      JET2Messages.TraceRuntimeTags_FunctionResult,
      new Object []{ functionResult }), getTagInfo(), RuntimeLoggerContextExtender.TRACE_LEVEL);

    getWriter().write(functionResult);
  }

}
