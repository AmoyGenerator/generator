/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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

package org.eclipse.jet.internal.taglib.control;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.XPathContextExtender;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.AbstractEmptyTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;


/**
 * Implement the JET2 Control tag 'get'.
 *
 */
public class GetTag extends AbstractEmptyTag
{

  /**
   * 
   */
  public GetTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.EmptyTag#doAction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAction(TagInfo tc, JET2Context context, JET2Writer out) throws JET2TagException
  {
    String selectXPath = getAttribute("select"); //$NON-NLS-1$
    String defaultValue = getAttribute("default"); //$NON-NLS-1$

    XPathContextExtender xpathContext = XPathContextExtender.getInstance(context);
    String result = xpathContext.resolveAsString(xpathContext.currentXPathContextObject(), selectXPath);
    if (result == null && defaultValue != null)
    {
      result = defaultValue;
    }

    if(result == null)
    {
      throw new JET2TagException(JET2Messages.GetTag_NoResult);
    }

    String convertCase = getAttribute("case"); //$NON-NLS-1$

    if(convertCase != null){
      if(convertCase.equalsIgnoreCase("l") || convertCase.equalsIgnoreCase("lower")){
        result = result.toLowerCase();
      }else if(convertCase.equalsIgnoreCase("u") || convertCase.equalsIgnoreCase("upper")){
        result = result.toUpperCase();
      }else if(convertCase.equalsIgnoreCase("hl") || convertCase.equalsIgnoreCase("headLower")){
        if(result.length() > 0){
          char ch = result.charAt(0);
          String head = String.valueOf(ch).toLowerCase();
          result = head + result.substring(1);
        }
      }else if(convertCase.equalsIgnoreCase("hu") || convertCase.equalsIgnoreCase("headUpper")){
        if(result.length() > 0){
          char ch = result.charAt(0);
          String head = String.valueOf(ch).toUpperCase();
          result = head + result.substring(1);
        }
      }
    }
    out.write(result);
  }


  private String camel4underline(String param){  
    Pattern  p=Pattern.compile("[A-Z]");  
    if(param==null ||param.equals("")){  
      return "";  
    } 

    StringBuilder builder=new StringBuilder(param);  
    Matcher mc=p.matcher(param);  
    int i=0;  
    while(mc.find()){  
      builder.replace(mc.start()+i, mc.end()+i, "_"+mc.group().toLowerCase());  
      i++;  
    }  

    if('_' == builder.charAt(0)){  
      builder.deleteCharAt(0);  
    }  
    return builder.toString();  
  }  


}
