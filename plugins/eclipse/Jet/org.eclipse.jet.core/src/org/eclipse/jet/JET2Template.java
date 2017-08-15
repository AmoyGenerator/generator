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
 * $Id: JET2Template.java,v 1.2 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet;



/**
 * Interface to compiled JET2 templates.
 *
 */
public interface JET2Template
{

  /**
   * Execute the template against the input contained in the {@link JET2Context}, 
   * and writing the result to the {@link JET2Writer}.
   * @param context the input context. Cannot be <code>null</code>.
   * @param out the output writer. Cannote be <code>null</code>.
   */
  public abstract void generate(JET2Context context, JET2Writer out);
}
