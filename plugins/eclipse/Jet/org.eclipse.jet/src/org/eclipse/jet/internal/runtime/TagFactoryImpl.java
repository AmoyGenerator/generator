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

package org.eclipse.jet.internal.runtime;


import java.text.MessageFormat;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.ConditionalTag;
import org.eclipse.jet.taglib.ContainerTag;
import org.eclipse.jet.taglib.CustomTag;
import org.eclipse.jet.taglib.CustomTagKind;
import org.eclipse.jet.taglib.EmptyTag;
import org.eclipse.jet.taglib.FunctionTag;
import org.eclipse.jet.taglib.IteratingTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.OtherTag;
import org.eclipse.jet.taglib.RuntimeTagElement;
import org.eclipse.jet.taglib.TagDefinition;
import org.eclipse.jet.taglib.TagFactory;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.taglib.TagLibraryManager;


/**
 * Standard implementation of a Eclipse-hosted Tag factory.
 *
 */
public class TagFactoryImpl implements TagFactory
{

  private final JET2Context context;

  /**
   * @param context 
   * 
   */
  public TagFactoryImpl(JET2Context context)
  {
    super();
    this.context = context;
  }

  public RuntimeTagElement createRuntimeTag(String libraryId, String tagNCName, String tagQName, TagInfo tagInfo)
  {
    org.eclipse.jet.taglib.TagLibrary tagLibrary = TagLibraryManager.getInstance().getTagLibrary(libraryId, false);
    if(tagLibrary == null)
    {
      // TODO Improve so that tag developers can find the problem faster.
      String msg = JET2Messages.TagFactoryImpl_TagCreateFailed;
      context.logError(tagInfo, MessageFormat.format(msg, new Object []{ tagNCName, libraryId }), null);
      return new DoNothingRuntimeTagElement();
    }
    try
    {
      final TagDefinition tagDefinition = tagLibrary.getTagDefinition(tagNCName);
      if(tagDefinition instanceof CustomTagFactory) {
        CustomTag newCustomTag = ((CustomTagFactory)tagDefinition).newTagElement();
        CustomTagKind kind = newCustomTag.getKind();
        if (kind == CustomTagKind.CONDITIONAL)
        {
          return new SafeConditionalRuntimeTag((ConditionalTag)newCustomTag);
        }
        else if (kind == CustomTagKind.EMPTY)
        {
          return new SafeEmptyRuntimeTag((EmptyTag)newCustomTag);
        }
        else if (kind == CustomTagKind.FUNCTION)
        {
          return new SafeFunctionRuntimeTag((FunctionTag)newCustomTag);
        }
        else if (kind == CustomTagKind.ITERATING)
        {
          return new SafeIteratingRuntimeTag((IteratingTag)newCustomTag);
        }
        else if (kind == CustomTagKind.OTHER)
        {
          return new SafeOtherRuntimeTag((OtherTag)newCustomTag);
        }
        else if (kind == CustomTagKind.CONTAINER)
        {
          return new SafeContainerRuntimeTag((ContainerTag)newCustomTag);
        }
      }
    }
    catch (JET2TagException e)
    {
      // TODO Improve so that tag developers can find the problem faster.
      String msg = JET2Messages.TagFactoryImpl_TagCreateFailed;
      context.logError(tagInfo, MessageFormat.format(msg, new Object []{ tagNCName, libraryId }), e);
    }
    return new DoNothingRuntimeTagElement();
  }


}
