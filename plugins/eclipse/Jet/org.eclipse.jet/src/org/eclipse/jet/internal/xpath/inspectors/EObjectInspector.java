/*******************************************************************************
 * Copyright (c) 2006, 2009 IBM Corporation and others.
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


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.xpath.inspector.AddElementException;
import org.eclipse.jet.xpath.inspector.CopyElementException;
import org.eclipse.jet.xpath.inspector.ExpandedName;
import org.eclipse.jet.xpath.inspector.IElementInspector;
import org.eclipse.jet.xpath.inspector.INodeInspector;
import org.eclipse.jet.xpath.inspector.INodeInspectorExtension1;
import org.eclipse.jet.xpath.inspector.InspectorManager;
import org.eclipse.jet.xpath.inspector.InvalidChildException;
import org.eclipse.jet.xpath.inspector.SimpleElementRequiresValueException;


/**
 * Implement an inspector for EObjects
 * 
 */
public class EObjectInspector implements IElementInspector, INodeInspectorExtension1
{

  static final class ElementNameTester implements IXMLElementTester
  {

    private final ExpandedName expandedName;
    private final ExtendedMetaData exMD;
    private final EObject documentRoot;

    public ElementNameTester(ExtendedMetaData exMD, ExpandedName expandedName, EObject documentRoot)
    {
      this.expandedName = expandedName;
      this.exMD = exMD;
      this.documentRoot = documentRoot;
    }

    public boolean matches(Object child)
    {
      if (child instanceof EObject)
      {
        final EObject eChild = (EObject)child;
        final EReference containmentFeature = eChild.eContainmentFeature();
        // Note, the final condition on class names prevents subclasses of the
        // declared feature type from being accepted if there is a substitution
        // group for the element. This is necessary to handle
        // matches against substitution group head elements, which must only return
        // elements of the head type, not sub-types
        if (expandedName.getLocalPart().equals(exMD.getName(containmentFeature)) &&
          (expandedName.getNamespaceURI() == null 
              || expandedName.getNamespaceURI().equals(exMD.getNamespace(containmentFeature)))
              && substFeatureMatches(eChild.eClass(), containmentFeature)
              )
        {
          return true;
        }
        else {
          // See whether this is a substitution element containmentFeature
          final String childNS = exMD.getNamespace(eChild.eClass());
          // If so, the child element's NS has a feature whose name is the test feature
          final EStructuralFeature testFeature = exMD.getElement(childNS, expandedName.getLocalPart());
          // and whose affiliation is the containment feature
          final EStructuralFeature affiliation = testFeature != null ? exMD.getAffiliation(testFeature) : null;
          if(affiliation != null
              && affiliation.getName().equals(containmentFeature.getName())
              && testFeature.getEType().equals(eChild.eClass())) {
            return true;
          }
        } 
      }
      else if (child instanceof EMFEAttrAsElementWrapper)
      {
        final EMFEAttrAsElementWrapper wrapper = (EMFEAttrAsElementWrapper)child;
        final EStructuralFeature feature = wrapper.getFeature();
        if (expandedName.getLocalPart().equals(exMD.getName(feature)) &&
          (expandedName.getNamespaceURI() == null || expandedName.getNamespaceURI().equals(exMD.getNamespace(feature))))
        {
          return true;
        }

      }
      return false;
    }
    /**
     * Test whether the substitution feature for the element matches
     * the containment feature reported by EMF for the object
     * @param eClass the EClass of the element being considered
     * @param containmentFeature the reported containment feature
     * @return true if there is no substitution feature or if that substitution equals the containment feature
     */
    private boolean substFeatureMatches(EClass eClass, EReference containmentFeature)
    {
      final EStructuralFeature substitutionFeature = substitutionFeature(eClass);
      return substitutionFeature == null || substitutionFeature.equals(containmentFeature);
    }

    /**
     * Return the global element (represented by a structural feature) that corresponds to the passed EClass,
     * and that is marked as a substitution element for another element. 
     * @param eClass an EClass
     * @return the structural feature or <code>null</code>
     */
    private EStructuralFeature substitutionFeature(EClass eClass)
    {
      if(documentRoot == null) {
        return null;
      }
      List globalElements = exMD.getElements(documentRoot.eClass());
      for (Iterator i = globalElements.iterator(); i.hasNext();)
      {
        EStructuralFeature sf = (EStructuralFeature)i.next();
        if(exMD.getAffiliation(sf) != null) {
          // its a substitution, see if its type is that of child
          if(sf.getEType().equals(eClass)) {
            return sf;
          }
        }
      }
      return null;
    }

    
  }


  static class SchemaElementTester implements IXMLElementTester
  {
    private final ExpandedName expandedName;
    private final ExtendedMetaData exMD;

    public SchemaElementTester(ExtendedMetaData exMD, ExpandedName expandedName)
    {
      this.expandedName = expandedName;
      this.exMD = exMD;
    }

    public boolean matches(Object child)
    {
      if(child instanceof EObject) {
        final EObject eChild = (EObject)child;
        final EReference containmentFeature = eChild.eContainmentFeature();
        final List affiliations = getAffiliations(containmentFeature);
        final List testFeatures = affiliations.size() > 0 ? affiliations : Collections.singletonList(containmentFeature);
        for (Iterator i = testFeatures.iterator(); i.hasNext();)
        {
          final EStructuralFeature testFeature = (EStructuralFeature)i.next();
          if(expandedName.getLocalPart().equals(exMD.getName(testFeature))
              && (expandedName.getNamespaceURI() == null 
                  || expandedName.getNamespaceURI().equals(exMD.getNamespace(testFeature)))) {
           return true; 
          }
        }
      }
      return false;
    }
    
    private List getAffiliations(EStructuralFeature feature)
    {
      List result = new ArrayList();

      for (EStructuralFeature affiliation = exMD.getAffiliation(feature); affiliation != null; affiliation = exMD.getAffiliation(affiliation))
      {
        result.add(affiliation);
      }

      return result;
    }

  }


  private static final String SCHEMA_ELEMENT_PREFIX = "_schema-element_"; //$NON-NLS-1$

  private static final String EMPTY_STRING = ""; //$NON-NLS-1$

  /**
   * 
   */
  private final static Map resourceSetMetaData = Collections.synchronizedMap(new WeakHashMap());

  protected static final Object[] EMPTY_ARRAY = new Object []{};

  /**
   * 
   */
  public EObjectInspector()
  {
    super();
  }




  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jet.xpath.IElementInspector#getAttributes(java.lang.Object)
   */
  public Object[] getAttributes(Object contextNode)
  {
    EObject eObject = (EObject)contextNode;
    return getAttributesInternal(eObject, false);
  }


  /**
   * @param eObject
   * @param includeUnsets TODO
   * @return
   */
  private Object[] getAttributesInternal(EObject eObject, boolean includeUnsets)
  {
    ExtendedMetaData exMD = getExtendedMetaData(eObject);
    // this is all the EStructuralFeatures that are explicitly tagged as
    // attributes...
    List allAttributes = new ArrayList(exMD.getAllAttributes(eObject.eClass()));
    // now add those EAttributes which don't have an explicit metadata...
    for (Iterator i = eObject.eClass().getEAllAttributes().iterator(); i.hasNext();)
    {
      EAttribute attribute = (EAttribute)i.next();
      if (exMD.getFeatureKind(attribute) == ExtendedMetaData.UNSPECIFIED_FEATURE)
      {
        allAttributes.add(attribute);
      }
    }
    // now build a list of EStructuralFeature.Setting objects representing
    // the non-null attributes...
    List attributeSettings = new ArrayList(allAttributes.size());
    for (Iterator i = allAttributes.iterator(); i.hasNext();)
    {
      EStructuralFeature feature = (EStructuralFeature)i.next();

      if (eObject.eIsSet(feature) || includeUnsets)
      {
        if (exMD.getFeatureKind(feature) == ExtendedMetaData.ATTRIBUTE_WILDCARD_FEATURE)
        {
          // this feature represents an anyAttribute wildcard, extract
          // from the FeatureMap the actual attributes/features.
          FeatureMap featureMap = (FeatureMap)eObject.eGet(feature);
          for (Iterator j = featureMap.iterator(); j.hasNext();)
          {
            FeatureMap.Entry entry = (FeatureMap.Entry)j.next();
            EStructuralFeature.Setting setting = new AnyAttributeSetting(entry, eObject);
            attributeSettings.add(setting);
          }
        }
        else
        {
          EStructuralFeature.Setting setting = ((InternalEObject)eObject).eSetting(feature);
          attributeSettings.add(setting);
        }
      }
    }
    return attributeSettings.toArray();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jet.xpath.IElementInspector#getNamedAttribute(java.lang.Object,
   *      org.eclipse.jet.xpath.ExpandedName)
   */
  public Object getNamedAttribute(Object contextNode, ExpandedName nameTestExpandedName)
  {
    EObject eObject = (EObject)contextNode;
    final Setting setting = getNamedAttributeInternal(eObject, nameTestExpandedName);
//    return setting == null || !setting.isSet() ? null : setting;
    // Fix bug 133151
    return setting == null || (!setting.isSet() && setting.get(true) == null) ? null : setting;
  }

  /**
   * Stronger typed verions of {@link #getNamedAttribute(Object, ExpandedName)}.
   * @param eObject
   * @param nameTestExpandedName
   * @return an Setting for the attribute.
   */
  private EStructuralFeature.Setting getNamedAttributeInternal(EObject eObject, ExpandedName nameTestExpandedName)
  {
    EClass eClass = eObject.eClass();

    EStructuralFeature feature = null;

    if (isXMLNode(eClass))
    {
      // It seems expensive to get all the attributes and run through
      // them, but
      // the extendedMetaData.getAttribute() fails to find schema-based
      // attributes.
      // 
      ExtendedMetaData exMD = getExtendedMetaData(eObject);
      Object[] allAttributeSettings = getAttributesInternal(eObject, true);
      for (int i = 0; i < allAttributeSettings.length; i++)
      {
        EStructuralFeature.Setting setting = (EStructuralFeature.Setting)allAttributeSettings[i];
        EStructuralFeature settingFeature = setting.getEStructuralFeature();
        if (matchesFeature(nameTestExpandedName, settingFeature, exMD))
        {
          return setting;
        }
      }

      // This is the code that JUST doesn't work.
      // feature = extendedMetaData.getAttribute(eClass, featureURI ==
      // null ? extendedMetaData.getNamespace(eClass) : featureURI,
      // featureName);
      // if(feature == null) {
      // // try finding in an 'anyAttribute' feature.
      // EStructuralFeature anyAttributeFeature =
      // eClass.getEStructuralFeature("anyAttribute");
      // if(anyAttributeFeature != null) {
      // FeatureMap anyAttrs = (FeatureMap)
      // eObject.eGet(anyAttributeFeature);
      // for (Iterator i = anyAttrs.iterator(); i.hasNext();) {
      // FeatureMap.Entry entry = (FeatureMap.Entry) i.next();
      // EStructuralFeature eFeature = entry.getEStructuralFeature();
      // ExpandedName entryEN = new
      // ExpandedName(eFeature.getEContainingClass()
      // .getEPackage().getNsURI(), eFeature.getName());
      // if(nameTestExpandedName.equals(entryEN)) {
      // return new AnyAttributeSetting(entry, eObject);
      // }
      // }
      // }
      //
      // }
    }
    else
    {
      String featureURI = nameTestExpandedName.getNamespaceURI();
      String featureName = nameTestExpandedName.getLocalPart();

      feature = eClass.getEStructuralFeature(featureName);
      if (feature instanceof EReference)
      {
        // not an 'attribute'
        feature = null;
      }
      else if (featureURI != null && !featureURI.equals(feature.getEContainingClass().getEPackage().getNsURI()))
      {
        // namespace URI's didn't match...
        feature = null;
      }
      // if we found a feature, return an EStructuralFeature.Setting for
      // it.
      if (feature != null)
      {
        EStructuralFeature.Setting setting = ((InternalEObject)eObject).eSetting(feature);
        return setting;
      }
    }
    return null;
  }

  /**
   * @param expandedName
   * @param feature
   * @param exMD TODO
   * @return
   */
  private boolean matchesFeature(ExpandedName expandedName, EStructuralFeature feature, ExtendedMetaData exMD)
  {
    ExpandedName featureEN = internalExpandedNameOfFeature(feature, exMD);
    final boolean equals = expandedName.equals(featureEN) || expandedName.equals(new ExpandedName(featureEN.getLocalPart()));
    return equals;
  }

  /**
   * @param feature
   * @param exMD TODO
   * @return
   */
  private ExpandedName internalExpandedNameOfFeature(EStructuralFeature feature, ExtendedMetaData exMD)
  {
    ExpandedName featureEN = new ExpandedName(exMD.getNamespace(feature), exMD.getName(feature));
    return featureEN;
  }

  private boolean isXMLNode(EClass eClass)
  {
    return eClass.getEAnnotation(ExtendedMetaData.ANNOTATION_URI) != null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jet.xpath.IElementInspector#createAttribute(java.lang.Object,
   *      java.lang.String, java.lang.String)
   */
  public boolean createAttribute(Object contextNode, String attributeName, String value)
  {
    EObject eObject = (EObject)contextNode;
    EClass eClass = eObject.eClass();

    final Setting existingSetting = getNamedAttributeInternal(eObject, new ExpandedName(attributeName));
    if (existingSetting != null)
    {
      existingSetting.set(existingSetting.getEStructuralFeature().getEType().getEPackage().getEFactoryInstance().createFromString(
        (EDataType)existingSetting.getEStructuralFeature().getEType(),
        value));
      return true;
    }

    final EStructuralFeature feature = getExtendedMetaData(eObject).demandFeature(null,
      attributeName,
      false);
    EStructuralFeature affiliation = getExtendedMetaData(eObject).getAffiliation(eClass, feature);
    if (affiliation != null)
    {
      eObject.eSet(feature, value);
      return true;
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jet.xpath.INodeInspector#getNodeKind(java.lang.Object)
   */
  public NodeKind getNodeKind(Object obj)
  {
    EObject eObject = (EObject)obj;
    NodeKind result = NodeKind.ELEMENT;
    EStructuralFeature containingFeature = eObject.eContainingFeature();
    ExtendedMetaData exMD = getExtendedMetaData(eObject);
    EClass eClass = eObject.eClass();
    String eClassName = exMD.getName(eClass);
    if(XMLTypePackage.eNS_URI.equals(exMD.getNamespace(eClass)))
    {
      // Handle objects in the generic XMLTypePackage separately. Not all types here are
      // Elements. Comments, ProcessingInstructions, CData etcetra can also appear.
      // This complicated further because the package has evolved since EMF 2.2, and as
      // of EMF 2.3 includes extra types (ProcessingInstruction), which for compatibility,
      // the code cannot explicitly reference.
      // For these special kinds,
      if ("processingInstruction_._type".equals(eClassName)) //$NON-NLS-1$
      {
        return NodeKind.PROCESSING_INSTRUCTION;
      }
      else if("".equals(eClassName)) //$NON-NLS-1$
      {
        return NodeKind.ROOT;
      }
    }
    if (containingFeature != null)
    {
      // mostly EObjects map to ELEMENTs, but some EObjects are
      // ATTRIBUTES.
      // We check the extended meta data on the containing feature to see.
      switch (exMD.getFeatureKind(containingFeature))
      {
        case ExtendedMetaData.ATTRIBUTE_FEATURE:
        case ExtendedMetaData.ATTRIBUTE_WILDCARD_FEATURE:
        result = NodeKind.ATTRIBUTE;
          break;
      }
    }
    else if (EMPTY_STRING.equals(eClassName))
    {
      // it's a XML Document Root.
      result = NodeKind.ROOT;
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jet.xpath.INodeInspector#getParent(java.lang.Object)
   */
  public Object getParent(Object obj)
  {
    EObject eObject = (EObject)obj;
    Object parent = eObject.eContainer();
    if (parent == null)
    {
      parent = eObject.eResource();
    }
    return parent;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jet.xpath.INodeInspector#stringValueOf(java.lang.Object)
   */
  public String stringValueOf(Object object)
  {
    EObject eObject = (EObject)object;
    EClass eClass = eObject.eClass();
    if (isXMLNode(eClass))
    {
      switch (getExtendedMetaData(eObject).getContentKind(eClass))
      {
        case ExtendedMetaData.UNSPECIFIED_CONTENT:
        case ExtendedMetaData.EMPTY_CONTENT:
        return EMPTY_STRING;
        case ExtendedMetaData.SIMPLE_CONTENT:
        {
          EStructuralFeature simpleFeature = getExtendedMetaData(eObject).getSimpleFeature(eClass);
          final Object rawValue = eObject.eGet(simpleFeature);
          return stringValueOfFeature(simpleFeature, rawValue);
        }
        case ExtendedMetaData.ELEMENT_ONLY_CONTENT:
        case ExtendedMetaData.MIXED_CONTENT:
        {
          StringBuffer result = new StringBuffer();
          appendStringValue(result, eObject);
          return result.toString();
        }
      }
    }
    else if(object instanceof EEnumLiteral) {
      return ((EEnumLiteral)object).getLiteral();
    }
    return EMPTY_STRING;
  }

  /**
   * @param result
   * @param eObject
   */
  private void appendStringValue(StringBuffer result, EObject eObject)
  {
    Object[] children = getChildren(eObject);
    for (int i = 0; i < children.length; i++)
    {
      if (children[i] instanceof EObject)
      {
        appendStringValue(result, (EObject)children[i]);
      }
      else if (children[i] instanceof EMFXMLNodeWrapper && ((EMFXMLNodeWrapper)children[i]).getNodeKind() == NodeKind.TEXT)
      {
        result.append(((EMFXMLNodeWrapper)children[i]).getText());
      }
      else if (children[i] instanceof EMFEAttrAsElementWrapper)
      {
        final EMFEAttrAsElementWrapper wrapper = (EMFEAttrAsElementWrapper)children[i];
        Object rawValue = wrapper.getValue();
        result.append(stringValueOfFeature(wrapper.getFeature(), rawValue));
      }
    }
  }

  /**
   * @param feature
   * @param rawValue
   * @return
   */
  protected String stringValueOfFeature(EStructuralFeature feature, final Object rawValue)
  {
    EFactory ef = feature.getEType().getEPackage().getEFactoryInstance();
    String result = ef.convertToString((EDataType)feature.getEType(), rawValue);
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jet.xpath.INodeInspector#expandedNameOf(java.lang.Object)
   */
  public ExpandedName expandedNameOf(Object object)
  {
    EObject eObject = (EObject)object;
    EStructuralFeature containmentFeature = eObject.eContainmentFeature();

    return expandedNameOfFeature(eObject, containmentFeature);
  }

  /**
   * @param eObject
   * @param feature
   * @return
   */
  protected final ExpandedName expandedNameOfFeature(EObject eObject, EStructuralFeature feature)
  {
    EClass eClass = eObject.eClass();

    if (feature != null && isXMLNode(eClass))
    {
      ExtendedMetaData exMD = getExtendedMetaData(eObject);
      return internalExpandedNameOfFeature(feature, exMD);
    }
    else if (feature != null)
    {
      // EPackage ePackage = containmentFeature.getEContainingClass().getEPackage();
      // including the package URI in the expanded name would require all
      // XPath name tests to use a qualified name. Don't think we want to force
      // this.
      return new ExpandedName(/*ePackage.getNsURI(), */feature.getName());
    }
    else if (eObject.eResource() != null)
    {
      return new ExpandedName(EMFResourceInspector.CONTENTS_PSEUDO_ELEMENT);
    }
    else
    {
      return null;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jet.xpath.INodeInspector#getChildren(java.lang.Object)
   */
  public Object[] getChildren(Object contextNode)
  {
    EObject eObject = (EObject)contextNode;
    EClass eClass = eObject.eClass();

    if (isXMLNode(eClass))
    {
      final ExtendedMetaData exMD = getExtendedMetaData(eObject);
      switch (exMD.getContentKind(eClass))
      {
        case ExtendedMetaData.UNSPECIFIED_CONTENT:
        case ExtendedMetaData.EMPTY_CONTENT:
        case ExtendedMetaData.SIMPLE_CONTENT:
        return EMPTY_ARRAY;
        case ExtendedMetaData.ELEMENT_ONLY_CONTENT:
        {
          final List allElementsFeatures = exMD.getAllElements(eClass);
          List result = new ArrayList(allElementsFeatures.size());
          for (Iterator i = allElementsFeatures.iterator(); i.hasNext();)
          {
            EStructuralFeature feature = (EStructuralFeature)i.next();
            switch (exMD.getFeatureKind(feature))
            {
              case ExtendedMetaData.ELEMENT_FEATURE:
              if(exMD.getGroup(feature) != null) {
                // features a group will be retrived view the group feature 
                // (where they will occur in the document order)
                // suppress them here.
                break;
              }
              if (feature instanceof EAttribute)
              {
                if (eObject.eIsSet(feature))
                {
                  final Setting setting = ((InternalEObject)eObject).eSetting(feature);
                  if(!feature.isMany()) 
                  {
                    result.add(new EMFEAttrAsElementWrapper(setting));
                  }
                  else
                  {
                    final EList list = (EList)setting.get(true);
                    for(int j = 0; j < list.size(); j++ ) {
                      result.add(new EMFEAttrAsElementWrapper(setting, j));
                    }
                  }
                }
              }
              else if(((EReference)feature).isContainment())
              {
                if (feature.isMany())
                {
                  result.addAll((List)eObject.eGet(feature));
                }
                else
                {
                  result.add(eObject.eGet(feature));
                }
              }
                break;
              case ExtendedMetaData.GROUP_FEATURE:
              case ExtendedMetaData.ELEMENT_WILDCARD_FEATURE:
              {
                Object rawValue = eObject.eGet(feature);
                if (rawValue instanceof FeatureMap)
                {
                  FeatureMap featureMap = (FeatureMap)rawValue;
                  for (Iterator j = featureMap.iterator(); j.hasNext();)
                  {
                    FeatureMap.Entry entry = (FeatureMap.Entry)j.next();
                    if (entry.getEStructuralFeature() instanceof EAttribute)
                    {
                      result.add(new EMFEAttrAsElementWrapper(new AnyAttributeSetting(entry, eObject)));
                    }
                    else
                    {
                      result.add(entry.getValue());
                    }
                  }
                }
                else
                {
                  result.addAll((List)rawValue);
                }
              }

            }
          }
          return result.toArray();
        }
        case ExtendedMetaData.MIXED_CONTENT:
        {
          EAttribute mixedFeature = exMD.getMixedFeature(eClass);
          FeatureMap featureMap = (FeatureMap)eObject.eGet(mixedFeature);
          Object[] result = new Object [featureMap.size()];
          int resultIndex = 0;
          for (Iterator i = featureMap.iterator(); i.hasNext();)
          {
            FeatureMap.Entry entry = (FeatureMap.Entry)i.next();
            Object resultObject = null;
            final EStructuralFeature entryFeature = entry.getEStructuralFeature();
            NodeKind nodeKind = XMLTypeUtil2.getNodeKind(entryFeature);
            if (nodeKind == NodeKind.TEXT || nodeKind == NodeKind.COMMENT)
            {
              resultObject = new EMFXMLNodeWrapper(eObject, (String)entry.getValue(), nodeKind);
            }
            else if(entryFeature instanceof EReference && ((EReference)entryFeature).isContainment())
            {
              resultObject = entry.getValue();
            } else if(entryFeature instanceof EAttribute) {
              resultObject = new EMFEAttrAsElementWrapper(new AnyAttributeSetting(entry,eObject));
            }
            result[resultIndex++] = resultObject;
          }
          return result;
        }
      }
    }
    else
    {
      return eObject.eContents().toArray();
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jet.xpath.INodeInspector#getDocumentRoot(java.lang.Object)
   */
  public Object getDocumentRoot(Object contextNode)
  {
    final EObject eObject = (EObject)contextNode;
    // look for a xxxDocumentRoot instance. These are a little hard
    // to spot. They do not have a marking interface, instead, we look
    // for EMF ExtendedMetaData with 'name' set to the empty string.
    for (EObject parent = eObject; parent != null; parent = parent.eContainer())
    {
      // we know document roots have an empty name in the extended metadata.
      if (EMPTY_STRING.equals(getExtendedMetaData(eObject).getName(parent.eClass())))
      {
        return parent;
      }
    }
    // didn't find a document Root, try finding the EMF Resource
    return eObject.eResource();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jet.xpath.INodeInspector#nameOf(java.lang.Object)
   */
  public String nameOf(Object contextNode)
  {
    EObject eObject = (EObject)contextNode;
    EStructuralFeature feature = eObject.eContainmentFeature();
    return nameOfFeature(eObject, feature);
  }

  /**
   * @param eObject
   * @param feature
   * @return
   */
  protected String nameOfFeature(EObject eObject, EStructuralFeature feature)
  {
    if (feature != null)
    {
      EPackage ePkg = feature.getEContainingClass().getEPackage();
      String namespace = ePkg.getNsURI();
      String nsPrefix = ePkg.getNsPrefix();
      final INodeInspector inspector = InspectorManager.getInstance().getInspector(eObject);
      Object root = inspector.getDocumentRoot(eObject);
      if (root instanceof EObject)
      {
        String namespace2 = getExtendedMetaData(eObject).getNamespace(feature);
        if (namespace2 == null)
        {
          nsPrefix = null;
        }
        else
        {
          // it's a DocumentRoot, look up the namespace prefix map.
          EObject rootEObject = (EObject)root;
          EReference prefixMapFeature = getExtendedMetaData(eObject).getXMLNSPrefixMapFeature(rootEObject.eClass());
          if (prefixMapFeature != null)
          {
            EcoreEMap xmlnsPrefixMap = (EcoreEMap)rootEObject.eGet(prefixMapFeature);
            for (Iterator i = xmlnsPrefixMap.iterator(); i.hasNext();)
            {
              BasicEMap.Entry entry = (BasicEMap.Entry)i.next();
              if (namespace.equals(entry.getValue()))
              {
                nsPrefix = (String)entry.getKey();
                break;
              }
            }
          }
        }
      }
      String localName = isXMLNode(eObject.eClass()) ? getExtendedMetaData(eObject).getName(feature) : feature.getName();
      return nsPrefix != null && nsPrefix.length() > 0 ? nsPrefix + ":" //$NON-NLS-1$
        + localName : localName;
    }
    else if (eObject.eResource() != null)
    {
      return EMFResourceInspector.CONTENTS_PSEUDO_ELEMENT;
    }
    else
    {
      return EMPTY_STRING;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jet.xpath.INodeInspectorExtension1#getNamedChildren(java.lang.Object,
   *      org.eclipse.jet.xpath.ExpandedName)
   */
  public Object[] getNamedChildren(Object contextNode, ExpandedName nameTestExpandedName)
  {
    EObject eObject = (EObject)contextNode;
    EClass eClass = eObject.eClass();

    if (isXMLNode(eClass))
    {
      return getXMLNamedElement2(eObject, eClass, nameTestExpandedName);

    }
    else
    {
      String featureURI = nameTestExpandedName.getNamespaceURI();
      String featureName = nameTestExpandedName.getLocalPart();

      EStructuralFeature feature = getEReference(eClass, featureURI, featureName);
      // if we found a feature, return the value for
      // it.
      if (feature != null && eObject.eIsSet(feature))
      {
        Object rawValue = eObject.eGet(feature);
        if (feature.isMany())
        {
          return ((List)rawValue).toArray();
        }
        else
        {
          return new Object []{ rawValue };
        }
      }

      // try testing for eClassName of contained elements.
      List children = new ArrayList(eObject.eContents());
      for (Iterator i = children.iterator(); i.hasNext();)
      {
        EObject child = (EObject)i.next();
        if (!child.eClass().getName().equals(featureName))
        {
          i.remove();
        }
      }
      return children.toArray();

    }
  }

  interface IXMLElementTester {
    public abstract boolean matches(Object child);
  }
  
  private Object[] getXMLNamedElement2(EObject eObject, EClass eClass, ExpandedName expandedName)
  {
    final Object[] children = getChildren(eObject);
    final List result = new ArrayList(children.length);
    
    final IXMLElementTester test = getElementTester(eObject, expandedName);
    
    for(int i = 0; i < children.length; i++) {
      Object child = children[i];
      if(test.matches(child)) {
        result.add(child);
      }
    }
    return result.toArray();
  }


  /**
   * Factory method for an {@link IXMLElementTester}
   * @param eObject
   * @param expandedName
   * @return
   */
  private IXMLElementTester getElementTester(EObject eObject, ExpandedName expandedName)
  {
    final ExtendedMetaData exMD = getExtendedMetaData(eObject);
    final String localPart = expandedName.getLocalPart();
    final IXMLElementTester test;
    if(localPart.startsWith(SCHEMA_ELEMENT_PREFIX)) {
      test = new SchemaElementTester(exMD, 
        new ExpandedName(expandedName.getNamespaceURI(), localPart.substring(SCHEMA_ELEMENT_PREFIX.length())));
    } else {
      Object documentRoot = getDocumentRoot(eObject);
      test = new ElementNameTester(exMD, expandedName, documentRoot instanceof EObject ? (EObject)documentRoot : null);
    }
    return test;
  }
  
  /**
   * @param eClass
   * @param featureURI
   * @param featureName
   * @return
   */
  private EStructuralFeature getEReference(EClass eClass, String featureURI, String featureName)
  {
    EStructuralFeature feature = eClass.getEStructuralFeature(featureName);
    if (feature instanceof EAttribute)
    {
      // not an 'reference'
      feature = null;
    }
    else if (featureURI != null && !featureURI.equals(feature.getEContainingClass().getEPackage().getNsURI()))
    {
      // namespace URI's didn't match...
      feature = null;
    }
    return feature;
  }

  private boolean addFeatureMapElement(
    EObject parent,
    EStructuralFeature feature,
    EStructuralFeature fmFeature,
    Object newElement,
    Object addBeforeThisSibling)
  {
    if (!(addBeforeThisSibling instanceof EObject))
    {
      // error
      return false;
    }
    EObject sibling = (EObject)addBeforeThisSibling;
    if (parent != sibling.eContainer())
    {
      return false;
    }
    ExtendedMetaData exMD = getExtendedMetaData(parent);
    if (!matchesFeature(internalExpandedNameOfFeature(feature, exMD), sibling.eContainmentFeature(), exMD))
    {
      return false;
    }
    // insert using the existing feature. This avoids problems with
    // having two apparently identically named features, that are in fact different.
    // This can happen only when in the default namespace, and the extended meta data
    // used to load the model is not the same as is used by this inspector.
    // We will try hard to use the same extended meta data for both, but JET2 is not
    // necessarily responsible for loading a model - it may be handed an already loaded model.
    feature = sibling.eContainmentFeature();
    FeatureMap fm = (FeatureMap)parent.eGet(fmFeature);
    EList featureList = fm.list(feature);
    int addIndex = featureList.indexOf(sibling);
    featureList.add(addIndex, newElement);
    return true;
  }

  public Object addElement(Object contextNode, ExpandedName elementName, Object addBeforeThisSibling) throws SimpleElementRequiresValueException,
    InvalidChildException
  {
    EObject eObject = (EObject)contextNode;
    EStructuralFeature feature;

    feature = getElementFeature(eObject, elementName);

    if (feature == null)
    {
      return null;
    }

    // create the new element...
    Object newElement = null;
    if (feature instanceof EReference)
    {
      boolean useAnyType = feature.getEType() == EcorePackage.eINSTANCE.getEObject();
      newElement = useAnyType ? XMLTypeFactory.eINSTANCE.createAnyType() : feature.getEType().getEPackage().getEFactoryInstance().create(
        (EClass)feature.getEType());
    }
    else if (feature instanceof EAttribute)
    {
      throw new SimpleElementRequiresValueException(contextNode, elementName);
    }

    // add the new element to the feature

    newElement = addElementToParent(eObject, feature, newElement, addBeforeThisSibling);
    if (newElement == null)
    {
      throw new InvalidChildException(contextNode, elementName, addBeforeThisSibling);
    }
    return newElement;
  }

  /**
   * @param eParent
   * @param feature
   * @param newElement
   * @param addBeforeThisSibling
   * @return
   */
  private Object addElementToParent(EObject eParent, EStructuralFeature feature, Object newElement, Object addBeforeThisSibling)
  {
    // see if there is a feature map containing 'feature'
    EStructuralFeature affiliation = getExtendedMetaData(eParent).getAffiliation(eParent.eClass(), feature);
    if (FeatureMapUtil.isFeatureMap(affiliation))
    {
      FeatureMap fm = (FeatureMap)eParent.eGet(affiliation);
      // Note: This code seems a little perverse because Feature Maps don't always
      // do what you would expect. Some things this code is aware of:
      //   * fm.size() does not necessarily return the size of the feature - it seems
      //        to return the # of entries of a particular feature - how it decides is unknown.
      //   * in fm.add(int, feature, Object), then passed index is relative to the other
      //        elements of the featuremap with the same feature id. This makes it very hard
      //        to add an element before/after another element in a different feature.
      //   * in fm.add(feature, Object), there is a similar behavior. The add is after the
      //        last element in the same feature, not at the end of the map ?!?
      // The code below attempts to compensate for all the above.
      if (addBeforeThisSibling == null)
      {
        // NOT fm.add(feature, newElement);
        fm.add(FeatureMapUtil.createEntry(feature, newElement));
      }
      else
      {
        boolean added = addFeatureMapElement(eParent, feature, affiliation, newElement, addBeforeThisSibling);
        if (!added)
        {
          newElement = null;
        }
      }
    }
    else if (feature.isMany())
    {
      List values = (List)eParent.eGet(feature);
      int index = addBeforeThisSibling != null ? values.indexOf(addBeforeThisSibling) : values.size();
      if (index != -1)
      {
        values.add(index, newElement);
      }
      else
      {
        newElement = null;
      }
    }
    else
    {
      eParent.eSet(feature, newElement);
    }
    return newElement;
  }

  /**
   * @param eObject TODO
   * @param elementName
   * @param eClass
   * @return
   */
  private EStructuralFeature getElementFeature(EObject eObject, ExpandedName elementName)
  {
    EClass eClass = eObject.eClass();
    EStructuralFeature feature;
    // find the feature that will contain the element...
    String elementNS = elementName.getNamespaceURI();
    String localName = elementName.getLocalPart();
    if (isXMLNode(eClass))
    {
      feature = getExtendedMetaData(eObject).getElement(eClass, elementNS, localName);
      if (feature == null)
      {
        // generic XML processing creates features on the document root, look for them there...
        EStructuralFeature wildCardFeature = getExtendedMetaData(eObject).getElementWildcardAffiliation(eClass, elementNS, localName);
        if (wildCardFeature != null)
        {
          feature = getExtendedMetaData(eObject).demandFeature(elementNS, localName, true);
        }
      }
    }
    else
    {
      feature = getEReference(eClass, elementNS, localName);
    }
    return feature;
  }

  public void removeElement(Object contextNode)
  {
    EObject eObject = (EObject)contextNode;

    final EObject parent = eObject.eContainer();
    if (parent != null)
    {
      final EStructuralFeature containingFeature = eObject.eContainingFeature();
      if (FeatureMapUtil.isFeatureMap(containingFeature))
      {
        // feature map remove
        final EReference feature = eObject.eContainmentFeature();
        FeatureMap fm = (FeatureMap)parent.eGet(containingFeature);
        fm.list(feature).remove(eObject);
      }
      else if (containingFeature.isMany())
      {
        // simple list remove
        final EList eList = (EList)parent.eGet(containingFeature);
        eList.remove(eObject);
      }
      else
      {
        // simple element remove
        parent.eSet(containingFeature, null);
      }

    }
    else if (eObject.eResource() != null)
    {
      eObject.eResource().getContents().remove(eObject);
    }
  }

  /**
   * A shallow EMF copier...
   */
  private final class ShallowCopier extends EcoreUtil.Copier
  {

    /**
     * 
     */
    private static final long serialVersionUID = 5305612945568496060L;

    protected void copyContainment(EReference eReference, EObject eObject, EObject copyEObject)
    {
      // do nothing, we don't copy containment.
      // This is all that is required for the shallow copier to work.
    }
  }

  public Object copyElement(Object tgtParent, Object srcElement, String name, boolean recursive) throws CopyElementException
  {
    EObject eObject = (EObject)tgtParent;

    EStructuralFeature feature = getElementFeature(eObject, new ExpandedName(name));

    Object newElement = null;

    final NodeKind nodeKind = getNodeKind(srcElement);
    if (srcElement instanceof EObject && nodeKind == NodeKind.ELEMENT)
    {
      EObject srcEObject = (EObject)srcElement;
      if (recursive)
      {
        newElement = EcoreUtil.copy(srcEObject);
      }
      else
      {
        EcoreUtil.Copier copier = new ShallowCopier();
        EObject newEObject = copier.copy(srcEObject);
        copier.copyReferences();
        final EClass eClass = newEObject.eClass();
        if(isXMLNode(eClass)) {
          // copyReferences will have copied mixed and any features, if present
          // we need to remove these...
          final EAttribute mixedFeature = getExtendedMetaData(eObject).getMixedFeature(eClass);
          if(mixedFeature != null)
          {
            FeatureMap fm = (FeatureMap)newEObject.eGet(mixedFeature);
            fm.clear();
          }
        }
        
        newElement = newEObject;
      }

      addElementToParent(eObject, feature, newElement, null);
    }
    else if( srcElement instanceof EObject && nodeKind == NodeKind.ROOT)
    {
      try
      {
        newElement = addElement(eObject, new ExpandedName(name), null);
        if(recursive) {
          final Object[] children = getChildren(srcElement);
          Object root = null;
          for (int i = 0; i < children.length; i++)
          {
            if(children[i] instanceof EObject && getNodeKind(children[i]) == NodeKind.ELEMENT) {
              root = children[i];
              break;
            }
          }
          if(root != null) {
            copyElement(newElement, root, expandedNameOf(root).getLocalPart(), true);
          }
        }
      }
      catch (SimpleElementRequiresValueException e)
      {
        throw new CopyElementException(e);
      }
      catch (InvalidChildException e)
      {
        throw new CopyElementException(e);
      }
    }
    else if (srcElement instanceof EMFEAttrAsElementWrapper)
    {
      EMFEAttrAsElementWrapper wrapper = (EMFEAttrAsElementWrapper)srcElement;

      eObject.eSet(feature, wrapper.getValue());
      newElement = new EMFEAttrAsElementWrapper(((InternalEObject)eObject).eSetting(feature));
    }
    else
    {
      throw new CopyElementException(JET2Messages.EObjectInspector_SourceIncompatibleWithTarget);
    }
    return newElement;
  }

  public Object addTextElement(Object parentElement, String name, String bodyContent, boolean asCData) throws AddElementException
  {
    EObject eObject = (EObject)parentElement;
    ExpandedName elementName = new ExpandedName(name);

    EStructuralFeature feature;

    feature = getElementFeature(eObject, elementName);

    if (feature == null)
    {
      return null;
    }

    if (feature.getEType() == EcorePackage.eINSTANCE.getEObject())
    {
      // it's a demand created type...
      EStructuralFeature affiliation = getExtendedMetaData(eObject).getAffiliation(eObject.eClass(), feature);
      AnyType newElement = XMLTypeFactory.eINSTANCE.createAnyType();
      
      if(asCData)
      {
        newElement.getMixed().add(XMLTypePackage.eINSTANCE.getXMLTypeDocumentRoot_CDATA(), bodyContent);
      } else {
        newElement.getMixed().add(XMLTypePackage.eINSTANCE.getXMLTypeDocumentRoot_Text(), bodyContent);
      }
      if (affiliation.isMany())
      {
        FeatureMap fm = (FeatureMap)eObject.eGet(affiliation);
        fm.add(feature, newElement);
      }
      else
      {
        eObject.eSet(feature, newElement);
      }
      return newElement;
    }
    else if (feature instanceof EReference)
    {
      throw new AddElementException(JET2Messages.EObjectInspector_NotSimpleTextElement);
    }
    else
    {
      // it's a static type.
      // add the new element to the feature
      final EFactory factory = feature.getEType().getEPackage().getEFactoryInstance();
      Object setValue = factory.createFromString((EDataType)feature.getEType(), bodyContent);
      if (feature.isMany())
      {
        throw new UnsupportedOperationException("Not implemented yet - multi-valued simple feature"); //$NON-NLS-1$
      }
      else
      {
        eObject.eSet(feature, setValue);
        return new EMFEAttrAsElementWrapper(((InternalEObject)eObject).eSetting(feature));
      }
    }
  }

  public void removeAttribute(Object element, String name)
  {
    EObject eObject = (EObject)element;
    final Setting setting = getNamedAttributeInternal(eObject, new ExpandedName(name));
//    if(setting != null && setting.isSet() && setting.getEStructuralFeature().isUnsettable()) {
    if(setting != null) {
      setting.unset();
    }
  }

  public boolean testExpandedName(Object node, ExpandedName testName)
  {
    return testName.equals(expandedNameOf(node)) || testName.equals(eclassNameOf(node));
  }

  private ExpandedName eclassNameOf(Object node)
  {
    ExpandedName en = null;
    if(getNodeKind(node) == NodeKind.ELEMENT)
    {
      en = new ExpandedName(((EObject)node).eClass().getName());
    }
    return en;
  }


  /**
   * @param eObject TODO
   * @return Returns the extendedMetaData.
   */
  public static ExtendedMetaData getExtendedMetaData(EObject eObject)
  {
    Resource resource = eObject.eResource();
    ResourceSet resourceSet = resource == null ? null : resource.getResourceSet();
    
    ExtendedMetaData extendedMetaData = (ExtendedMetaData)resourceSetMetaData.get(resourceSet);
    if(extendedMetaData == null)
    {
      extendedMetaData = resourceSet == null ? new BasicExtendedMetaData() : new BasicExtendedMetaData(resourceSet.getPackageRegistry());
      resourceSetMetaData.put(resourceSet, extendedMetaData);
    }
    return extendedMetaData;
  }

}
