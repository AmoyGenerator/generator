/*******************************************************************************
 * Copyright (c) 2006, 2009 IBM Corporation and others.
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
package org.eclipse.jet.internal.launch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.internal.runtime.RuntimeLoggerContextExtender;
import org.eclipse.jet.internal.runtime.RuntimeTagLogger;
import org.eclipse.jet.taglib.TagInfo;

/**
 * JET transform implementation of IStreamMonitor.
 */
public class JETStreamMonitor implements IStreamMonitor, RuntimeTagLogger
{
  private final List listeners = new ArrayList();
  
  private final StringBuffer buffer = new StringBuffer();

  private int msgLevel;

  public JETStreamMonitor(JET2Context context, int msgLevel)
  {
    RuntimeLoggerContextExtender.getInstance(context).addListener(this);
    this.msgLevel = msgLevel;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.debug.core.model.IStreamMonitor#addListener(org.eclipse.debug.core.IStreamListener)
   */
  public void addListener(IStreamListener listener)
  {
    if(!listeners.contains(listener))
    {
      listeners.add(listener);
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.debug.core.model.IStreamMonitor#getContents()
   */
  public String getContents()
  {
    return buffer.toString();
  }

  /* (non-Javadoc)
   * @see org.eclipse.debug.core.model.IStreamMonitor#removeListener(org.eclipse.debug.core.IStreamListener)
   */
  public void removeListener(IStreamListener listener)
  {
    listeners.remove(listener);
  }

  public void log(String message, TagInfo td, String templatePath, int level)
  {
    if(level != msgLevel)
    {
      return;
    }
    
    String msgPrefix = ""; //$NON-NLS-1$
    switch(level)
    {
      case RuntimeLoggerContextExtender.ERROR_LEVEL:
        msgPrefix = JET2Messages.JETStreamMonitor_Error + ": "; //$NON-NLS-1$
        break;
      case RuntimeLoggerContextExtender.WARNING_LEVEL:
        msgPrefix = JET2Messages.JETStreamMonitor_Warning + ": "; //$NON-NLS-1$
        break;
      case RuntimeLoggerContextExtender.INFO_LEVEL:
        // no prefix for info messages
        break;
      case RuntimeLoggerContextExtender.TRACE_LEVEL:
        msgPrefix = JET2Messages.JETStreamMonitor_Trace + ": "; //$NON-NLS-1$
        break;
      case RuntimeLoggerContextExtender.DEBUG_LEVEL:
        msgPrefix = JET2Messages.JETStreamMonitor_Debug + ": "; //$NON-NLS-1$
        break;
    }
    String formattedMsg = formatMessage(msgPrefix + message, td, templatePath);
    
    for (Iterator i = listeners.iterator(); i.hasNext();)
    {
      IStreamListener listener = (IStreamListener)i.next();
      listener.streamAppended(formattedMsg, this);
    }
  }

  /**
   * @param message
   * @param td
   * @param templatePath
   * @return
   */
  private String formatMessage(String message, TagInfo td, String templatePath)
  {
    StringBuffer msg = new StringBuffer();
    if(templatePath != null && templatePath.length() > 0)
    {
      msg.append(templatePath);
      if(td != null)
      {
        msg.append('(').append(td.getLine()).append(',').append(td.getCol()).append(')');
      }
      msg.append(": "); //$NON-NLS-1$
      if(td != null)
      {
        msg.append(" ").append(td.displayString()); //$NON-NLS-1$
      }
      msg.append(System.getProperty("line.separator")); //$NON-NLS-1$
      msg.append("    "); //$NON-NLS-1$
    }
    msg.append(message);
    msg.append(System.getProperty("line.separator")); //$NON-NLS-1$
    String finalMsg = msg.toString();
    return finalMsg;
  }

}
