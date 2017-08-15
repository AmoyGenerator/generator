/**
 * <copyright>
 *
 * Copyright (c) 2006 IBM Corporation and others.
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
package org.eclipse.jet.internal.xpath.functions;


import org.eclipse.jet.xpath.DefaultXPathFunctionResolver;
import org.eclipse.jet.xpath.XPathFunctionResolver;


/**
 * Resolver for the built-in XPath functions.
 *
 */
public class BuiltinXPathFunctionResolver
{

  private static DefaultXPathFunctionResolver instance = null;


  /**
   * 
   */
  private BuiltinXPathFunctionResolver()
  {
    // nothing to do
  }

  public static XPathFunctionResolver getInstance()
  {
    if (instance == null)
    {
      instance = new DefaultXPathFunctionResolver();
      addFunctions();
    }
    return instance;
  }

  private static void addFunctions()
  {
    // *** Node set functions 
    instance.addFunction(LastFunction.FUNCTION_META_DATA);
    instance.addFunction(PositionFunction.FUNCTION_META_DATA);
    instance.addFunction(CountFunction.FUNCTION_META_DATA);
    // TODO Implement 'id'  new XPathFunctionMetaData("id", null, 1, 1),
    instance.addFunction(LocalNameFunction.FUNCTION_META_DATA);
    instance.addFunction(NamespaceUriFunction.FUNCTION_META_DATA);
    instance.addFunction(NameFunction.FUNCTION_META_DATA);

    // *** String functions
    instance.addFunction(StringFunction.FUNCTION_META_DATA);
    instance.addFunction(ConcatFunction.FUNCTION_META_DATA);
    instance.addFunction(StartsWithFunction.FUNCTION_META_DATA);
    instance.addFunction(ContainsFunction.FUNCTION_META_DATA);
    instance.addFunction(SubstringBeforeFunction.FUNCTION_META_DATA);
    instance.addFunction(SubstringAfterFunction.FUNCTION_META_DATA);
    instance.addFunction(SubstringFunction.FUNCTION_META_DATA);
    instance.addFunction(StringLengthFunction.FUNCTION_META_DATA);
    instance.addFunction(NormalizeSpaceFunction.FUNCTION_META_DATA);
    instance.addFunction(TranslateFunction.FUNCTION_META_DATA);

    // *** Boolean functions
    instance.addFunction(BooleanFunction.FUNCTION_META_DATA);
    instance.addFunction(NotFunction.FUNCTION_META_DATA);
    instance.addFunction(TrueFalseFunction.TRUE_FUNCTION_META_DATA);
    instance.addFunction(TrueFalseFunction.FALSE_FUNCTION_META_DATA);
    // TODO Implement 'lang'  new XPathFunctionMetaData("lang", null, 1, 1),

    // *** Number functions
    instance.addFunction(NumberFunction.FUNCTION_META_DATA);
    instance.addFunction(SumFunction.FUNCTION_META_DATA);
    instance.addFunction(FloorFunction.FUNCTION_META_DATA);
    instance.addFunction(CeilingFunction.FUNCTION_META_DATA);
    instance.addFunction(RoundFunction.FUNCTION_META_DATA);
  }

}
