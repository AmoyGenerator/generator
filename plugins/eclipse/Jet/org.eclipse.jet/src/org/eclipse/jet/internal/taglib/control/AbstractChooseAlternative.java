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
package org.eclipse.jet.internal.taglib.control;

import java.text.MessageFormat;

import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.AbstractConditionalTag;
import org.eclipse.jet.taglib.ConditionalTag;
import org.eclipse.jet.taglib.CustomTag;
import org.eclipse.jet.taglib.JET2TagException;

/**
 * Abstract class than handles common behaviors between 'when' and 'otherwise' tag implementations. The
 * choose tag can be extended to include other tags by defining a tag the extends this type. Such tags
 * must have the following properties:
 * <ul>
 * <li>The tag must be declared as a <code>conditionalTag</code> in a <code>org.eclipse.jet.tagLibraries</code> extension.</li>
 * <li>The <code>processContents</code> attribute must be set of <code>custom</code>.</li>
 * <li>The tag should implement its {@link ConditionalTag#doEvalCondition(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context)} method as follows:
 * <pre>
 *    boolean processContents = false;
 *
 *    ChooseTag chooseTag = getChooseTag();
 *    if (!chooseTag.isSatisfied()){
 *        // implement test here, and set processContents if appropriate
 *        if (processContents) {
 *            chooseTag.setSatisfied(true);
 *        }
 *    }
 *    return processContents;
 * </pre>
 * </li>
 * </ul>
 */
public abstract class AbstractChooseAlternative extends AbstractConditionalTag implements ConditionalTag 
{

  /**
   * 
   */
  public AbstractChooseAlternative()
  {
    super();
  }

  /**
   * Write the body content to the parent Choose tag selectionWriter.
   */
  public void setBodyContent(JET2Writer bodyContent)
  {
    CustomTag parentTag = getParent();
  
    if (parentTag instanceof ChooseTag)
    {
      ChooseTag chooseTag = (ChooseTag)parentTag;
      chooseTag.writeSelection(bodyContent);
    }
  }

  /**
   * Return the parent ChooseTag, or throw an exception
   * @return the parent ChooseTag
   * @throws JET2TagException if there is no parent choose tag.
   */
  protected final ChooseTag getChooseTag() throws JET2TagException
  {
    CustomTag parentTag = getParent();

    if (parentTag instanceof ChooseTag)
    {
      return (ChooseTag)parentTag;
    }
    else
    {
      throw new JET2TagException(MessageFormat.format(JET2Messages.AnyTag_IllegalParent, new Object []{ "choose" })); //$NON-NLS-1$
    }

  }
}
