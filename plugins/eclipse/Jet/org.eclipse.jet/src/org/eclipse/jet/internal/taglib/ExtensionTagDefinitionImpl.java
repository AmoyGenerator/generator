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
 * $Id: ExtensionTagDefinitionImpl.java,v 1.2 2007/11/29 21:37:48 pelder Exp $
 */
package org.eclipse.jet.internal.taglib;

import java.text.MessageFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.internal.runtime.CustomTagFactory;
import org.eclipse.jet.taglib.CustomTag;
import org.eclipse.jet.taglib.CustomTagKind;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagAttributeDefinition;
import org.eclipse.jet.taglib.TagDefinition;
import org.eclipse.jet.taglib.TagInstanceFactory;
import org.eclipse.jet.taglib.TagLibrary;

/**
 * Implementation of TagDefinition that is backed by a loaded Extension point element 
 */
public class ExtensionTagDefinitionImpl extends TagDefinitionImpl implements TagDefinition, CustomTagFactory
{
  
  private static final String CLASS__ATTR = "class"; //$NON-NLS-1$

  private static final String USE__ATTR = "use"; //$NON-NLS-1$

  private static final String USE__VALUE__REQUIRED = "required"; //$NON-NLS-1$

  private static final String NAME__ATTR = "name"; //$NON-NLS-1$

  private static final String ATTRIBUTE__ELEMENT = "attribute"; //$NON-NLS-1$

  private static final String ALLOW_AS_EMPTY__ATTR = "allowAsEmpty"; //$NON-NLS-1$

  private static final String CONTENT_PROCESSING__VALUE__CUSTOM = "custom"; //$NON-NLS-1$

  private static final String CONTENT_PROCESSING__ATTR = "processContents"; //$NON-NLS-1$

  private static final String DEPRECATED__ATTR = "deprecated"; //$NON-NLS-1$

  private static final String TYPE__ATTR = "type"; //$NON-NLS-1$
  
  private static final String WHEN_CONTAINING_LINE_IS_EMPTY__ATTR = "whenContainingLineIsEmpty"; //$NON-NLS-1$
  
  private static final String REMOVE = "remove"; //$NON-NLS-1$
  
  private final IConfigurationElement configElement2;

  public static ExtensionTagDefinitionImpl createInstance(String tagName, IConfigurationElement configElement, CustomTagKind kind, ExtensionTagLibraryImpl tagLibrary)
  {
    boolean deprecated = Boolean.valueOf(configElement.getAttribute(DEPRECATED__ATTR)).booleanValue();
    boolean customContentProcessing = CONTENT_PROCESSING__VALUE__CUSTOM.equals(configElement.getAttribute(CONTENT_PROCESSING__ATTR));
    boolean allowAsEmpty = Boolean.valueOf(configElement.getAttribute(ALLOW_AS_EMPTY__ATTR)).booleanValue();

    String removeWhenEmpty = configElement.getAttribute(WHEN_CONTAINING_LINE_IS_EMPTY__ATTR);
    boolean removeWhenContainingLineIsEmpty;
    
    if(removeWhenEmpty == null)
    {
      removeWhenContainingLineIsEmpty = 
        kind != CustomTagKind.EMPTY && kind != CustomTagKind.FUNCTION;
    }
    else 
    {
      removeWhenContainingLineIsEmpty = REMOVE.equals(removeWhenEmpty);
    }
    
    String description = ExtensionTagLibraryImpl.getDescription(configElement).trim();

    ExtensionTagDefinitionImpl td = new ExtensionTagDefinitionImpl(configElement, tagLibrary, tagName, kind,
          description, customContentProcessing, allowAsEmpty, 
          deprecated, removeWhenContainingLineIsEmpty );
    
    IConfigurationElement[] children = configElement.getChildren(ATTRIBUTE__ELEMENT);
    for (int i = 0; i < children.length; i++)
    {
      TagAttributeDefinition tad = new TagAttributeDefinitionImpl(
        children[i].getAttribute(NAME__ATTR),
        USE__VALUE__REQUIRED.equalsIgnoreCase(children[i].getAttribute(USE__ATTR)),
        Boolean.valueOf(children[i].getAttribute(DEPRECATED__ATTR)).booleanValue(),
        ExtensionTagLibraryImpl.getDescription(children[i]),
        children[i].getAttribute(TYPE__ATTR));
      if (tad.getName() != null)
      {
        td.addTagAttribute(tad);
      }
      else
      {
        String msg = JET2Messages.TagDefinitionImpl_MissingName;
        InternalJET2Platform.logError(MessageFormat.format(msg, new Object []{
          children[i].getDeclaringExtension().getExtensionPointUniqueIdentifier(),
          children[i].getDeclaringExtension().getNamespace() }));
      }
    }
 
    return td;
  }
  
  private ExtensionTagDefinitionImpl(IConfigurationElement configElement, TagLibrary tagLibrary, String tagName, CustomTagKind kind, String description, boolean customContentProcessing, boolean allowAsEmpty, boolean deprecated, boolean removeWhenContainingLineIsEmpty)
  {
    super(tagLibrary, tagName, kind,
      description, customContentProcessing, allowAsEmpty, 
      deprecated, removeWhenContainingLineIsEmpty);
    configElement2 = configElement;
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.CustomTagFactory#newTagElement()
   */
  public CustomTag newTagElement() throws JET2TagException
  {
    TagInstanceFactory factory = ((ExtensionTagLibraryImpl)getTagLibrary()).getTagInstanceFactory();
    if(factory != null) {
      return factory.createCustomTag(getName());
    } else {
      try
      {
        return (CustomTag)configElement2.createExecutableExtension(CLASS__ATTR);
      }
      catch (CoreException e)
      {
        throw new JET2TagException(e);
      }
    }
  }

}
