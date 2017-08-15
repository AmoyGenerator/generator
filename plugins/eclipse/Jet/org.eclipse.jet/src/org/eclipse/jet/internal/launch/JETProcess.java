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
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.eclipse.jet.CoreJETException;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Platform;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.workspace.WorkspaceContextExtender;
import org.eclipse.jet.transform.IJETBundleDescriptor;

/**
 * Implement IProcess for JET transforms.
 */
public class JETProcess extends PlatformObject implements IProcess
{

  private final String label;
  private final ILaunch launch;
  private boolean terminated = false;
  private final String id;
  private final IResource resource;
  private final IProgressMonitor monitor;
  private JET2Context context;
  private JETStreamsProxy streamsProxy;
  private final Map attributes = new HashMap();

  public JETProcess(ILaunch launch, String id, IResource resource, int logFilterLevel, IProgressMonitor monitor)
  {
    this.label = " - " + DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()); //$NON-NLS-1$
    this.launch = launch;
    this.id = id;
    this.resource = resource;
    this.monitor = monitor;
    
    context = new JET2Context(null);

    streamsProxy = new JETStreamsProxy(context, logFilterLevel);
    
    setAttribute(IProcess.ATTR_PROCESS_TYPE, "org.eclipse.jet.process"); //$NON-NLS-1$

    
  }
  /* (non-Javadoc)
   * @see org.eclipse.debug.core.model.IProcess#getLabel()
   */
  public String getLabel()
  {
    return label;
  }

  /* (non-Javadoc)
   * @see org.eclipse.debug.core.model.IProcess#getLaunch()
   */
  public ILaunch getLaunch()
  {
    return launch;
  }

  /* (non-Javadoc)
   * @see org.eclipse.debug.core.model.IProcess#getStreamsProxy()
   */
  public IStreamsProxy getStreamsProxy()
  {
    return streamsProxy;
  }

  /* (non-Javadoc)
   * @see org.eclipse.debug.core.model.IProcess#setAttribute(java.lang.String, java.lang.String)
   */
  public void setAttribute(String key, String value)
  {
    attributes.put(key, value);
  }

  /* (non-Javadoc)
   * @see org.eclipse.debug.core.model.IProcess#getAttribute(java.lang.String)
   */
  public String getAttribute(String key)
  {
    return (String)attributes.get(key);
  }

  /* (non-Javadoc)
   * @see org.eclipse.debug.core.model.IProcess#getExitValue()
   */
  public int getExitValue() throws DebugException
  {
    return 0;
  }

  /* (non-Javadoc)
   * @see org.eclipse.debug.core.model.ITerminate#canTerminate()
   */
  public boolean canTerminate()
  {
    return false;
  }

  /* (non-Javadoc)
   * @see org.eclipse.debug.core.model.ITerminate#isTerminated()
   */
  public boolean isTerminated()
  {
    return terminated;
  }

  /* (non-Javadoc)
   * @see org.eclipse.debug.core.model.ITerminate#terminate()
   */
  public void terminate() throws DebugException
  {
  }

  public void run() {
    IStatus status;
    try
    {
      final IJETBundleDescriptor descriptor = JET2Platform.getJETBundleManager().getDescriptor(id);
      if(descriptor != null)
      {
        String loaderId = descriptor.getModelLoaderId();
        String fileType = descriptor.getModelExtension();
        
        WorkspaceContextExtender.loadResourceAsSource(context, resource, loaderId, fileType);
        
        status = JET2Platform.runTransform(id, context, monitor);
        context.logInfo(status.getMessage());
      }
      else
      {
        context.logError(MessageFormat.format(JET2Messages.JET2Platform_TransformNotFound, new Object[] {id}));
      }
    }
    catch (IOException e)
    {
      // didn't work.
      status = new Status(IStatus.ERROR, id, IStatus.OK, e.getLocalizedMessage(), e);
      context.logError(e.getLocalizedMessage());
    }
    catch (CoreJETException e)
    {
      status = new Status(IStatus.ERROR, id, IStatus.OK, e.getLocalizedMessage(), e);
      context.logError(e.getLocalizedMessage());
    }
    terminated = true;
    context = null;
     
  }
}
