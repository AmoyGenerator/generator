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
import org.eclipse.jet.taglib.EmptyTag;


/**
 * Safe Wrapper and RuntimeTagElement adapter for {@link EmptyTag}.
 *
 */
public class SafeEmptyRuntimeTag extends SafeCustomRuntimeTag
{

  public SafeEmptyRuntimeTag(EmptyTag tag)
  {
    super(tag);
  }

  public void doStart(JET2Context startContext, JET2Writer startOut)
  {
    super.doStart(startContext, startOut);

    Platform.run(new TagSafeRunnable()
      {

        public void doRun() throws Exception
        {
          EmptyTag untrustedTag = (EmptyTag)getUntrustedTag();
          untrustedTag.doAction(getTagInfo(), getContext(), getWriter());
        }

      });
    RuntimeLoggerContextExtender.log(getContext(), JET2Messages.TraceRuntimeTags_ActionCompleted, getTagInfo(), RuntimeLoggerContextExtender.TRACE_LEVEL);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.RuntimeTagElement#okToProcessBody()
   */
  public boolean okToProcessBody()
  {
    return false; // will never get here
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.RuntimeTagElement#handleBodyContent(org.eclipse.jet.JET2Writer)
   */
  public void handleBodyContent(JET2Writer bodyContent)
  {
    // do nothing - will never get here
  }

}
