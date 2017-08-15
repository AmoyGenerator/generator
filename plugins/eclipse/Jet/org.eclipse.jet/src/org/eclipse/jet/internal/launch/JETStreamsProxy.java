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
package org.eclipse.jet.internal.launch;

import java.io.IOException;

import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.internal.runtime.RuntimeLoggerContextExtender;

/**
 * Implement IStreamsProxy for JET transforms.
 */
public class JETStreamsProxy implements IStreamsProxy
{

  public static String JET_WARNING_STREAM = "org.eclipse.jet.warningStream"; //$NON-NLS-1$
  public static String JET_TRACE_STREAM = "org.eclipse.jet.traceStream"; //$NON-NLS-1$
  public static String JET_DEBUG_STREAM = "org.eclipse.jet.debugStream"; //$NON-NLS-1$
  
  private JETStreamMonitor outputStreamMonitor = null;
  private JETStreamMonitor errorStreamMonitor = null;
  private JETStreamMonitor warningStreamMonitor = null;
  private JETStreamMonitor traceStreamMonitor = null;
  private JETStreamMonitor debugStreamMonitor = null;

  /**
   * @param context 
   * @param levelFilter TODO
   * 
   */
  public JETStreamsProxy(JET2Context context, int levelFilter)
  {
    super();
    this.outputStreamMonitor = createLogger(context, RuntimeLoggerContextExtender.INFO_LEVEL, levelFilter);
    this.errorStreamMonitor = createLogger(context, RuntimeLoggerContextExtender.ERROR_LEVEL, levelFilter);
    this.warningStreamMonitor = createLogger(context, RuntimeLoggerContextExtender.WARNING_LEVEL, levelFilter);
    this.traceStreamMonitor = createLogger(context, RuntimeLoggerContextExtender.TRACE_LEVEL, levelFilter);
    this.debugStreamMonitor = createLogger(context, RuntimeLoggerContextExtender.DEBUG_LEVEL, levelFilter);
  }

  private JETStreamMonitor createLogger(JET2Context context, int level, int minLevel)
  {
    return (level >= minLevel) ? new JETStreamMonitor(context, level) : null;
  }
  /* (non-Javadoc)
   * @see org.eclipse.debug.core.model.IStreamsProxy#getErrorStreamMonitor()
   */
  public IStreamMonitor getErrorStreamMonitor()
  {
    return errorStreamMonitor;
  }

  /* (non-Javadoc)
   * @see org.eclipse.debug.core.model.IStreamsProxy#getOutputStreamMonitor()
   */
  public IStreamMonitor getOutputStreamMonitor()
  {
    return outputStreamMonitor;
  }

  /* (non-Javadoc)
   * @see org.eclipse.debug.core.model.IStreamsProxy#write(java.lang.String)
   */
  public void write(String input) throws IOException
  {
    outputStreamMonitor.log(input, null, null, RuntimeLoggerContextExtender.INFO_LEVEL);
  }

  public final JETStreamMonitor getDebugStreamMonitor()
  {
    return debugStreamMonitor;
  }

  public final JETStreamMonitor getTraceStreamMonitor()
  {
    return traceStreamMonitor;
  }

  public final JETStreamMonitor getWarningStreamMonitor()
  {
    return warningStreamMonitor;
  }

}
