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
 * $Id$
 * /
 *******************************************************************************/

package org.eclipse.jet.internal.compiler;


import java.util.Map;

import org.eclipse.jet.core.parser.ast.XMLBodyElement;
import org.eclipse.jet.core.parser.ast.XMLElement;


/**
 * A wrapper on XMLElement that provides some utility methods needed by the compiler JET templates.
 *
 */
public class GenXMLElement
{

  private final XMLElement element;

  private final String tagVariable;

  private final String parentTagVariable;

  private final String tagInfoVariableName;

  public GenXMLElement(XMLElement element, String tagVariable, String tagInfoVariableName, String parentTagVariable)
  {
    this.element = element;
    this.tagVariable = tagVariable;
    this.tagInfoVariableName = tagInfoVariableName;
    this.parentTagVariable = parentTagVariable;
  }

  public String getTagVariable()
  {
    return tagVariable;
  }

  public String getTagInfoVariable()
  {
    return tagInfoVariableName;
  }

  public String getName()
  {
    return element.getName();
  }

  public Map getAttributes()
  {
    return element.getAttributes();
  }

  public boolean hasBody()
  {
    return element instanceof XMLBodyElement;
  }

  public String getNSPrefix()
  {
    return element.getNSPrefix();
  }

  public String getTagNCName()
  {
    return element.getTagNCName();
  }

  public String getParentTagVariable()
  {
    return parentTagVariable;
  }

  public boolean requiresNewWriter()
  {
    return element.getTagDefinition().requiresNewWriter();
  }
}
