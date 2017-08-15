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
package org.eclipse.jet.internal.extensionpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jet.internal.JETActivatorWrapper;
import org.eclipse.jet.internal.taglib.ExtensionTagLibraryImpl;
import org.eclipse.jet.internal.taglib.TagAttributeDefinitionImpl;
import org.eclipse.jet.internal.taglib.TagDefinitionImpl;
import org.eclipse.jet.taglib.CustomTagKind;
import org.eclipse.jet.taglib.TagAttributeDefinition;
import org.eclipse.jet.taglib.TagDefinition;
import org.eclipse.jet.taglib.TagLibrary;
import org.eclipse.jet.xpath.NodeSet;
import org.eclipse.jet.xpath.XPath;
import org.eclipse.jet.xpath.XPathException;
import org.eclipse.jet.xpath.XPathExpression;
import org.eclipse.jet.xpath.XPathFactory;
import org.eclipse.jet.xpath.XPathRuntimeException;

/**
 * Factory for creating tag library data implementing the interfaces
 * {@link TagLibrary}, {@link TagDefinition} and {@link TagAttributeDefinition}.
 * <p>
 * This class is a singleton, and is not intended to be subclassed or instantiated by clients.
 * </p>
 */
public final class TagLibraryDataFactory
{

  public static final String A_ALLOW_AS_EMPTY = "allowAsEmpty"; //$NON-NLS-1$
  public static final String A_CLASS = "class"; //$NON-NLS-1$
  public static final String A_DEPRECATED = "deprecated"; //$NON-NLS-1$
  public static final String A_ID = "id"; //$NON-NLS-1$
  public static final String A_NAME = "name"; //$NON-NLS-1$
  public static final String A_PROCESS_CONTENTS = "processContents"; //$NON-NLS-1$
  public static final String A_STANDARD_PREFIX = "standardPrefix"; //$NON-NLS-1$
  public static final String A_TYPE = "type"; //$NON-NLS-1$
  public static final String A_USE = "use"; //$NON-NLS-1$
  public static final String A_WHEN_CONTAINING_LINE_IS_EMPTY = "whenContainingLineIsEmpty"; //$NON-NLS-1$
  
  public static final String E_ATTRIBUTE = "attribute"; //$NON-NLS-1$
  public static final String E_CONDITIONALTAG = "conditionalTag"; //$NON-NLS-1$
  public static final String E_CONTAINERTAG = "containerTag"; //$NON-NLS-1$
  public static final String E_DESCRIPTION = "description"; //$NON-NLS-1$
  public static final String E_EMPTYTAG = "emptyTag"; //$NON-NLS-1$
  public static final String E_FUNCTIONTAG = "functionTag"; //$NON-NLS-1$
  public static final String E_ITERATINGTAG = "iteratingTag"; //$NON-NLS-1$
  public static final String E_OTHERTAG = "otherTag"; //$NON-NLS-1$
  public static final String E_TAGLIBRARY = "tagLibrary"; //$NON-NLS-1$
  
  /**
   * 
   */
  public static final TagLibraryDataFactory INSTANCE = new TagLibraryDataFactory();
  
  public static final String PROCESS_CONTENTS_CUSTOM = "custom"; //$NON-NLS-1$
  public static final String PROCESS_CONTENTS_STANDARD = "standard"; //$NON-NLS-1$
  
  public static final String TYPE_BOOLEAN = "boolean"; //$NON-NLS-1$
  public static final String TYPE_STRING = "string"; //$NON-NLS-1$
  public static final String TYPE_XPATH = "xpath"; //$NON-NLS-1$
  
  public static final String USE_OPTIONAL = "optional"; //$NON-NLS-1$
  public static final String USE_REQUIRED = "required"; //$NON-NLS-1$
  
  public static final String WHEN_CONTAINING_LINE_IS_EMPTY_PRESERVE = "preserve"; //$NON-NLS-1$
  public static final String WHEN_CONTAINING_LINE_IS_EMPTY_REMOVE = "remove"; //$NON-NLS-1$

  private static final Map tagToKindMap = new HashMap(5);
  static
  {
    tagToKindMap.put(E_OTHERTAG, CustomTagKind.OTHER);
    tagToKindMap.put(E_FUNCTIONTAG, CustomTagKind.FUNCTION);
    tagToKindMap.put(E_ITERATINGTAG, CustomTagKind.ITERATING);
    tagToKindMap.put(E_CONDITIONALTAG, CustomTagKind.CONDITIONAL);
    tagToKindMap.put(E_EMPTYTAG, CustomTagKind.EMPTY);
    tagToKindMap.put(E_CONTAINERTAG, CustomTagKind.CONTAINER);
  }

  private boolean expresionsInitialized = false;

  private XPathExpression xIsDeprecated;

  private XPathExpression xId;

  private XPathExpression xName;

  private XPathExpression xStdPrefix;

  private XPathExpression xDescription;

  private XPathExpression xEAttributes;

  private XPathExpression xETagLibraries;

  private XPathExpression xETags;
//  private XPathExpression xClass;
  private XPathExpression xIsRemoveWhenContainingLineIsEmpty;
  private XPathExpression xIsAllowAsEmpty;
  private XPathExpression xIsOptionalUse;
  private XPathExpression xType;
  private XPathExpression xIsPreserveWhenContainingLineIsEmpty;
  private XPathExpression xElementName;
  private XPathExpression xIsCustomProcessContents;
  
  /**
   * 
   */
  private TagLibraryDataFactory()
  {
    super();
  }

  public TagLibrary createTagLibrary(String namespace, IConfigurationElement element) 
  {
    String id = element.getAttribute(A_ID);
    String libraryId = namespace + "." + id; //$NON-NLS-1$
    return new ExtensionTagLibraryImpl(libraryId, element);
  }

  public TagLibrary[] createTagLibraries(String namespace, Object pluginDocumentRoot)
  {
    List libraries = new ArrayList();
    try
    {
      initXPathExpressions();
      final NodeSet nodeSet = xETagLibraries.evaluateAsNodeSet(pluginDocumentRoot);
      for (Iterator i = nodeSet.iterator(); i.hasNext();)
      {
        Object tagLibraryElement = (Object)i.next();
        TagLibrary tagLibrary = createTagLibrary(namespace, tagLibraryElement);
        if(tagLibraryElement != null)
        {
          libraries.add(tagLibrary);
        }
      }
    }
    catch (XPathException e)
    {
      JETActivatorWrapper.INSTANCE.log(e);
    }
    catch (XPathRuntimeException e)
    {
      JETActivatorWrapper.INSTANCE.log(e);
    }
    return (TagLibrary[])libraries.toArray(new TagLibrary[libraries.size()]);
  }
  
  public TagLibrary createTagLibrary(String namespace, Object tagLibraryElement)
  {
    TagLibrary result = null;
    try
    {
      initXPathExpressions();
      // tag library attributes: id, name stdPrefix, deprecated
      String id = xId.evaluateAsString(tagLibraryElement);
      String tagLibraryId = namespace + "." + id; //$NON-NLS-1$
      String name = xName.evaluateAsString(tagLibraryElement);
      String stdPrefix = xStdPrefix.evaluateAsString(tagLibraryElement);
      boolean deprecated = xIsDeprecated.evaluateAsBoolean(tagLibraryElement);
      String description = xDescription.evaluateAsString(tagLibraryElement).trim();
      TagLibraryImpl tagLibrary = new TagLibraryImpl(tagLibraryId, name, description, stdPrefix, deprecated);
      
      NodeSet tags = xETags.evaluateAsNodeSet(tagLibraryElement);
      for (Iterator i = tags.iterator(); i.hasNext();)
      {
        Object tagElement = (Object)i.next();
        TagDefinition tagDefinition = createTagDefinition(tagLibrary, tagElement);
        if(tagDefinition != null)
        {
          tagLibrary.addTag(tagDefinition);
        }
      }
      
      result = tagLibrary;
    }
    catch (XPathException e)
    {
      JETActivatorWrapper.INSTANCE.log(e);
    }
    return result;
  }
  
  private TagDefinition createTagDefinition(TagLibrary tagLibrary, Object tagElement)
  {
    TagDefinition tagDefinition = null;
    // Attributes of a tag: name, class, deprecated, whenContainingLineIsEmpty, 
    // processContents(container and below), allowAsEmpty(containeronly)
    // description

    try
    {
      String name = xName.evaluateAsString(tagElement);
      String description = xDescription.evaluateAsString(tagElement).trim();
      String elementName = xElementName.evaluateAsString(tagElement);
      CustomTagKind kind = getCustomKind(elementName);
//      String tagClass = xClass.evaluateAsString(tagElement);
      boolean deprecated = xIsDeprecated.evaluateAsBoolean(tagElement);
      boolean removeWhenContainingLineIsEmpty;
      if(kind == CustomTagKind.EMPTY || kind == CustomTagKind.FUNCTION) {
        removeWhenContainingLineIsEmpty = xIsRemoveWhenContainingLineIsEmpty.evaluateAsBoolean(tagElement);
      } else {
        removeWhenContainingLineIsEmpty = !xIsPreserveWhenContainingLineIsEmpty.evaluateAsBoolean(tagElement);
      }
      boolean allowAsEmpty = xIsAllowAsEmpty.evaluateAsBoolean(tagElement);
      boolean customContentProcessing = xIsCustomProcessContents.evaluateAsBoolean(tagElement);
      
      final TagDefinitionImpl tagDefinitionImpl = new TagDefinitionImpl(tagLibrary, name, kind, description, 
              customContentProcessing, allowAsEmpty, deprecated, removeWhenContainingLineIsEmpty);
      tagDefinition = tagDefinitionImpl;
      
      NodeSet attributes = xEAttributes.evaluateAsNodeSet(tagElement);
      for (Iterator i = attributes.iterator(); i.hasNext();)
      {
        Object attributeElement = (Object)i.next();
        TagAttributeDefinition tagAttrDefn = createTagAttributeDefinition(attributeElement);
        if(tagAttrDefn != null) 
        {
          tagDefinitionImpl.addTagAttribute(tagAttrDefn);
        }
      }
    }
    catch (XPathRuntimeException e)
    {
      JETActivatorWrapper.INSTANCE.log(e);
    }
    
    return tagDefinition;
  }

  private TagAttributeDefinition createTagAttributeDefinition(Object attributeElement)
  {
    TagAttributeDefinition tagAttributeDefinition = null;
    // name, use, deprecated, type
    try
    {
      String name = xName.evaluateAsString(attributeElement);
      String description = xDescription.evaluateAsString(attributeElement).trim();
      boolean required = !xIsOptionalUse.evaluateAsBoolean(attributeElement);
      boolean deprecated = xIsDeprecated.evaluateAsBoolean(attributeElement);
      String type = xType.evaluateAsString(attributeElement);
      if(type.length() == 0)
      {
        type = "string"; //$NON-NLS-1$
      }
      
      tagAttributeDefinition = new TagAttributeDefinitionImpl(name, required, deprecated, description, type);
    }
    catch (XPathRuntimeException e)
    {
      JETActivatorWrapper.INSTANCE.log(e);
    }
    return tagAttributeDefinition;
  }

  /**
   * @param elementName
   * @return
   */
  private CustomTagKind getCustomKind(String elementName)
  {
    return (CustomTagKind)tagToKindMap.get(elementName);
  }

  private void initXPathExpressions() throws XPathException
  {
    if (!expresionsInitialized )
    {
      final XPath xpath = XPathFactory.newInstance().newXPath(null);

      xETagLibraries = xpath.compile("/plugin/extension[@point = 'org.eclipse.jet.tagLibraries']/" +  E_TAGLIBRARY); //$NON-NLS-1$

      xETags = xpath.compile(E_CONDITIONALTAG + "|" + E_CONTAINERTAG + "|" + E_EMPTYTAG  //$NON-NLS-1$//$NON-NLS-2$
        + "|" + E_FUNCTIONTAG + "|" + E_ITERATINGTAG + "|" + E_OTHERTAG);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

      xEAttributes = xpath.compile(E_ATTRIBUTE);

      xDescription = xpath.compile(E_DESCRIPTION);

      xElementName = xpath.compile("local-name()"); //$NON-NLS-1$
      
      xId = xpath.compile("@" + A_ID); //$NON-NLS-1$
      xName = xpath.compile("@" + A_NAME); //$NON-NLS-1$
      xStdPrefix = xpath.compile("@" + A_STANDARD_PREFIX); //$NON-NLS-1$
      xIsDeprecated = xpath.compile("@" + A_DEPRECATED + "= 'true'"); //$NON-NLS-1$ //$NON-NLS-2$
      
//      xClass = xpath.compile("@" + A_CLASS); //$NON-NLS-1$
      xIsRemoveWhenContainingLineIsEmpty = xpath.compile("@" + A_WHEN_CONTAINING_LINE_IS_EMPTY + " = '" + WHEN_CONTAINING_LINE_IS_EMPTY_REMOVE + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      xIsPreserveWhenContainingLineIsEmpty = xpath.compile("@" + A_WHEN_CONTAINING_LINE_IS_EMPTY + " = '" + WHEN_CONTAINING_LINE_IS_EMPTY_PRESERVE + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      xIsAllowAsEmpty = xpath.compile("@" + A_ALLOW_AS_EMPTY + " = 'true'");  //$NON-NLS-1$//$NON-NLS-2$
      xIsCustomProcessContents = xpath.compile("@" + A_PROCESS_CONTENTS + " = '" + PROCESS_CONTENTS_CUSTOM + "'");   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
      // name, class, deprecated, whenContainingLineIsEmpty, processContents(container and below), allowAsEmpty(containeronly)
      // description
      
      xIsOptionalUse = xpath.compile("@" + A_USE + " = '" + USE_OPTIONAL + "'");   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
      xType = xpath.compile("@" + A_TYPE); //$NON-NLS-1$
      // name, use, deprecated, type

      expresionsInitialized = true;
    }
  }
}
