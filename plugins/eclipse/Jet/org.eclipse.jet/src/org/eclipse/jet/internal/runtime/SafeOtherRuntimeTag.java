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
import org.eclipse.jet.taglib.OtherTag;


/**
 * Safe wrapper and RuntimeTagElement adapter for {@link OtherTag}.
 *
 */
public class SafeOtherRuntimeTag extends SafeCustomRuntimeTag
{

  //	private final OtherTag untrustedTag;
  private boolean okToProcessBodyResult;

  public SafeOtherRuntimeTag(OtherTag tag)
  {
    super(tag);
    //		this.untrustedTag = tag;
  }

  public void doStart(JET2Context startContext, JET2Writer startOut)
  {
    super.doStart(startContext, startOut);

    Platform.run(new TagSafeRunnable()
      {

        public void doRun() throws Exception
        {
          OtherTag untrustedTag = (OtherTag)getUntrustedTag();
          untrustedTag.doStart(getTagInfo(), getContext(), getWriter());
        }
      });
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.RuntimeTagElement#doEnd()
   */
  public void doEnd()
  {
    Platform.run(new TagSafeRunnable()
      {

        public void doRun() throws Exception
        {
          OtherTag untrustedTag = (OtherTag)getUntrustedTag();
          untrustedTag.doEnd(getTagInfo(), getContext(), getWriter());
        }
      });
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.RuntimeTagElement#okToProcessBody()
   */
  public boolean okToProcessBody()
  {
    okToProcessBodyResult = false;
    Platform.run(new TagSafeRunnable()
      {

        public void doRun() throws Exception
        {
          OtherTag untrustedTag = (OtherTag)getUntrustedTag();
          okToProcessBodyResult = untrustedTag.okToProcessBody(getTagInfo(), getContext());
        }
      });
    return okToProcessBodyResult;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.runtime.RuntimeTagElement#handleBodyContent(org.eclipse.jet.JET2Writer)
   */
  public void handleBodyContent(final JET2Writer bodyContent)
  {
    Platform.run(new TagSafeRunnable()
      {

        public void doRun() throws Exception
        {
          OtherTag untrustedTag = (OtherTag)getUntrustedTag();
          untrustedTag.handleBodyContent(getTagInfo(), getContext(), getWriter(), bodyContent);
        }
      });
  }

}
