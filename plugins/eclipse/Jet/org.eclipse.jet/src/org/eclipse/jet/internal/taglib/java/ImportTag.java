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

package org.eclipse.jet.internal.taglib.java;


import org.eclipse.emf.codegen.util.ImportManager;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.taglib.AbstractFunctionTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.taglib.java.JavaActionsUtil;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;


/**
 * Implement the JET2 standard Java tag 'import'. 
 *
 */
public class ImportTag extends AbstractFunctionTag
{

  /**
   * 
   */
  public ImportTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.FunctionTag#doFunction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, java.lang.String)
   */
  public String doFunction(TagInfo tc, JET2Context context, String bodyContent) throws JET2TagException
  {
    final ImportManager importManager = JavaActionsUtil.getImportManager(getOut());
    ASTParser parser = ASTParser.newParser(AST.JLS3);
    
    // create a jface Document to track positions of replaceable text...
    final IDocument document = new Document();
    document.set(bodyContent);
    
    // create a Java statement with the type declaration
    String varDeclStatement = bodyContent + " x;"; //$NON-NLS-1$
    // ... and get an AST for it ...
    parser.setSource(varDeclStatement.toCharArray());
    parser.setKind(ASTParser.K_STATEMENTS);
    Block syntheticBlock = (Block)parser.createAST(null);
    
    // visit the AST, looking for Java 'simple types'
    syntheticBlock.accept(new ASTVisitor() {
      public boolean visit(SimpleType node)
      {
        // register the fully qualified name with the import manager
        final String fullyQualifiedName = node.getName().getFullyQualifiedName();
        importManager.addImport(fullyQualifiedName);
        try
        {
          // register the position so we can replace it later, once we have
          // recorded the positions of all the qualified names...
          document.addPosition(new Position(
            node.getName().getStartPosition(),
            node.getName().getLength()));
        }
        catch (BadLocationException e)
        {
          // should not happen, ignore
        }
        return super.visit(node);
      }
    });

    // Now run through all saved positions, and replace the original text with
    // the short name from the import manager...
    try
    {
      final Position[] positions = document.getPositions(IDocument.DEFAULT_CATEGORY);
      for (int i = 0; i < positions.length; i++)
      {
        String qualifiedName = document.get(positions[i].offset, positions[i].length);
        document.replace(positions[i].offset, positions[i].length, importManager.getImportedName(qualifiedName));
      }
    }
    catch (BadPositionCategoryException e)
    {
      // should not happen, ignore
    }
    catch (BadLocationException e)
    {
      // should not happen, ignore
    }
    
    
    return document.get();
  }

}
