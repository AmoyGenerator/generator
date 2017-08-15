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
 * $Id: DeepIterateTag.java,v 1.2 2009/05/02 02:58:59 pelder Exp $
 */
package org.eclipse.jet.internal.taglib.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jet.BufferedJET2Writer;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.XPathContextExtender;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.taglib.DocumentHelper;
import org.eclipse.jet.internal.taglib.DocumentHelper.InsertAfterEmptyPosition;
import org.eclipse.jet.taglib.AbstractIteratingTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Implement &lt;c:deepIterate&gt; tag.
 */
public class DeepIterateTag extends AbstractIteratingTag
{

  /**
     * @author pelder
     */
  static class LoopStatus extends DeepIterateTag
  {
    private final int depth;
    
    private boolean leaf;

    public LoopStatus(int depth) {
      this.depth = depth;
      
    }

    public LoopStatus(int depth, boolean leaf)
    {
      this.depth = depth;
      this.leaf = leaf;
    }

    /**
     * @return Returns the depth.
     */
    final int getDepth()
    {
      return depth;
    }

    /**
     * @param leaf The leaf to set.
     */
    public void setLeaf(boolean leaf)
    {
      this.leaf = leaf;
    }

    /**
     * @return Returns the leaf.
     */
    public boolean isLeaf()
    {
      return leaf;
    }
    
    public Object asInspectableObject() {
      try
      {
        final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        final Element status = document.createElement("status"); //$NON-NLS-1$
        document.appendChild(status);
        status.setAttribute("depth", Integer.toString(depth)); //$NON-NLS-1$
        if(leaf) {
          status.setAttribute("isLeaf", "true");  //$NON-NLS-1$//$NON-NLS-2$
        }
        return status;
      }
      catch (DOMException e)
      {
        // shouldn't happen - all names passed are valid XML names...
        throw new RuntimeException("Internal Error: An XML name is unexpectedly invalid", e); //$NON-NLS-1$
      }
      catch (ParserConfigurationException e)
      {
        // shouldn't happen - we're using defaults throughout
        throw new RuntimeException("Internal Error: DocumentBuilder could not be created.", e); //$NON-NLS-1$
      }
      catch (FactoryConfigurationError e)
      {
        // shouldn't happen...
        throw new RuntimeException("Internal Error: DocumentBuilderFactory could not be created.", e); //$NON-NLS-1$
      }
    }
  }

  /**
   * Track location of nested &lt;c:deepContent&gt; tags
   */
  private static final class DeepContentPositionTracker {
    
    private List insertPositions = new ArrayList();

    public DeepContentPositionTracker() {
      insertPositions.add(null);
    }
    /**
     * @return Returns the currentInsertPosition.
     */
    final Position getInsertPosition()
    {
      final int size = insertPositions.size();
      if(size < 2) {
        throw new IllegalStateException("size = " + size); //$NON-NLS-1$
      }
      return (Position)insertPositions.get(size - 2);
    }
    
    final void initDepth(int newDepth) {
      final int oldDepth = insertPositions.size() - 1;
      if(newDepth < 1 || newDepth > oldDepth + 1) {
        throw new IllegalArgumentException("newdepth = " + newDepth + ", oldDepth = " + oldDepth);  //$NON-NLS-1$//$NON-NLS-2$
      }
      // clean-up no longer accessible insert positions...
      for(int i = oldDepth; i >= newDepth; i--) {
         insertPositions.remove(i);
      }
      insertPositions.add(null); // initially now position at this level...
    }
    
    final void setPosition(Position position) {
      insertPositions.set(insertPositions.size() - 1, position);
    }
    
  }
  
  private static final String INSERTION_POINT_CATEGORY = DeepIterateTag.class.getName() + ".INSERT_POSITION"; //$NON-NLS-1$
  private String tagName;
  private String var;
  private String varStatus;
  private Iterator iterator;
  private Object savedVarValue;
  private Object savedVarStatusValue;
  private String indent;
  private DeepIterateEntry currentEntry;
  
  private DeepContentPositionTracker deepContentPositionTracker;
  
  private BufferedJET2Writer workingOutput;
  private IDeepIterateStrategy strategy;

  /**
   * 
   */
  public DeepIterateTag()
  {
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.IteratingTag#doInitializeLoop(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context)
   */
  public void doInitializeLoop(TagInfo td, JET2Context context) throws JET2TagException
  {
    // get attribute values
    tagName = td.getTagName();
    var = getAttribute("var"); //$NON-NLS-1$
    varStatus = getAttribute("varStatus"); //$NON-NLS-1$
    indent = getAttribute("indent"); //$NON-NLS-1$

    // save variables we will be setting...
    savedVarValue = saveVariable(context, var);
    savedVarStatusValue = saveVariable(context, varStatus);

    // build result set...
    DeepIteratorStrategyBuilder builder = new DeepIteratorStrategyBuilder(context, tagName, getAttribute("select")) //$NON-NLS-1$
      .var(var)
      .traversal(getAttribute("traversal")) //$NON-NLS-1$
      .varStatus(varStatus)
      .allowDuplicates(td.hasAttribute("allowDuplicates") ? Boolean.getBoolean(td.getAttribute("allowDuplicates")) : true) //$NON-NLS-1$ //$NON-NLS-2$
      .filter(getAttribute("filter")); //$NON-NLS-1$
    
    final String contextSelect = getAttribute("initialContext"); //$NON-NLS-1$
    if(contextSelect != null) {
      final XPathContextExtender xpc = XPathContextExtender.getInstance(context);
      builder.contextObject(xpc.resolveSingle(contextSelect));
    }
    
    strategy = builder.build();
    
    final Collection result = strategy.search();
    
    // setup for nested deepContent tags...
    if(strategy.supportsDeepContent()) {
      deepContentPositionTracker = new DeepContentPositionTracker();
    }
    
    // initialize the working output...
    initWorkingOutput();
    
    // get a result iterator
    iterator = result.iterator();
  }

  /**
   * @param context
   * @param varName
   * @return
   * @throws JET2TagException
   */
  private Object saveVariable(JET2Context context, String varName) throws JET2TagException
  {
    Object savedValue = null;
    if(varName != null) {
      savedValue = context.hasVariable(varName) ? context.getVariable(varName) : null;
    }
    return savedValue;
  }

  /**
   * 
   */
  private void initWorkingOutput()
  {
    workingOutput = (BufferedJET2Writer)getOut().newNestedContentWriter();
    IDocument doc = (IDocument)workingOutput.getAdapter(IDocument.class);
    DocumentHelper.installPositionCategory(doc, INSERTION_POINT_CATEGORY);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.IteratingTag#doEvalLoopCondition(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context)
   */
  public boolean doEvalLoopCondition(TagInfo td, JET2Context context) throws JET2TagException
  {
    final boolean hasNext = iterator.hasNext();
    if(!hasNext) {
      restoreVariable(context, var, savedVarValue);
      restoreVariable(context, varStatus, savedVarStatusValue);
    }
    if(!hasNext) {
      flushWorkingOutput();
    }
    return hasNext;
  }

  /**
   * @param context
   * @param varName
   * @param savedValue
   * @throws JET2TagException
   */
  private void restoreVariable(JET2Context context, String varName, Object savedValue) throws JET2TagException
  {
    if(varName != null) {
    if(savedValue != null) {
      context.setVariable(varName, savedValue);
    } else {
      context.removeVariable(varName);
    }
    }
  }

  /**
   * 
   */
  private void flushWorkingOutput()
  {
    IDocument doc = (IDocument)workingOutput.getAdapter(IDocument.class);
    try
    {
      doc.removePositionCategory(INSERTION_POINT_CATEGORY);
    }
    catch (BadPositionCategoryException e)
    {
      // shouldn't happen
      throw new RuntimeException("Internal Error: Position category has not been added: " + INSERTION_POINT_CATEGORY, e); //$NON-NLS-1$
    }
    // flush working output...
    getOut().write(workingOutput);
    
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.AbstractIteratingTag#doBeforeBody(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doBeforeBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    super.doBeforeBody(td, context, out);
    
    currentEntry = (DeepIterateEntry)iterator.next();
    
    // ready insert position tracker, if it exists...
    if(deepContentPositionTracker != null) {
      deepContentPositionTracker.initDepth(currentEntry.getDepth());
    }
    
    // setup context object
    final XPathContextExtender xpc = XPathContextExtender.getInstance(context);
    xpc.pushXPathContextObject(currentEntry.getObject());
    
    setVariable(context, var, currentEntry.getObject());
    if(varStatus != null) {
      LoopStatus status = new LoopStatus(currentEntry.getDepth(), currentEntry.isLeaf());
      setVariable(context, varStatus, status.asInspectableObject());
    }
  }

  /**
   * @param context
   * @param varName
   * @param varValue
   * @throws JET2TagException
   */
  private void setVariable(JET2Context context, String varName, Object varValue) throws JET2TagException
  {
    if(varName != null) {
      context.setVariable(varName, varValue);
    }
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.AbstractIteratingTag#doAfterBody(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public void doAfterBody(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    super.doAfterBody(td, context, out);
    
    final XPathContextExtender xpc = XPathContextExtender.getInstance(context);
    xpc.popXPathContextObject();
  }
  
  
  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.AbstractContainerTag#setBodyContent(org.eclipse.jet.JET2Writer)
   */
  public void setBodyContent(JET2Writer bodyContent)
  {
    if(indent != null) {
      DocumentHelper.indent((BufferedJET2Writer)bodyContent, currentEntry.getDepth() - 1, indent);
    }
    Position insertPosition = deepContentPositionTracker != null
      ? deepContentPositionTracker.getInsertPosition()
      : null;
    if(insertPosition != null) {
      replaceContent(workingOutput, (InsertAfterEmptyPosition)insertPosition, (BufferedJET2Writer)bodyContent);
    } else {
      workingOutput.write(bodyContent);
    }
  }
  
  private void replaceContent(BufferedJET2Writer targetWriter, InsertAfterEmptyPosition insertPosition, BufferedJET2Writer sourceWriter)
  {
    final IDocument targetDocument = (IDocument)targetWriter.getAdapter(IDocument.class);
    // save insertPosition.offset. The position replaceContent operation will update it, but we
    // need the original offset for later on...
    final int insertionOffset = insertPosition.offset;
    insertPosition.setEnableInsertBefore(true);
    targetWriter.replaceContent(insertionOffset, insertPosition.length, sourceWriter.getContent());
    insertPosition.setEnableInsertBefore(false);
    try
    {
      // transfer position categories, position updaters and positions
      final IDocument sourceDocument = (IDocument)sourceWriter.getAdapter(IDocument.class);

      // Position updaters should not be copied, instead, they will be added as part of adding
      // any new categories.
      final String categories[] = sourceDocument.getPositionCategories();
      for (int i = 0; i < categories.length; i++)
      {
        // ensure 'document' has the category - no harm in doing this if the category is already there
        DocumentHelper.installPositionCategory(targetDocument, categories[i]);
        final Position[] positions = sourceDocument.getPositions(categories[i]);
        for (int j = 0; j < positions.length; j++)
        {
          sourceDocument.removePosition(categories[i], positions[j]);
          positions[j].setOffset(positions[j].getOffset() + insertionOffset);
          targetDocument.addPosition(categories[i], positions[j]);
        }
      }
    }
    catch (BadPositionCategoryException e)
    {
      // this exception should not occur in this circumstance. We are getting categories
      // from the document we are applying them against, so we should never encounter problems.
      InternalJET2Platform.logError("Internal Error", e); //$NON-NLS-1$
    }
    catch (BadLocationException e)
    {
      // this exception should not occur in this circumstance. If we are creating invalid positions
      // then this is a sign of programming errors.
      InternalJET2Platform.logError("Internal Error", e); //$NON-NLS-1$
    }
  }

  public void markContentInsertionPoint(BufferedJET2Writer out) throws JET2TagException {
    
    strategy.checkDeepContentAllowed();
    
    // get the underlying document, and ensure it has the correct position category...
    IDocument document = (IDocument)out.getAdapter(IDocument.class);
    DocumentHelper.installPositionCategory(document, INSERTION_POINT_CATEGORY);
    
    // add a position
    final Position position = new DocumentHelper.InsertAfterEmptyPosition(document.getLength());
    try
    {
      document.addPosition(INSERTION_POINT_CATEGORY, position);
    }
    catch (BadLocationException e)
    {
      // shouldn't happen
      throw new RuntimeException("Internal Error: Unexpected invalid position: " + position, e); //$NON-NLS-1$
    }
    catch (BadPositionCategoryException e)
    {
      // shouldn't happen
      throw new RuntimeException("Internal Error: Position category has not been added: " + INSERTION_POINT_CATEGORY, e); //$NON-NLS-1$
    }
    
    // remember the insert position
    
    deepContentPositionTracker.setPosition(position);
  }


}
