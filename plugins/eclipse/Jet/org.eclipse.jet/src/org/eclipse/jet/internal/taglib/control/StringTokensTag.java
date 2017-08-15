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
import java.util.Vector;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.XPathContextExtender;
import org.eclipse.jet.internal.exceptions.MissingRequiredAttributeException;
import org.eclipse.jet.taglib.AbstractIteratingTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.transform.TransformContextExtender;

/**
 * Implement the Contributed Control Tags tag 'stringTokens'.
 *
 */
public class StringTokensTag extends AbstractIteratingTag {


	private String _string = null;

	private String _delimitedBy = null;

	private String _name = null;

	private String _delimiter = null;

	private String _reverse = null;

	private String _tokenLength = null;

	/**
	 *  Begin custom declarations
	 */

	private Object		tokenNode = null;
	private StringToken tokens[] = null;
	private int			index = 0;
	
	/**
	 * End custom declarations
	 */

	public StringTokensTag() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.taglib.IteratingTag#doEvalLoopCondition(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context)
	 */
	public boolean doEvalLoopCondition(TagInfo tagInfo, JET2Context context)
			throws JET2TagException {

		boolean doAnotherIteration = false;
		
		/**
	 	 *  Begin doEvalLoopCondition logic
		 */

		doAnotherIteration = index < tokens.length;
		if (!doAnotherIteration) { return false; }
		
	    context.setVariable(_name, tokenNode);

		XPathContextExtender xpathContext = XPathContextExtender.getInstance(context);

		xpathContext.setAttribute(tokenNode,"value",tokens[index].getToken()); //$NON-NLS-1$
		xpathContext.setAttribute(tokenNode,"delimiter",tokens[index].getDelimiter()); //$NON-NLS-1$
		xpathContext.setAttribute(tokenNode,"first",String.valueOf(tokens[index].isFirst())); //$NON-NLS-1$
		xpathContext.setAttribute(tokenNode,"last",String.valueOf(tokens[index].isLast())); //$NON-NLS-1$
		xpathContext.setAttribute(tokenNode,"index",String.valueOf(index)); //$NON-NLS-1$

		index++;
			
		/**
		 * End doEvalLoopCondition logic
		 */
		
		return doAnotherIteration;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jet.taglib.IteratingTag#doInitializeLoop(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context)
	 */
	public void doInitializeLoop(TagInfo tagInfo, JET2Context context)
			throws JET2TagException {

		XPathContextExtender xpathContext = XPathContextExtender.getInstance(context);

		/**
		 * Get the "string" attribute and convert any embedded dynamic attributes
		 * into dynamic XPath expressions.
		 */
		_string = getAttribute("string"); //$NON-NLS-1$
		if (_string == null) {
			throw new MissingRequiredAttributeException("string"); //$NON-NLS-1$
		}

		/**
		 * Get the "delimitedBy" attribute and convert any embedded dynamic attributes
		 * into dynamic XPath expressions.
		 */
		_delimitedBy = getAttribute("delimitedBy"); //$NON-NLS-1$

		/**
		 * Get the "name" attribute and convert any embedded dynamic attributes
		 * into dynamic XPath expressions.
		 */
		_name = getAttribute("name"); //$NON-NLS-1$
		if (_name == null) {
			throw new MissingRequiredAttributeException("name"); //$NON-NLS-1$
		}

		/**
		 * Get the "delimiter" attribute and convert any embedded dynamic attributes
		 * into dynamic XPath expressions.
		 */
		_delimiter = getAttribute("delimiter"); //$NON-NLS-1$

		/**
		 * Get the "reverse" attribute and convert any embedded dynamic attributes
		 * into dynamic XPath expressions.
		 */
		_reverse = getAttribute("reverse"); //$NON-NLS-1$

		/**
		 * Get the "tokenLength" attribute and convert any embedded dynamic attributes
		 * into dynamic XPath expressions.
		 */
		_tokenLength = getAttribute("tokenLength"); //$NON-NLS-1$

		/**
	 	 *  Begin doInitializeLoop logic
		 */

	    try {
			Object modelRoot = TransformContextExtender.loadModelFromString("<token/>", null , "xml"); //$NON-NLS-1$ //$NON-NLS-2$
			tokenNode = xpathContext.resolveSingle(modelRoot, "/token" ); //$NON-NLS-1$

			boolean reverse = false;
			if ("true".equalsIgnoreCase(_reverse)) { reverse = true; } //$NON-NLS-1$
			if ("yes".equalsIgnoreCase(_reverse))  { reverse = true; } //$NON-NLS-1$
			
			int tokenLength = -1;
			try { tokenLength = Integer.parseInt(_tokenLength); } catch (Throwable t) {}
			
			tokens = tokenize(_string,_delimitedBy,tokenLength,reverse);
			
			setDelimiter(_delimiter);
		} catch (Exception e) {
			throw new JET2TagException(e);
		}
	    
		/**
		 * End doInitializeLoop logic
		 */

	}

	/*
	 *  Begin tag-specific methods
	 */

	public void doAfterBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException {
		super.doAfterBody(td, context, out);
		context.removeVariable(_name);
	}

	public void doBeforeBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException {
		super.doBeforeBody(td, context, out);
	}

	
	private StringToken[] tokenize(String string, String delimiters, int tokenLength, boolean reverse) {
		Vector v = new Vector();
		
		if (tokenLength > 0) {
			for (int i = 0; i < string.length(); i =i + tokenLength) {
				StringToken token = new StringToken();
				token.setToken(string.substring(i,i+tokenLength));
				token.setDelimiter(""); //$NON-NLS-1$
				token.setFirst(false);
				token.setLast(false);
				v.addElement(token);
			}
		} else if (delimiters.length() == 0) {
			StringTokenizer st = new StringTokenizer(string);
			while (st.hasMoreTokens()) {
				StringToken token = new StringToken();
				token.setToken(st.nextToken());
				token.setDelimiter(""); //$NON-NLS-1$
				token.setFirst(false);
				token.setLast(false);
				v.addElement(token);
			}	
		} else {
			StringTokenizer st = new StringTokenizer(string,delimiters,true);
			String tok = ""; //$NON-NLS-1$
			while (st.hasMoreTokens()) {
				String val = st.nextToken();
				if ((val.length() == 1) && (delimiters.indexOf(val) > -1)) {
					StringToken token = new StringToken();
					token.setToken(tok);
					token.setDelimiter(val);
					token.setFirst(false);
					token.setLast(false);
					v.addElement(token);
					tok = ""; //$NON-NLS-1$
				} else {
					tok = val;
				}
			}
			if (tok.length() > 0) {
				StringToken token = new StringToken();
				token.setToken(tok);
				token.setDelimiter(""); //$NON-NLS-1$
				token.setFirst(false);
				token.setLast(false);
				v.addElement(token);
			}
		}
		
		StringToken result[] = new StringToken[v.size()];
		v.copyInto(result);
		if (reverse) {
			StringToken backward[] = new StringToken[result.length];	
			for (int i = 0; i < result.length; i++) {
				backward[i] = result[result.length - 1 - i];
			}
			result = backward;	
		}
		if (result.length > 0) {
			result[0].setFirst(true);
			result[result.length - 1].setLast(true);
		}
		return result;
	}

	private class StringToken {

		private String	token;
		private String delimiter;
		private boolean first;
		private boolean last;
		/**
		 * Constructor for StringToken.
		 */
		public StringToken() {
			super();
		}

		/**
		 * Returns the first.
		 * @return boolean
		 */
		public boolean isFirst() {
			return first;
		}

		/**
		 * Returns the last.
		 * @return boolean
		 */
		public boolean isLast() {
			return last;
		}

		/**
		 * Returns the token.
		 * @return String
		 */
		public String getToken() {
			return token;
		}

		/**
		 * Sets the first.
		 * @param first The first to set
		 */
		public void setFirst(boolean first) {
			this.first = first;
		}

		/**
		 * Sets the last.
		 * @param last The last to set
		 */
		public void setLast(boolean last) {
			this.last = last;
		}

		/**
		 * Sets the token.
		 * @param token The token to set
		 */
		public void setToken(String token) {
			this.token = token;
		}

		/**
		 * Returns the delimiter.
		 * @return boolean
		 */
		public String getDelimiter() {
			return delimiter;
		}

		/**
		 * Sets the delimiter.
		 * @param delimiter The delimiter to set
		 */
		public void setDelimiter(String delimiter) {
			this.delimiter = delimiter;
		}
	}

	/*
	 * End tag-specific methods
	 */


}
