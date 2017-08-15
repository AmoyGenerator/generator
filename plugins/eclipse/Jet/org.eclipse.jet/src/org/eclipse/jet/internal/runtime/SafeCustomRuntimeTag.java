/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.CustomTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.RuntimeTagElement;
import org.eclipse.jet.taglib.TagInfo;


/**
 * Abstract implementation of a Safe handler. A Safe tag handler wraps a JET2 tag and catches
 * and appropriately logs all runtime exceptions.
 * <p>
 * This class is used by the JET2 compiler. It is not intended to be subclassed by clients.
 *
 */
public abstract class SafeCustomRuntimeTag implements RuntimeTagElement
{

  protected abstract class TagSafeRunnable implements ISafeRunnable
  {

    public void handleException(Throwable exception)
    {
      
      if(exception instanceof OperationCanceledException) 
      {
        throw ((OperationCanceledException) exception);
      } 
      else if (exception instanceof RuntimeException && !(exception instanceof JET2TagException))
      {
        // we make the assumption that runtime exceptions we're unintended results of
        // tag execution, and that a clearer message should be issued.
        String msg = MessageFormat.format(JET2Messages.SafeCustomRuntimeTag_ErrorExecutingHandler, new Object []{ exception.toString() });
        context.logError(tagInfo, msg, exception);

        // check to see if we're a runtime workbench. If so, dump the exception to the
        // standard output to aid debugging.
        if (Platform.inDevelopmentMode())
        {
          exception.printStackTrace();
        }
      }
      else
      {
        context.logError(tagInfo, null, exception);
      }
    }

    public final void run() throws Exception
    {
      try {
        doRun();
      }
      catch(JET2TagException e)
      {
        e.printStackTrace();
        handleException(e);
      }
    }

    protected abstract void doRun() throws Exception;
  }

  private RuntimeTagElement parentTag;

  private TagInfo tagInfo;

  private JET2Context context;

  private JET2Writer out;

  private final CustomTag untrustedTag;

  public SafeCustomRuntimeTag(CustomTag tag)
  {
    this.untrustedTag = tag;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.RuntimeTagElement#setParent(org.eclipse.jet.taglib.RuntimeTagElement)
   */
  public final void setRuntimeParent(RuntimeTagElement parentTag)
  {
    this.parentTag = parentTag;
    final CustomTag customParent;
    if (parentTag instanceof SafeCustomRuntimeTag)
    {
      customParent = ((SafeCustomRuntimeTag)parentTag).untrustedTag;
    }
    else
    {
      customParent = null;
    }

    SafeRunner.run(new TagSafeRunnable()
      {

        public void doRun() throws Exception
        {
          untrustedTag.setParent(customParent);

        }
      });
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.RuntimeTagElement#getParent()
   */
  public final RuntimeTagElement getParent()
  {
    return parentTag;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.RuntimeTagElement#setTagInfo(org.eclipse.jet.taglib.TagInfo)
   */
  public final void setTagInfo(TagInfo tagInfo)
  {
    this.tagInfo = tagInfo;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.RuntimeTagElement#getTagInfo()
   */
  public final TagInfo getTagInfo()
  {
    return tagInfo;
  }

  public void doStart(JET2Context startContext, JET2Writer startOut)
  {
    //		System.out.println("Start: " + tagInfo);
    this.context = startContext;
    this.out = startOut;
    untrustedTag.setTagInfo(tagInfo);
    untrustedTag.setContext(context);
    untrustedTag.setOut(out);

  }

  public void doEnd()
  {
    //		System.out.println("End: " + tagInfo);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.RuntimeTagElement#getContext()
   */
  public final JET2Context getContext()
  {
    return context;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.RuntimeTagElement#getWriter()
   */
  public final JET2Writer getWriter()
  {
    return out;
  }

  protected final CustomTag getUntrustedTag()
  {
    return untrustedTag;
  }
}
