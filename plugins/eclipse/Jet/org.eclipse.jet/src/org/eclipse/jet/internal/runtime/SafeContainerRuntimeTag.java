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
import org.eclipse.jet.taglib.ContainerTag;


/**
 * Safe wrapper for {@link ContainerTag}.
 *
 */
public class SafeContainerRuntimeTag extends SafeCustomRuntimeTag
{

  protected boolean bodyProcessed = false;

  protected boolean okToProcessBodyCalled = false;

  /**
   * @param tag
   */
  public SafeContainerRuntimeTag(ContainerTag tag)
  {
    super(tag);
  }

  public void doStart(JET2Context startContext, JET2Writer startOut)
  {
    super.doStart(startContext, startOut);
    bodyProcessed = false;
  }

  public void doEnd()
  {
    if (!okToProcessBodyCalled)
    {
      // this was a container tag that was expressed as an empty tag: <foo/>. Call
      // doAction()...
      Platform.run(new TagSafeRunnable()
        {
          public void doRun() throws Exception
          {
            ContainerTag untrustedTag = (ContainerTag)getUntrustedTag();
            untrustedTag.doAction(getTagInfo(), getContext(), getWriter());
          }
        });
     
      RuntimeLoggerContextExtender.log(getContext(), JET2Messages.TraceRuntimeTags_ActionCompleted, getTagInfo(), RuntimeLoggerContextExtender.TRACE_LEVEL);
    }
    
    Platform.run(new TagSafeRunnable()
    {
      public void doRun() throws Exception
      {
        ContainerTag untrustedTag = (ContainerTag)getUntrustedTag();
        untrustedTag.doEnd(getTagInfo(), getContext(), getWriter());
      }
    });
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.RuntimeTagElement#okToProcessBody()
   */
  public boolean okToProcessBody()
  {
    okToProcessBodyCalled = true;
    if (!bodyProcessed)
    {
      Platform.run(new TagSafeRunnable()
        {
          public void doRun() throws Exception
          {
            ContainerTag untrustedTag = (ContainerTag)getUntrustedTag();
            untrustedTag.doBeforeBody(getTagInfo(), getContext(), getWriter());
          }
        });
      RuntimeLoggerContextExtender.log(getContext(), JET2Messages.TraceRuntimeTags_BeforeBodyCompleted, getTagInfo(), RuntimeLoggerContextExtender.TRACE_LEVEL);
    }
    return !bodyProcessed;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.RuntimeTagElement#handleBodyContent(org.eclipse.jet.JET2Writer)
   */
  public void handleBodyContent(final JET2Writer bodyContent)
  {
    bodyProcessed = true;
    Platform.run(new TagSafeRunnable()
      {
        public void doRun() throws Exception
        {
          ContainerTag untrustedTag = (ContainerTag)getUntrustedTag();
          if (bodyContent != getWriter())
          {
            RuntimeLoggerContextExtender.log(getContext(), 
              MessageFormat.format(
                JET2Messages.TraceRuntimeTags_SetBodyContents, 
                new Object[] {((BufferedJET2Writer)bodyContent).getContent()}),
               getTagInfo(), RuntimeLoggerContextExtender.TRACE_LEVEL);
            untrustedTag.setBodyContent(bodyContent);
          }
          untrustedTag.doAfterBody(getTagInfo(), getContext(), getWriter());
        }
      });
    //        RuntimeLoggerContextExtender.log(getContext(), "exiting setBodyContent()", getTagInfo());
  }
  
}
