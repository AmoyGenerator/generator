/**
 * <copyright>
 *
 * Copyright (c) 2009 IBM Corporation and others.
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
 * $Id: SetLocaleTag.java,v 1.1 2009/03/06 20:53:09 pelder Exp $
 */
package org.eclipse.jet.internal.taglib.format;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.taglib.AbstractEmptyTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.osgi.util.NLS;

/**
 * Implement &lt;f:setLocale&;gt tag.
 */
public class SetLocaleTag extends AbstractEmptyTag
{

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.EmptyTag#doAction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAction(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    final String value = getAttribute("value"); //$NON-NLS-1$
    final String variant = getAttribute("variant"); //$NON-NLS-1$
    
    if(value.trim().length() == 0) {
      throw new JET2TagException(NLS.bind(Messages.SetLocaleTag_AttributeMustNotBeBlank, "value")); //$NON-NLS-1$
    }
    
    ResourceBundleUtil.setLocale(context, value, variant != null ? variant : ""); //$NON-NLS-1$
  }

}
