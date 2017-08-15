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
package org.eclipse.jet.internal.xpath.inspectors;


import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.jet.internal.xpath.functions.StringFunction;
import org.eclipse.jet.xpath.inspector.ExpandedName;
import org.eclipse.jet.xpath.inspector.INodeInspector;


/**
 * XPath Inspector for EMF Settings interface, which is mapped to the XPath ATTRIBUTE node.
 *
 */
public class EStructuralFeatureSettingInspector implements INodeInspector
{

  private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
  /**
   * 
   */
  public EStructuralFeatureSettingInspector()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspector#getNodeKind(java.lang.Object)
   */
  public NodeKind getNodeKind(Object obj)
  {
    return NodeKind.ATTRIBUTE;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspector#getParent(java.lang.Object)
   */
  public Object getParent(Object obj)
  {
    EStructuralFeature.Setting setting = (EStructuralFeature.Setting)obj;
    return setting.getEObject();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspector#stringValueOf(java.lang.Object)
   */
  public String stringValueOf(Object object)
  {
    EStructuralFeature.Setting setting = (EStructuralFeature.Setting)object;
    return StringFunction.evaluate(setting.get(true));
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspector#expandedNameOf(java.lang.Object)
   */
  public ExpandedName expandedNameOf(Object object)
  {
    EStructuralFeature.Setting setting = (EStructuralFeature.Setting)object;
    ExtendedMetaData exMD = EObjectInspector.getExtendedMetaData(setting.getEObject());
    EStructuralFeature feature = setting.getEStructuralFeature();
    return new ExpandedName(exMD.getNamespace(feature), exMD.getName(feature));
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspector#getDocumentRoot(java.lang.Object)
   */
  public Object getDocumentRoot(Object contextNode)
  {
    EStructuralFeature.Setting setting = (EStructuralFeature.Setting)contextNode;
    return setting.getEObject().eResource();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspector#getChildren(java.lang.Object)
   */
  public Object[] getChildren(Object contextNode)
  {
    return EMPTY_OBJECT_ARRAY;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.INodeInspector#nameOf(java.lang.Object)
   */
  public String nameOf(Object contextNode)
  {
    EStructuralFeature.Setting setting = (EStructuralFeature.Setting)contextNode;
    ExtendedMetaData exMD = EObjectInspector.getExtendedMetaData(setting.getEObject());
    return exMD.getName(setting.getEStructuralFeature());
  }

  public boolean testExpandedName(Object node, ExpandedName testName)
  {
    return testName.equals(expandedNameOf(node));
  }

}
