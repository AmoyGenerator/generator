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

package org.eclipse.jet.internal.compiler;


import org.eclipse.jet.BodyContentWriter;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Template;
import org.eclipse.jet.JET2TemplateLoader;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.compiler.DefaultJET2ASTVisitor;
import org.eclipse.jet.compiler.JET2CompilationUnit;
import org.eclipse.jet.compiler.JET2Expression;
import org.eclipse.jet.compiler.JET2Scriptlet;
import org.eclipse.jet.compiler.TextElement;
import org.eclipse.jet.compiler.XMLBodyElement;
import org.eclipse.jet.compiler.XMLElement;
import org.eclipse.jet.compiler.XMLEmptyElement;
import org.eclipse.jet.transform.TransformContextExtender;


/**
 * A JET2 AST visitor that creates the 'generate' method body for the AST.
 *
 */
public class MethodBodyCreator extends DefaultJET2ASTVisitor
{
  private static final String NULL_KEYWORD = "null"; //$NON-NLS-1$

  private static final String UNDERSCORE = "_"; //$NON-NLS-1$

  private static final String TAG_INFO_PREFIX = "_jettd_"; //$NON-NLS-1$

  private static final String TAG_PREFIX = "_jettag_"; //$NON-NLS-1$

  private java.util.Stack varStack = new java.util.Stack();

  private final JET2TemplateLoader templateLoader;
  private final JET2Context context;
  private final JET2Writer out = new BodyContentWriter();
  private String indent = "        "; //$NON-NLS-1$

  public MethodBodyCreator(JET2TemplateLoader templateLoader) {
    this.templateLoader = templateLoader;
    context = new JET2Context(null);
    TransformContextExtender.getInstance(context);
  }
  /**
   * Return the name of a unique variable to refer to the passed element.
   * @param element
   * @return the variable name
   */
  public static String tagVariableName(XMLElement element)
  {
    return TAG_PREFIX + tagVariableSuffix(element);
  }

  /**
   * Return the name of a unique variable to refer to the static information about the passed element.
   * @param element
   * @return the variable name
   */
  public static String tagInfoVariableName(XMLElement element)
  {
    return TAG_INFO_PREFIX + tagVariableSuffix(element);
  }

  /**
   * Create a unique suffix of all variables that refer to the passed element
   * @param element an XML Element
   * @return the tag variable name
   */
  private static String tagVariableSuffix(XMLElement element)
  {
    return element.getNSPrefix() + UNDERSCORE + element.getTagNCName() + UNDERSCORE + element.getLine() + UNDERSCORE
      + element.getColumn();
  }

  /**
   * @see org.eclipse.jet.compiler.DefaultJET2ASTVisitor#visit(org.eclipse.jet.compiler.XMLBodyElement)
   */
  public void visit(XMLBodyElement element)
  {
    GenXMLElement genElement = buildGenElement(element);
    varStack.push(genElement.getTagVariable());
    generate("templates/xmlStart.jet", indent, genElement); //$NON-NLS-1$
    indent = indent + "    "; //$NON-NLS-1$
  }

  /**
   * @see org.eclipse.jet.compiler.DefaultJET2ASTVisitor#endVisit(org.eclipse.jet.compiler.XMLBodyElement)
   */
  public void endVisit(XMLBodyElement element)
  {
    indent = indent.substring(0, indent.length() - 4);
    GenXMLElement genElement = buildGenElement(element);
    generate("templates/xmlEnd.jet", indent, genElement); //$NON-NLS-1$
    varStack.pop();
  }

  /** 
   * @see org.eclipse.jet.compiler.DefaultJET2ASTVisitor#visit(org.eclipse.jet.compiler.XMLEmptyElement)
   */
  public void visit(XMLEmptyElement element)
  {
    GenXMLElement genElement = buildGenElement(element);
    
    generate("templates/xmlStart.jet", indent, genElement); //$NON-NLS-1$
    generate("templates/xmlEnd.jet", indent, genElement); //$NON-NLS-1$
  }

  /**
   * @param element
   * @return
   */
  private GenXMLElement buildGenElement(XMLElement element)
  {
    String tagVarName = tagVariableName(element);
    String parentTagVar = varStack.empty() ? NULL_KEYWORD : (String)varStack.peek();
    return new GenXMLElement(element.getDelegate(), tagVarName, tagInfoVariableName(element), parentTagVar);
  }

  /**
   * @see org.eclipse.jet.compiler.DefaultJET2ASTVisitor#visit(org.eclipse.jet.compiler.JET2Expression)
   */
  public void visit(JET2Expression expression)
  {
    generate("templates/writeJavaExpression.jet", indent, expression); //$NON-NLS-1$
  }

  /**
   * @see org.eclipse.jet.compiler.DefaultJET2ASTVisitor#visit(org.eclipse.jet.compiler.JET2Scriptlet)
   */
  public void visit(JET2Scriptlet scriptlet)
  {
    out.write(scriptlet.getJavaContent());
  }

  /**
   * @see org.eclipse.jet.compiler.DefaultJET2ASTVisitor#visit(org.eclipse.jet.compiler.TextElement)
   */
  public void visit(TextElement text)
  {
    generate("templates/writeTextElement.jet", indent, text); //$NON-NLS-1$
  }

  public void visit(JET2CompilationUnit compilationUnit)
  {
    
  }
  
  public void endVisit(JET2CompilationUnit compilationUnit)
  {
    super.endVisit(compilationUnit);
  }
  /**
   * Return the underlying string buffer container the generated contents.
   * @return the string buffer
   */
  public String getBuffer()
  {
    return out.toString();
  }

  private void generate(String templatePath, String indent, Object element) {
      context.setVariable("indent", indent); //$NON-NLS-1$
      context.setVariable("element", element); //$NON-NLS-1$
      JET2Template template = templateLoader.getTemplate(templatePath);
      template.generate(context, out);
  }

}
