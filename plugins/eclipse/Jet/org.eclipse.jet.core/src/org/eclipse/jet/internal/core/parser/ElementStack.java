/**
 * <copyright>
 *
 * Copyright (c) 2007 IBM Corporation and others.
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
 * $Id: ElementStack.java,v 1.1 2007/04/04 14:53:53 pelder Exp $
 */
package org.eclipse.jet.internal.core.parser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jet.core.parser.ast.XMLBodyElement;


/**
 * Stack of JET AST Elements used by parser.
 */
public final class ElementStack
{

  private final List stack = new ArrayList();

  /**
   * 
   */
  public ElementStack()
  {
    super();
  }

  /**
   * Push an XML element on the active element stack
   * @param element
   */
  public void push(XMLBodyElement element)
  {
    stack.add(element);
  }

  /**
   * Remove an XML element from the active element stack
   * @return the popped element
   * @throws IllegalStateException if the stack is empty
   */
  public XMLBodyElement pop()
  {
    if (stack.size() == 0)
    {
      throw new IllegalStateException("stack is empty"); //$NON-NLS-1$
    }
    return (XMLBodyElement)stack.remove(stack.size() - 1);
  }

  /**
   * Return the top element on the active element statkc
   * @return the top element
   * @throws IllegalStateException if the stack is empty
   */
  public XMLBodyElement peek()
  {
    if (stack.size() == 0)
    {
      throw new IllegalStateException("stack is empty"); //$NON-NLS-1$
    }
    return (XMLBodyElement)stack.get(stack.size() - 1);

  }

  /**
   * Test if the stack is empty
   * @return <code>true</code> if empty, <code>false</code> otherwise
   */
  public boolean isEmpty()
  {
    return stack.size() == 0;
  }

  /**
   * Find the index of the element with the specified name that is closest to the top of the stack.
   * @param name the element name to search for
   * @return the found element's index or -1 if not found
   */
  public int findElementIndex(String name)
  {
    for (int i = stack.size() - 1; i >= 0; i--)
    {
      XMLBodyElement element = get(i);
      if (element.getName().equalsIgnoreCase(name))
      {
        return i;
      }
    }

    return -1;
  }

  /**
   * Test if the passed index is that of the top-most element in the active element stack.
   * @param index the test index
   * @return <code>true</code> if the index represents the top-most element, <code>false</code> otherwise.
   * @throws IllegalArgumentException if the index is less than 0 or larger than the stack size.
   */
  public boolean isAtTop(int index)
  {
    if (index < 0 || index >= stack.size())
    {
      throw new IllegalArgumentException();
    }
    return index == stack.size() - 1;
  }

  /**
   * Return the element at the specified index
   * @param index
   * @return the element
   * @throws IllegalArgumentException if the index is less than 0 or larger than the stack size.
   * 
   */
  public XMLBodyElement get(int index)
  {
    if (index < 0 || index >= stack.size())
    {
      throw new IllegalArgumentException();
    }
    return (XMLBodyElement)stack.get(index);
  }
}