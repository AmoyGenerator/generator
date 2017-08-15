/**
 * <copyright>
 *
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 *
 * </copyright>
 *
 * $Id: XPathFunctionLazyLoadWrapper.java,v 1.2 2008/09/12 12:56:28 pelder Exp $
 */
package org.eclipse.jet.internal.extensionpoints;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.xpath.XPathFunction;
import org.eclipse.osgi.util.NLS;

/**
 * @author pelder
 */
public class XPathFunctionLazyLoadWrapper implements XPathFunction
{

  private final IConfigurationElement configElement;
  private final String classAttributeName;
  private final String name;

  public XPathFunctionLazyLoadWrapper(String name, IConfigurationElement configElement, String classAttributeName)
  {
    this.name = name;
    this.configElement = configElement;
    this.classAttributeName = classAttributeName;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathFunction#evaluate(java.util.List)
   */
  public Object evaluate(List args)
  {
    throw new IllegalStateException();
  }

  public XPathFunction resolveFunction()
  {
    if(XPathFunctionsManager.DEBUG) {
      System.out.println(NLS.bind("Resolving function {0}", name)); //$NON-NLS-1$
    }
    try
    {
      Object function = configElement.createExecutableExtension(classAttributeName);
      if(function instanceof XPathFunction) {
        return (XPathFunction)function;
      } else {
        InternalJET2Platform.log(new Status(IStatus.ERROR, configElement.getContributor().getName(),
          NLS.bind(Messages.XPathFunctionLazyLoadWrapper_XPathFunctionNotImplemented,
            function.getClass().getName(), name)));
        return new XPathFunctionNotImplementedWrapper(name, 
          configElement.getContributor().getName(),
          function.getClass().getName());
      }
    }
    catch (InvalidRegistryObjectException e)
    {
      InternalJET2Platform.getDefault().log(e);
      return new StaleXPathFunctionWrapper(name);
    }
    catch (CoreException e)
    {
      InternalJET2Platform.getDefault().log(e);
      return new UnableToLoadXPathFunctionWrapper(name, configElement);
    }
  }

}
