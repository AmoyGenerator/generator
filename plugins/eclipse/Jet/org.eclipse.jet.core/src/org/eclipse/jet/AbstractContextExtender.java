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
 * $Id: AbstractContextExtender.java,v 1.4 2007/11/29 21:37:21 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet;



/**
 * Base class for extending {@link JET2Context}. The JET execution context, {@link JET2Context}, is capable of
 * storing private data for various extensions. Subclass this class to create such a private data extension. Steps to create an context
 * extension.
 * <ol>
 * <li>Subclass this class</li>
 * <li>Implement {@link #createExtendedData(JET2Context)} to return a object representing the private data of the context extension. This method is only called the first time
 * an extender is constructed for a particular context. 
 * The method {@link #getExtendedData()} returns the data to the current context instance.</li>
 * <li>Implement additional methods that make use of this private context data.</li>
 * <li>Create a constructor that accepts a {@link JET2Context} and passes it to the super constructor. It is recommended that
 * this constructor be private.</li>
 * <li> (Recommended) Implement a <code>public static <i>YourExtender</i> getInstance(JET2Context)</code> method that returns an instance the
 * AbstractContextExtender subclass.</li>
 * </ol>
 * @deprecated Since 0.9.0. Use public methods {@link JET2Context#addPrivateData(String, Object)} and {@link JET2Context#getPrivateData(String)}
 */
public abstract class AbstractContextExtender
{

  private final JET2Context baseContext;

  /**
   * 
   */
  protected AbstractContextExtender(JET2Context context)
  {
    super();
    this.baseContext = context;
    if (!context.hasContextExtender(this.getClass()))
    {
      context.registerContextExtender(this.getClass(), createExtendedData(context));
    }
  }

  /**
   * Called by the AbstractContextExtender constructor if the extender's data
   * has not yet been created in the context.
   * @param context the context in which the data will be created.
   * @return the extension data object.
   */
  protected abstract Object createExtendedData(JET2Context context);

  protected Object getExtendedData()
  {
    return baseContext.getContextExtenderData(this.getClass());
  }

  /**
   * Return the JET2Context that this extender instance is extending.
   * @return the hosting context.
   */
  public final JET2Context getContext()
  {
    return baseContext;
  }
}
