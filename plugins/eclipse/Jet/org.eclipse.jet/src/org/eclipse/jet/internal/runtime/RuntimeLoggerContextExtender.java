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
 * $Id$
 * /
 *******************************************************************************/
package org.eclipse.jet.internal.runtime;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.jet.ContextLogEntry;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.taglib.TagInfo;


/**
 * A JET2Context extender that may be installed for logging runtime tag activity.
 */
public final class RuntimeLoggerContextExtender
{
  private static String PRIVATE_CONTEXT_DATA_KEY = RuntimeLoggerContextExtender.class.getName();

  private static interface LogState {
    void log(String message, TagInfo td, String templatePath, int level);
  }
  
  private static final LogState NO_LISTENERS = new LogState() {

    public void log(String message, TagInfo td, String templatePath, int level)
    {
    }
    
  };
  
  private static final class OneListenerState implements LogState {

    private final RuntimeTagLogger runtimeTagLogger;

    public OneListenerState(RuntimeTagLogger runtimeTagLogger) {
      this.runtimeTagLogger = runtimeTagLogger;
      
    }
    
    public void log(String message, TagInfo td, String templatePath, int level)
    {
      runtimeTagLogger.log(message, td, templatePath, level);
    }
  }
  
  private static final class MultipleListenerState implements LogState {

    private final RuntimeTagLogger[] listeners;
    private int listenersLength;
    public MultipleListenerState(List listeners) {
      this.listeners = (RuntimeTagLogger[])listeners.toArray(new RuntimeTagLogger[listeners.size()]);
      this.listenersLength = this.listeners.length;
    }
    
    public void log(String message, TagInfo td, String templatePath, int level)
    {
      for (int i = 0; i < listenersLength; i++)
      {
        listeners[i].log(message, td, templatePath, level);
      }
    }
    
  }

  public static final int ERROR_LEVEL = 5;

  public static final int WARNING_LEVEL = 4;

  public static final int INFO_LEVEL = 3;

  public static final int TRACE_LEVEL = 2;

  public static final int DEBUG_LEVEL = 1;


  private final ContextData contextData;

  private static final class ContextData implements JET2Context.LogListener
  {
    private final List listeners = new ArrayList();
    private LogState state = NO_LISTENERS;

    public void log(String message, TagInfo td, String templatePath, int level)
    {
      state.log(message, td, templatePath, level);
    }

    public void log(ContextLogEntry entry)
    {
      StringBuffer logMessage = new StringBuffer(entry.getMessage());
      final ContextLogEntry[] children = entry.getChildren();
      for (int i = 0; i < children.length; i++)
      {
        appendChildMessage(logMessage, children[i], 1);
      }
      state.log(logMessage.toString(), entry.getTagInfo(), entry.getTemplatePath(), getLevel(entry.getSeverity()));
    }

    private void appendChildMessage(StringBuffer logMessage, ContextLogEntry entry, int depth)
    {
      if(logMessage.charAt(logMessage.length() - 1) != '\n') {
        logMessage.append('\n');
      }
      for (int i = 0; i < depth; i++)
      {
        logMessage.append('\t');
      }
      logMessage.append(entry.getMessage());
      final ContextLogEntry[] children = entry.getChildren();
      for (int i = 0; i < children.length; i++)
      {
        appendChildMessage(logMessage, children[i], depth + 1);
      }
      
    }

    private int getLevel(int severity)
    {
      switch (severity)
      {
        case ContextLogEntry.ERROR:
        case ContextLogEntry.CANCEL:
          return RuntimeLoggerContextExtender.ERROR_LEVEL;
        case ContextLogEntry.WARNING:
          return RuntimeLoggerContextExtender.WARNING_LEVEL;
        case ContextLogEntry.INFO:
        case ContextLogEntry.OK:
          return RuntimeLoggerContextExtender.INFO_LEVEL;
        default:
          return RuntimeLoggerContextExtender.ERROR_LEVEL;

      }
      
    }
    
    public void addListener(RuntimeTagLogger tagLogger)
    {
      listeners.add(tagLogger);
      updateState();
    }

    public void removeListener(RuntimeTagLogger tagLogger)
    {
      listeners.remove(tagLogger);
      updateState();
    }

      

    /**
     * 
     */
    public void updateState()
    {
      switch(listeners.size()) {
        case 0:
          state = NO_LISTENERS;
          break;
        case 1:
          state = new OneListenerState((RuntimeTagLogger)listeners.get(0));
          break;
        default:
          state = new MultipleListenerState(listeners);
          break;
      }
    }
  }

  /**
   * Get the runtime contextData instance.
   * @param context the context
   * @return the installed context extender or <code>null</code>
   */
  public static RuntimeLoggerContextExtender getInstance(JET2Context context)
  {
    if(context == null) {
      throw new NullPointerException();
    }
    RuntimeLoggerContextExtender ex = (RuntimeLoggerContextExtender)context.getPrivateData(PRIVATE_CONTEXT_DATA_KEY);
    if (ex == null)
    {
      ex = new RuntimeLoggerContextExtender(context, new ContextData());
      context.addPrivateData(PRIVATE_CONTEXT_DATA_KEY, ex);
    }
    return ex;
  }

  private RuntimeLoggerContextExtender(JET2Context context, ContextData contextData)
  {
    context.addLogListener(contextData);
    this.contextData = contextData;
  }

  public void addListener(RuntimeTagLogger tagLogger)
  {
    contextData.addListener(tagLogger);
  }

  public void removeListener(RuntimeTagLogger tagLogger)
  {
    contextData.removeListener(tagLogger);
  }

  public static void log(JET2Context context, String message, TagInfo td, int level)
  {
    getInstance(context).contextData.log(message, td, context.getTemplatePath(), level);
  }

  public static void log(JET2Context context, String message, TagInfo tagInfo, String templatePath, int level)
  {
    getInstance(context).contextData.log(message, tagInfo, templatePath, level);
  }
}
