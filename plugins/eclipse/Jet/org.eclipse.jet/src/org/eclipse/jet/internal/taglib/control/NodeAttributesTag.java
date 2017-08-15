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

import java.io.IOException;

import org.eclipse.jet.CoreJETException;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.XPathContextExtender;
import org.eclipse.jet.internal.exceptions.MissingRequiredAttributeException;
import org.eclipse.jet.internal.exceptions.NoMatchingNodeException;
import org.eclipse.jet.taglib.AbstractIteratingTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.transform.TransformContextExtender;
import org.eclipse.jet.xpath.inspector.INodeInspector;
import org.eclipse.jet.xpath.inspector.InspectorManager;


/**
 * Implement the Contributed Control Tags tag 'nodeAttributes'.
 *
 */
public class NodeAttributesTag extends AbstractIteratingTag {


	private String _node = null;
	private Object resolved_node;

	private String _name = null;

	private String _delimiter = null;

	/**
	 *  Begin custom declarations
	 */

	private int 	index = -1;
	private Object	attr[];
	private Object	attrNode;

	/**
	 * End custom declarations
	 */

	public NodeAttributesTag() {
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

		index++;
		doAnotherIteration = index < attr.length;
			
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
		 * Get the "node" attribute
.
		 * Use that expression to retrieve a single node.
		 */
		_node = getAttribute("node"); //$NON-NLS-1$
		if (_node == null) {
			throw new MissingRequiredAttributeException("node"); //$NON-NLS-1$
		}
		resolved_node = xpathContext.resolveSingle(xpathContext.currentXPathContextObject(), _node);
		if (resolved_node == null) {
			throw new NoMatchingNodeException(_node);
		}

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
        setDelimiter(_delimiter);

		/**
	 	 *  Begin doInitializeLoop logic
		 */

		attr = new Object[0];
		index = -1;
		
		try {
			Object modelRoot = TransformContextExtender.loadModelFromString("<attribute/>", null , "xml"); //$NON-NLS-1$ //$NON-NLS-2$
			attrNode = xpathContext.resolveSingle(modelRoot, "/attribute" ); //$NON-NLS-1$
			
			attr = xpathContext.resolve(_node, "./@" ); //$NON-NLS-1$
		} catch (CoreJETException e) {
			throw new JET2TagException(e);
		} catch (IOException e) {
			throw new JET2TagException(e);
		} catch (JET2TagException e) {
			throw e;
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
		context.setVariable(_name, attrNode);
		XPathContextExtender xpathContext = XPathContextExtender.getInstance(context);
		INodeInspector insp = InspectorManager.getInstance().getInspector(attr[index]);
		xpathContext.setAttribute(attrNode, "name" , insp.nameOf(attr[index])); //$NON-NLS-1$
		xpathContext.setAttribute(attrNode, "value" , insp.stringValueOf(attr[index])); //$NON-NLS-1$
		xpathContext.setAttribute(attrNode, "index" , String.valueOf(index)); //$NON-NLS-1$
	}

	/*
	 * End tag-specific methods
	 */


}
