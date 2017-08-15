/**
 * <copyright>
 *
 * Copyright (c) 2009 IBM Corporation and others.
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
 * $Id: IJETParser2.java,v 1.2 2009/01/26 21:49:15 pelder Exp $
 */
package org.eclipse.jet.internal.core.parser;

import org.eclipse.jet.core.parser.IJETParser;

/**
 * Extension interface to {@link IJETParser}
 * 
 * NOTE: This should be public
 */
public interface IJETParser2 extends IJETParser
{
  /**
   * Parse contents as a JET template
   * @param template the template
   * @param templatePath the templatePath corresponding to the content of <code>null</code> if unknown
   * @return the root of the AST
   */
  public Object parse(char[] template, String templatePath);

}
