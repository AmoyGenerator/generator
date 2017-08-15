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
package org.eclipse.jet.internal.xpath.inspectors;


import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.eclipse.jet.xpath.inspector.INodeInspector.NodeKind;


/**
 * More convenience methods for EMF XMLTypes.
 * @see org.eclipse.emf.ecore.xml.type.util.XMLTypeUtil
 *
 */
public class XMLTypeUtil2
{

  /**
   * 
   */
  private XMLTypeUtil2()
  {
    super();
  }

  public static NodeKind getNodeKind(EStructuralFeature feature)
  {
    EPackage featurePackage = ((EClass)feature.eContainer()).getEPackage();
    if (featurePackage == XMLTypePackage.eINSTANCE)
    {
      switch (feature.getFeatureID())
      {
        case XMLTypePackage.XML_TYPE_DOCUMENT_ROOT__CDATA:
        case XMLTypePackage.XML_TYPE_DOCUMENT_ROOT__TEXT:
        return NodeKind.TEXT;
        case XMLTypePackage.XML_TYPE_DOCUMENT_ROOT__COMMENT:
        return NodeKind.COMMENT;
        default:
        return NodeKind.ATTRIBUTE;
      }
    }
    return NodeKind.ELEMENT;
  }
}
