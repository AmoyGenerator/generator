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
 * $Id: IteratingTagStatus.java,v 1.1 2009/03/16 14:42:30 pelder Exp $
 */
package org.eclipse.jet.internal.taglib.control;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Object representing a &lt;c:deepIterate&gt; status object
 */
public class IteratingTagStatus
{

  private final long begin;
  
  private final long count;
  
  private final long end;
  
  private long index;
  
  private Element status;
  
  public IteratingTagStatus(long begin, long count) {
    this.begin = begin;
    this.count = count;
    this.end = begin + count - 1;
    this.index = begin;
  }
  
  public IteratingTagStatus(int count) {
    this(0, count);
  }
  
  public final long getIndex()
  {
    return index;
  }

  public final void next()
  {
    this.index += 1;
  }

  public final long getBegin()
  {
    return begin;
  }

  public final long getCount()
  {
    return count;
  }

  public final long getEnd()
  {
    return end;
  }

  public final boolean isFirst()
  {
    return index == begin;
  }

  public final boolean isLast()
  {
    return index == end;
  }

  public Object getStatusObject() {
    if(status == null) {
      final Document document = createDocument();
      status = document.createElement("status"); //$NON-NLS-1$
    }
    status.setAttribute("begin", Long.toString(begin)); //$NON-NLS-1$
    status.setAttribute("end", Long.toString(end)); //$NON-NLS-1$
    status.setAttribute("index", Long.toString(index)); //$NON-NLS-1$
    status.setAttribute("count", Long.toString(count)); //$NON-NLS-1$
    if(isFirst()) {
      status.setAttribute("isFirst", "true");  //$NON-NLS-1$//$NON-NLS-2$
    } else {
      status.removeAttribute("isFirst"); //$NON-NLS-1$
    }
    if(isLast()) {
      status.setAttribute("isLast", "true");  //$NON-NLS-1$//$NON-NLS-2$
    } else {
      status.removeAttribute("isLast"); //$NON-NLS-1$
    }
    return status;
  }

  /**
   * @return
   * @throws ParserConfigurationException
   * @throws FactoryConfigurationError
   */
  private Document createDocument()
  {
    try
    {
      return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    }
    catch (ParserConfigurationException e)
    {
      throw new RuntimeException(e);
    }
    catch (FactoryConfigurationError e)
    {
      throw new RuntimeException(e);
    }
  }
}
