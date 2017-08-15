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

package org.eclipse.jet.internal.runtime;


import org.eclipse.core.runtime.Platform;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.ConditionalTag;
import org.eclipse.jet.taglib.RuntimeTagElement;


/**
 * A safe (throws no exceptions) adaptor of a {@link ConditionalTag} to
 * a {@link RuntimeTagElement}.
 */
public class SafeConditionalRuntimeTag extends SafeContainerRuntimeTag implements RuntimeTagElement 
{

  private boolean doEvalResult;

  /**
   * @param tag the conditional tag to wrap
   * 
   */
  public SafeConditionalRuntimeTag(ConditionalTag tag)
  {
    super(tag);
  }

  public void doStart(JET2Context startContext, JET2Writer startOut)
  {
    super.doStart(startContext, startOut);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.RuntimeTagElement#okToProcessBody()
   */
  public boolean okToProcessBody()
  {
    okToProcessBodyCalled = true;
    if (bodyProcessed)
    {
      return false;
    }

    doEvalResult = false;

    Platform.run(new TagSafeRunnable()
      {
        public void doRun() throws Exception
        {
          ConditionalTag untrustedTag = (ConditionalTag)getUntrustedTag();
          doEvalResult = untrustedTag.doEvalCondition(getTagInfo(), getContext());
          if (doEvalResult)
          {
            untrustedTag.doBeforeBody(getTagInfo(), getContext(), getWriter());
          }
        }
      });
    RuntimeLoggerContextExtender.log(getContext(), (doEvalResult ? JET2Messages.TraceRuntimeTags_ProcessingBody : JET2Messages.TraceRuntimeTags_SkippingBody), getTagInfo(), RuntimeLoggerContextExtender.TRACE_LEVEL);

    return doEvalResult;
  }

}
