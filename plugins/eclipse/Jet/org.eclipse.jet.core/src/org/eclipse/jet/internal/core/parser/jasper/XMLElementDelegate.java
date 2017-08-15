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
 * $Id: XMLElementDelegate.java,v 1.3 2008/04/14 19:53:37 pelder Exp $
 * /
 *******************************************************************************/
package org.eclipse.jet.internal.core.parser.jasper;


import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jet.core.parser.IProblem;
import org.eclipse.jet.core.parser.ProblemSeverity;


/**
 * A JET Parser delegate that handles XML Elements.
 */
public class XMLElementDelegate implements JETCoreElement
{

  private static final String XML_TAG_CLOSE = ">"; //$NON-NLS-1$

  private static final String SLASH = "/"; //$NON-NLS-1$

  private static final String XML_CLOSE_TAG_OPEN = "</"; //$NON-NLS-1$

  private static final String XML_TAG_OPEN = "<"; //$NON-NLS-1$

  public XMLElementDelegate()
  {
    super();
  }

  public boolean accept(JETParseEventListener listener, JETReader reader, JETParser parser) throws JETException
  {
	JETException findException = null;
    if (!(listener instanceof JETParseEventListener2))
    {
      return false;
    }
    JETParseEventListener2 jet2Listener = (JETParseEventListener2)listener;

    String elementOpen = XML_TAG_OPEN;
    if (!reader.matches(elementOpen))
    {
      return false;
    }
    JETMark start = reader.mark();
    reader.advance(elementOpen.length());

    boolean isEndTag = false;
    boolean isEmptyTag = false;
    if (reader.matches(SLASH))
    {
      isEndTag = true;
      reader.advance(1);
      elementOpen = XML_CLOSE_TAG_OPEN;
    }

    String tagName = reader.parseToken(false, false /*don't skip spaces*/);

    boolean knownInvalidTagName = jet2Listener.isKnownInvalidTagName(tagName);
    if (!jet2Listener.isKnownTag(tagName) && !knownInvalidTagName)
    {
      reader.reset(start);
      return false;
    }

    JETMark interiorStartMark = reader.mark();
    // check for the end position...
    JETMark endTagMark = reader.skipUntil(XML_TAG_CLOSE);
    if (endTagMark == null)
    {
      MessagesUtil.recordUnterminatedElement(jet2Listener, XML_TAG_CLOSE, start, reader);
      return true;
    }

    // now reset to the tag interior for detailed parsing...
    reader.reset(interiorStartMark);

    Map attributeMap = Collections.EMPTY_MAP;
    if (isEndTag)
    {
      reader.skipSpaces();
    }
    else
    {
      // use Linked hash map to keep attributes in order of declaration
      attributeMap = new LinkedHashMap();
      reader.skipSpaces();
      
      while (reader.peekChar() != '>' && reader.peekChar() != '/')
      {
    	 
    	//update by yuxin 
    	//JETMark[] attrMarks = reader.parseXmlAttribute();
    	
        JETMark[] attrMarks = new JETMark [5];
		try {
			attrMarks = reader.parseXmlAttribute();
		} catch (JETException e) {
			findException = e;
			break;
		}
        String name = new String(reader.getChars(attrMarks[0], attrMarks[1]));
        String value = new String(reader.getChars(attrMarks[3], attrMarks[4]));
        if (attributeMap.containsKey(name))
        {
          String msg = Messages.getString("XMLElementDelegate_DuplicateAttribute"); //$NON-NLS-1$
          jet2Listener.recordProblem(
            ProblemSeverity.ERROR,
            IProblem.DuplicateAttribute,
            msg,
            null,
            attrMarks[0].getCursor(),
            attrMarks[4].getCursor(),
            attrMarks[0].getLine(), attrMarks[0].getCol());
        }
        else
        {
          attributeMap.put(name, unescape(value.substring(1, value.length() - 1)));
        }
        reader.skipSpaces();
       
      }
     
      //		    attributeMap = reader.parseTagAttributes();
    }
    // find the closing char...
    if (reader.peekChar() == '/' && !isEndTag)
    {
      reader.nextChar();
      isEmptyTag = true;
    }
    if (reader.peekChar() != '>')
    {
      MessagesUtil.recordUnterminatedElement(jet2Listener, ">", start, reader); //$NON-NLS-1$
    }
    reader.nextChar();
    JETMark stop = reader.mark();
    if(knownInvalidTagName)
    {
      jet2Listener.recordProblem(
        ProblemSeverity.WARNING,
        IProblem.UnknownXMLTag,
        Messages.getString("XMLElementDelegate_UnknownXMLTag"), //$NON-NLS-1$
        new Object[] {tagName},
        start.getCursor(),
        stop.getCursor(),
        start.getLine(), start.getCol());
      reader.reset(start);
      return false;
    }
    else if (isEmptyTag)
    {
      jet2Listener.handleXMLEmptyTag(tagName, start, stop, attributeMap);
    }
    else if (isEndTag)
    {
      jet2Listener.handleXMLEndTag(tagName, start, stop);
    }
    else
    {
      jet2Listener.handleXMLStartTag(tagName, start, stop, attributeMap);
    }
    if(findException != null){
  	  throw findException;
    }
    return true;
  }

	/**
	 * Remove any escape characters
	 * @param rawString
	 * @return
	 */
	private String unescape(String rawString) {
		StringBuffer result = new StringBuffer(rawString.length());
		for(StringCharacterIterator i = new StringCharacterIterator(rawString); i.current() != CharacterIterator.DONE; i.next()) {
			char c = i.current();
			if(c != '\\') {
				result.append(c);
			} else {
				c = i.next();
				if(c != CharacterIterator.DONE) {
					result.append(c);
				}
			}
		}
		return result.toString();
	}

}
