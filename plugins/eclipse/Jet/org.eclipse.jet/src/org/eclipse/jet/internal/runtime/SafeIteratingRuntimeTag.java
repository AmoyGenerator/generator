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
import org.eclipse.jet.taglib.IteratingTag;


/**
 * Implement a Safe wrapper on IteratingTag.
 *
 */
public class SafeIteratingRuntimeTag extends SafeContainerRuntimeTag
{

  /**
   * This field is used exclusively by {@link #okToProcessBody()} to circumvent
   * compiler restrictions on access to non-final local variables by nested
   * anonymous classes.
   */
  private boolean okToProcessBodyResult;

  /**
   * This field is used to track whether {@link IteratingTag#doInitializeLoop(TagInfo, JET2Context)}
   * successfully completes (i.e. does not through an exception). If an exception is thrown
   * then {@link IteratingTag#doEvalLoopCondition(TagInfo, JET2Context)} is never
   * called, and the tag's contents are never processed. 
   */
  private boolean loopInitializedOk;

  public SafeIteratingRuntimeTag(IteratingTag tag)
  {
    super(tag);
  }

  public void doStart(JET2Context startContext, JET2Writer startOut)
  {
    super.doStart(startContext, startOut);

    loopInitializedOk = false;
    Platform.run(new TagSafeRunnable()
      {

        public void doRun() throws Exception
        {
          IteratingTag untrustedTag = (IteratingTag)getUntrustedTag();
          untrustedTag.doInitializeLoop(getTagInfo(), getContext());
          loopInitializedOk = true;
        }
      });
    RuntimeLoggerContextExtender.log(getContext(), JET2Messages.TraceRuntimeTags_LoopInitialized, getTagInfo(), RuntimeLoggerContextExtender.TRACE_LEVEL);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.RuntimeTagElement#okToProcessBody()
   */
  public boolean okToProcessBody()
  {
    okToProcessBodyCalled = true;
    if (!loopInitializedOk)
    {
      return false;
    }
    // Note: See not on this field declaration. This cannot be a local field
    okToProcessBodyResult = false;
    Platform.run(new TagSafeRunnable()
      {

        public void doRun() throws Exception
        {
          IteratingTag untrustedTag = (IteratingTag)getUntrustedTag();
          okToProcessBodyResult = untrustedTag.doEvalLoopCondition(getTagInfo(), getContext());
          if (okToProcessBodyResult)
          {
            untrustedTag.doBeforeBody(getTagInfo(), getContext(), getWriter());
          }
        }
      });
    RuntimeLoggerContextExtender.log(getContext(), okToProcessBodyResult ? JET2Messages.TraceRuntimeTags_ProcessingLoopBody : JET2Messages.TraceRuntimeTags_LoopFinished, getTagInfo(), RuntimeLoggerContextExtender.TRACE_LEVEL);
    return okToProcessBodyResult;
  }

}
