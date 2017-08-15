/**
 * <copyright>
 *
 * Copyright (c) 2006, 2008 IBM Corporation and others.
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
 * $Id$
 */
package org.eclipse.jet.internal.taglib.format;


import org.eclipse.jet.JET2Context;


/**
 * Provide storage for unique and milliseconds tags
 */
public final class FormatContextExtender
{

  /**
   * The name of the JET variable, that, if set, indicates that format tags that return
   * random values should generate pseudo random values.
   */
  public static final String FORMAT_TAGS_TEST_FLAG = "org.eclipse.jet.formatTags.test"; //$NON-NLS-1$

  private static String PRIVATE_CONTEXT_DATA_KEY = FormatContextExtender.class.getName();

  /**
   * Return a FormatContextExtender for the given context. A new FormatContextExtender is created if 
   * none exists.
   * @param context the JET2Context to extend.
   * @return the FormatContextExtender
   * @throws NullPointerException if <code>context</code> is <code>null</code>.
   */
  public static FormatContextExtender getInstance(JET2Context context)
  {
    if(context == null) {
      throw new NullPointerException();
    }
    FormatContextExtender ex = (FormatContextExtender)context.getPrivateData(PRIVATE_CONTEXT_DATA_KEY);
    if (ex == null)
    {
      ex = new FormatContextExtender(context);
      context.addPrivateData(PRIVATE_CONTEXT_DATA_KEY, ex);
    }
    return ex;
  }

  private long lastMilliseconds = 0;
  
  private long pseudoUUIDBase = 0;
  private long pseudoLastMilliseconds = 0;

  private int lastUnique = 0;

  private final JET2Context context;

  /**
   * @param context 
   */
  private FormatContextExtender(JET2Context context)
  {
    super();
    this.context = context;
  }

  /**
   * Reurns a unique system time in milliseconds.
   */
  public long getMilliseconds()
  {
    long next = !context.hasVariable(FORMAT_TAGS_TEST_FLAG)
      ? System.currentTimeMillis() : pseudoLastMilliseconds + 1;
    if (next <= lastMilliseconds)
    {
      next = lastMilliseconds + 1;
    }
    lastMilliseconds = next;
    return next;
  }

  /**
   * Reurns a unique integer.
   */
  public int getUnique()
  {
    int unique = lastUnique + 1;
    lastUnique = unique;
    return unique;
  }

  public String getPseudoUUID()
  {
    // similar ECoreUtil.generateUUID(), which returns a 24 character string using base 64 digits:
    // A-Za-z0-9-_ . 
    pseudoUUIDBase++;
    
    StringBuffer result = new StringBuffer(23);
    result.append("-Fake_UUID-"); //$NON-NLS-1$
    result.append(Long.toHexString(pseudoUUIDBase));
    result.append('-');
    int padding = 24 - result.length();
    for(int i = 0; i < padding; i++) {
      result.append('0');
    }
    
    return result.toString();
  }

}
