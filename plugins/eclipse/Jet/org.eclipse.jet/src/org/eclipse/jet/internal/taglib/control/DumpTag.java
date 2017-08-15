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

package org.eclipse.jet.internal.taglib.control;

import java.util.StringTokenizer;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.XPathContextExtender;
import org.eclipse.jet.internal.exceptions.MissingRequiredAttributeException;
import org.eclipse.jet.internal.exceptions.NoMatchingNodeException;
import org.eclipse.jet.taglib.AbstractEmptyTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.xpath.inspector.IElementInspector;
import org.eclipse.jet.xpath.inspector.INodeInspector;
import org.eclipse.jet.xpath.inspector.InspectorManager;
import org.eclipse.jet.xpath.inspector.INodeInspector.NodeKind;

/**
 * Implement the Contributed Control Tags tag 'dump'.
 *
 */
public class DumpTag extends AbstractEmptyTag {


	private static final String NL = System.getProperty("line.separator"); //$NON-NLS-1$
  private String _select = null;
	private Object resolved_node;

	private String _format = null;

	private String _entities = null;

	/*
	 *  Begin tag-specific declarations
	 */

	/*
	 * End tag-specific declarations
	 */
	
	public DumpTag() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.taglib.EmptyTag#doAction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
	 */
	public void doAction(TagInfo tagInfo, JET2Context context, JET2Writer out) throws JET2TagException {

		XPathContextExtender xpathContext = XPathContextExtender.getInstance(context);

		/**
		 * Get the "select" attribute
.
		 * Use that expression to retrieve a single node.
		 */
		_select = getAttribute("select"); //$NON-NLS-1$
		if (_select == null) {
			throw new MissingRequiredAttributeException("select"); //$NON-NLS-1$
		}
		resolved_node = xpathContext.resolveSingle(xpathContext.currentXPathContextObject(), _select);
		if (resolved_node == null) {
			throw new NoMatchingNodeException(_select);
		}

		/**
		 * Get the "format" attribute and convert any embedded dynamic attributes
		 * into dynamic XPath expressions.
		 */
		_format = getAttribute("format"); //$NON-NLS-1$
		/**
		 * Get the "entities" attribute and convert any embedded dynamic attributes
		 * into dynamic XPath expressions.
		 */
		_entities = getAttribute("entities"); //$NON-NLS-1$

		/**
	 	 *  Begin doAction logic
		 */

		boolean format = false;
		if ("true".equalsIgnoreCase(_format)) { format = true; } //$NON-NLS-1$
		if ("yes".equalsIgnoreCase(_format)) { format = true; } //$NON-NLS-1$

		boolean entities = false;
		if ("true".equalsIgnoreCase(_entities)) { entities = true; } //$NON-NLS-1$
		if ("yes".equalsIgnoreCase(_entities)) { entities = true; } //$NON-NLS-1$
		
//		dump(resolved_node,format,entities,1,context,out);
        
        StringBuffer buffer = new StringBuffer();
        dump2(resolved_node,format,entities,1,context,buffer);
        out.write(buffer.toString());
			
		/**
		 * End doAction logic
		 */

	}
    
	private void dump2(Object node, boolean format, boolean entities, int indent, JET2Context context, StringBuffer out)
    {
      final String CHILD_NODES_XPATH = "child::node()"; //$NON-NLS-1$
      final String ATTRIBUTEs_XPATH = "attribute::*"; //$NON-NLS-1$
      final String ATTRIBUTE_NAME_XPATH = "name()"; //$NON-NLS-1$
      final String NODE_VALUE_XPATH = "string()"; //$NON-NLS-1$
      XPathContextExtender xpc = XPathContextExtender.getInstance(context);
      
      if(node == null) return;
      INodeInspector inspector = InspectorManager.getInstance().getInspector(node);
      if(inspector == null) 
      {
        return;
      }
      
      final NodeKind nodeKind = inspector.getNodeKind(node);
      if(nodeKind == NodeKind.ELEMENT) 
      {
        out.append("<"); //$NON-NLS-1$
        String tagName = inspector.nameOf(node);
        if(tagName == null) tagName = "???"; //$NON-NLS-1$
        out.append(tagName);
        final Object[] attrs = xpc.resolve(node, ATTRIBUTEs_XPATH);
        for (int i = 0; attrs != null && i < attrs.length; i++)
        {
          out.append(" "); //$NON-NLS-1$
          String attrName = xpc.resolveAsString(attrs[i], ATTRIBUTE_NAME_XPATH);
          if(attrName == null) attrName = "???"; //$NON-NLS-1$
          out.append(attrName);
          out.append("="); //$NON-NLS-1$
          String value = xpc.resolveAsString(attrs[i], NODE_VALUE_XPATH);
          if(value == null) value = ""; //$NON-NLS-1$
          out.append(useRightQuotes(value, false));
        }
        final Object[] children = xpc.resolve(node, CHILD_NODES_XPATH);
        if(children != null && children.length > 0)
        {
          out.append(">"); //$NON-NLS-1$
          writeChildren(children, format, entities, indent, context, out);
          out.append("</"); //$NON-NLS-1$
          out.append(tagName);
          out.append(">"); //$NON-NLS-1$
        }
        else
        {
          out.append("/>"); //$NON-NLS-1$
        }
      }
      else if(nodeKind == NodeKind.TEXT)
      {
        String value = xpc.resolveAsString(node, NODE_VALUE_XPATH);
        if(value == null) value = ""; //$NON-NLS-1$
        if (entities) { value = insertEntitiesInContent(value); }
        out.append(value); 
      }
      else if(nodeKind == NodeKind.COMMENT)
      {
        out.append("<!--"); //$NON-NLS-1$
        String value = xpc.resolveAsString(node, NODE_VALUE_XPATH);
        if(value == null) value = ""; //$NON-NLS-1$
        out.append(value);
        out.append("-->"); //$NON-NLS-1$
      }
      else if(nodeKind == NodeKind.ROOT)
      {
        final Object[] children = xpc.resolve(node, CHILD_NODES_XPATH);
        if(children != null) {
          writeChildren(children, format, entities, 0, context, out);
        }
      }
      else if(nodeKind == NodeKind.PROCESSING_INSTRUCTION)
      {
        
      }
      else
      {
        
      }
    }

  /**
   * @param children
   * @param format
   * @param entities
   * @param indent
   * @param context
   * @param out
   */
  private void writeChildren(final Object[] children, boolean format, boolean entities, int indent, JET2Context context, StringBuffer out)
  {
    boolean textEncountered = false;
    for (int i = 0; i < children.length; i++)
    {
      if(children[i] == null) {
        continue;
      }
      INodeInspector childInspector = InspectorManager.getInstance().getInspector(children[i]);

      textEncountered = textEncountered || (childInspector != null && childInspector.getNodeKind(children[i]) == NodeKind.TEXT);
      if(!textEncountered && format)
      {
        out.append(NL);
        indent(out, indent);
      }
      
      dump2(children[i], format, entities, indent + 1, context, out);

    }
    if(!textEncountered && format)
    {
      out.append(NL);
      indent(out, indent - 1);
    }
  }

  /**
   * @param out
   * @param indent
   */
  private void indent(StringBuffer out, int indent)
  {
    for (int j = 0; j < indent; j++) { out.append("    "); } //$NON-NLS-1$
  }

  public void dump(Object node, boolean format, boolean entities, int indent, JET2Context context, JET2Writer out) {
		
		boolean written = false;
		boolean writeKids = false;
		INodeInspector inspector = InspectorManager.getInstance().getInspector(node);
		if (inspector == null) { return; }
		if (inspector.getNodeKind(node) == NodeKind.ELEMENT) { 
			IElementInspector elementInspector = (IElementInspector) inspector;
			out.write("<"+inspector.nameOf(node)); //$NON-NLS-1$
			Object attr[] = elementInspector.getAttributes(node);
			for (int i = 0; i < attr.length; i++ ) {
				INodeInspector ei = InspectorManager.getInstance().getInspector(attr[i]);
				String key = ei.nameOf(attr[i]);
				String value = ei.stringValueOf(attr[i]);
				out.write(" "+key+"="+useRightQuotes(value)); //$NON-NLS-1$ //$NON-NLS-2$
			}
			boolean staticEncountered = false;
			Object kid[] = elementInspector.getChildren(node);
			if (kid.length > 0) {
				out.write(">"); //$NON-NLS-1$
				for (int index = 0; index < kid.length; index++) {
					INodeInspector kidInspector = InspectorManager.getInstance().getInspector(kid[index]);
					if (kidInspector.getNodeKind(kid[index]) == NodeKind.ELEMENT) {
						if (!staticEncountered && format) {
							out.write(NL);
							for (int i = 0; i < indent; i++) { out.write("    "); } //$NON-NLS-1$
							staticEncountered = false;
						}	
					} else {
						staticEncountered = true;
					}
					dump(kid[index],format,entities,(indent+1),context,out);
				}
				if (!staticEncountered && format) {
					out.write(NL);
					for (int i = 1; i < indent; i++) { out.write("    "); } //$NON-NLS-1$
					staticEncountered = false;
				}	
				out.write("</"+inspector.nameOf(node)+">");  //$NON-NLS-1$//$NON-NLS-2$
			} else {
				out.write("/>"); //$NON-NLS-1$
			}
			written = true;
		}
		if (inspector.getNodeKind(node) == NodeKind.TEXT) {
			String buffer = inspector.stringValueOf(node);
			if(buffer == null) buffer = ""; //$NON-NLS-1$
//			if (node.isCDataNode()) {
//				out.write("<![CDATA["+buffer+"]]>"); 
//				written = true;
//			} else {
				if (entities) { buffer = insertEntitiesInContent(buffer); }
				out.write(buffer); 
//			}
			written = true;
		}
		if (inspector.getNodeKind(node) == NodeKind.ROOT) { 
//			out.write("<?xml version=\""+((Document) node).getVersion()+"\" encoding=\""+((Document) node).getEncoding()+"\"?>\r\n"); 
			written = true;
			writeKids = true;
		}
		if (!written) {
			System.out.println("Did not write type "+inspector.getNodeKind(node)); //$NON-NLS-1$
		}
		if (writeKids) {
			IElementInspector elementInspector = (IElementInspector) inspector;
			Object kid[] = elementInspector.getChildren(node);
			for (int index = 0; index < kid.length; index++) {
				dump(kid[index],format,entities,(indent+1),context,out);
			}
		}
	}

	/**
     * Method useRightQuotes.
     * @param string
     * @return String
     * @deprecated Use {@link #useRightQuotes(String,boolean)} instead
     */
    private String useRightQuotes(String string) {
    return useRightQuotes(string, false);
  }

  /**
	 * Method useRightQuotes.
	 * @param string
	 * @param entities TODO
	 * @return String
	 */
	private String useRightQuotes(String string, boolean entities) {
		final boolean containsDoubleQuote = string.indexOf("\"") > -1; //$NON-NLS-1$
		if(entities) {
		  if (containsDoubleQuote) {
			return "'"+insertEntitiesInAttribute(string, '\'')+"'";	 //$NON-NLS-1$ //$NON-NLS-2$
		  }
		  return "\""+insertEntitiesInAttribute(string, '"')+"\""; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
          if (containsDoubleQuote) {
            return "'"+string+"'";  //$NON-NLS-1$ //$NON-NLS-2$
          }
          return "\""+string+"\""; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Insert required XML entities for an attribute value
	 * @param value the attribute value
	 * @param delimiter the attribute delimiter (' or ")
	 * @return the modified string
	 */
	private String insertEntitiesInAttribute(String value, char delimiter) {
      final String charsToReplace = delimiter == '\'' ? "<&'" : "<&\""; //$NON-NLS-1$ //$NON-NLS-2$

      return insertEntities(value, charsToReplace);
	  
	}
	
    /**
     * Insert XML entities in XML content (text contained by a tag).
     * @param text the text to modify
     * @return the modified text
     */
    private String insertEntitiesInContent(String text) {
      return insertEntities(text, "<>&'\""); //$NON-NLS-1$
    }

    /**
     * Insert XML entities for the characters specified in charsToReplace.
     * @param text the text to modify
     * @param charsToReplace the chars for which entities will be replaced
     * @return the modified string
     */
    private String insertEntities(String text, final String charsToReplace)
    {
      StringBuffer sb = new StringBuffer(text.length() * 2);
      StringTokenizer st = new StringTokenizer(text,charsToReplace ,true); 
      while (st.hasMoreTokens()) {
          String token = st.nextToken();
          if(token.length() == 1) {
            switch(token.charAt(0)) {
              case '<':
                sb.append("&lt;"); //$NON-NLS-1$
                break;
              case '>':
                sb.append("&gt;"); //$NON-NLS-1$
                break;
              case '&':
                sb.append("&amp;"); //$NON-NLS-1$
                break;
              case '\'':
                sb.append("&apos;"); //$NON-NLS-1$
                break;
              case '"':
                sb.append("&quot;"); //$NON-NLS-1$
                break;
              default:
                sb.append(token);
            }
          } else {
            sb.append(token);
          }
      }
      String buffer = sb.toString();
      return buffer;
    }

	/**
	 * End custom methods
	 */

}
