/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
 * $Id: InternalJET2Parser.java,v 1.4 2009/08/20 13:43:13 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet.internal.core.parser;


import java.io.CharArrayReader;
import java.net.URI;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jet.core.expressions.EmbeddedExpressionFactory;
import org.eclipse.jet.core.parser.IJETParser;
import org.eclipse.jet.core.parser.IProblem;
import org.eclipse.jet.core.parser.ITagLibraryResolver;
import org.eclipse.jet.core.parser.ITemplateInput;
import org.eclipse.jet.core.parser.ITemplateResolver;
import org.eclipse.jet.core.parser.ProblemSeverity;
import org.eclipse.jet.core.parser.TemplateInputException;
import org.eclipse.jet.core.parser.ast.Comment;
import org.eclipse.jet.core.parser.ast.EmbeddedExpression;
import org.eclipse.jet.core.parser.ast.JETAST;
import org.eclipse.jet.core.parser.ast.JETCompilationUnit;
import org.eclipse.jet.core.parser.ast.JETDirective;
import org.eclipse.jet.core.parser.ast.JavaDeclaration;
import org.eclipse.jet.core.parser.ast.JavaExpression;
import org.eclipse.jet.core.parser.ast.JavaScriptlet;
import org.eclipse.jet.core.parser.ast.TagLibraryUsageManager;
import org.eclipse.jet.core.parser.ast.TextElement;
import org.eclipse.jet.core.parser.ast.XMLBodyElement;
import org.eclipse.jet.core.parser.ast.XMLBodyElementEnd;
import org.eclipse.jet.core.parser.ast.XMLEmptyElement;
import org.eclipse.jet.internal.core.parser.jasper.CommentElementDelegate;
import org.eclipse.jet.internal.core.parser.jasper.DeclarationElementDelegate;
import org.eclipse.jet.internal.core.parser.jasper.EmbeddedExpressionElement;
import org.eclipse.jet.internal.core.parser.jasper.ErrorRedirectingCoreElementDelegate;
import org.eclipse.jet.internal.core.parser.jasper.JETCoreElement;
import org.eclipse.jet.internal.core.parser.jasper.JETException;
import org.eclipse.jet.internal.core.parser.jasper.JETMark;
import org.eclipse.jet.internal.core.parser.jasper.JETParseEventListener2;
import org.eclipse.jet.internal.core.parser.jasper.JETParser;
import org.eclipse.jet.internal.core.parser.jasper.JETReader;
import org.eclipse.jet.internal.core.parser.jasper.XMLElementDelegate;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.TagDefinition;
import org.eclipse.jet.taglib.TagLibraryReference;


/**
 * JET Parser Listener used by the JET2 Syntax
 *
 */
public class InternalJET2Parser implements JETParseEventListener2, IJETParser
{

  private static final String ID__ATTR = "id"; //$NON-NLS-1$

  private static final String PREFIX__ATTR = "prefix"; //$NON-NLS-1$

  private static final String TAGLIB__DIRECTIVE = "taglib"; //$NON-NLS-1$
  private static final String JET__DIRECTIVE = "jet"; //$NON-NLS-1$
  /**
   * Stack of elements waiting for end tags.
   */
  private final ElementStack elementStack = new ElementStack();

  private JETCompilationUnit compilationUnit;

  private JETAST ast;

  private final TagLibraryUsageManager tagLibManager;

  private JETReader reader;

  private final ITemplateResolver templateResolver;

  private final boolean supportEmbeddedExpressions;

  public InternalJET2Parser(ITemplateResolver templateResolver, ITagLibraryResolver tagLibraryResolver, Map predefinedLibraryMap, boolean supportEmbeddedExpressions)
  {
    this.templateResolver = templateResolver;
	this.supportEmbeddedExpressions = supportEmbeddedExpressions;
    tagLibManager = new TagLibraryUsageManager(predefinedLibraryMap, tagLibraryResolver);
  }
  /**
   * @see org.eclipse.jet.internal.parser.JETParseEventListener#beginPageProcessing()
   */
  public void beginPageProcessing()
  {
    // nothing to do
  }

  /**
   * @see org.eclipse.jet.internal.parser.JETParseEventListener#handleDirective(java.lang.String, org.eclipse.jet.internal.parser.JETMark, org.eclipse.jet.internal.parser.JETMark, java.util.Map)
   */
  public void handleDirective(String directive, JETMark start, JETMark stop, Map attributes)
  {
    JETDirective directiveImpl = ast.newJETDirective(start.getLine(), start.getCol(), start.getCursor(), stop.getCursor() + 1, directive, attributes);

    // although a directive may appear nested, it really isn't. Add it to the compilation unit.
    // TODO Does this work for @start and @end? Probably NOT.
    compilationUnit.addBodyElement(directiveImpl);

    if (TAGLIB__DIRECTIVE.equalsIgnoreCase(directive))
    {
      handleTagLibDirective(start, stop, attributes);
    }
    else if(JET__DIRECTIVE.equalsIgnoreCase(directive))
    {
      handleJetDirective(start, stop, attributes);
    } else {
      recordProblem(ProblemSeverity.WARNING, IProblem.UnsupportedDirective, 
        JET2Messages.ASTCompilerParseListener_UnsupportedDirective,
        new Object[] {directive}, start.getCursor(), stop.getCursor(), start.getLine(), start.getCol() );
    }
  }


  private static Set knownJETAttributes = 
    new LinkedHashSet(Arrays.asList(new String[] {
      "skeleton", //$NON-NLS-1$
      "package", //$NON-NLS-1$
      "imports", //$NON-NLS-1$
      "class", //$NON-NLS-1$
      "nlString", //$NON-NLS-1$
      "startTag", //$NON-NLS-1$
      "endTag", //$NON-NLS-1$
      "version", //$NON-NLS-1$
    }));
  private static Set deprecatedJETAttributes =
    new LinkedHashSet(Arrays.asList(new String[] {
      "skeleton", //$NON-NLS-1$
      "nlString", //$NON-NLS-1$
    }));

  private JETParser parser;
  private void handleJetDirective(JETMark start, JETMark stop, Map attributes)
  {
    for (Iterator i = attributes.keySet().iterator(); i.hasNext();)
    {
      String attrName = (String)i.next();
      if(!knownJETAttributes.contains(attrName)) 
      {
        recordProblem(ProblemSeverity.ERROR, IProblem.UnknownAttributeInTag, 
          JET2Messages.JET2Compiler_UnknownAttribute, new Object[] {attrName}, 
          start.getCursor(), stop.getCursor(), start.getLine(), start.getCol());
      }
      if(deprecatedJETAttributes.contains(attrName)) 
      {
        recordProblem(ProblemSeverity.WARNING, IProblem.DeprecatedAttribute, 
          JET2Messages.JET2Compiler_DeprecatedAttribute, new Object[] {attrName}, 
          start.getCursor(), stop.getCursor(), start.getLine(), start.getCol());
      }
    }
    
    String pkg = (String)attributes.get("package"); //$NON-NLS-1$
    String cls = (String)attributes.get("class"); //$NON-NLS-1$
    String importStr = (String)attributes.get("imports"); //$NON-NLS-1$
    String startTag = (String)attributes.get("startTag"); //$NON-NLS-1$
    String endTag = (String)attributes.get("endTag"); //$NON-NLS-1$

    if(pkg != null) 
    {
      compilationUnit.setOutputJavaPackage(pkg);
    }
    if(cls != null)
    {
      compilationUnit.setOutputJavaClassName(cls);
    }
    if(importStr != null)
    {
      String[] imports = importStr.split("\\s+"); //$NON-NLS-1$
      compilationUnit.addImports(Arrays.asList(imports));
    }

    if(startTag != null)
    {
      parser.setStartTag(startTag);
    }
    if(endTag != null)
    {
      parser.setEndTag(endTag);
    }
  }

  /**
   * @param start
   * @param stop
   * @param attributes
   */
  private void handleTagLibDirective(JETMark start, JETMark stop, Map attributes)
  {
    String prefix = ((String)attributes.get(PREFIX__ATTR)).trim().toLowerCase();
    String id = (String)attributes.get(ID__ATTR);

    if (id == null)
    {
      compilationUnit.createProblem(
        ProblemSeverity.ERROR,
        IProblem.MissingRequiredAttribute,
        JET2Messages.JET2Compiler_MissingDirectiveAttribute,
        new Object []{ TAGLIB__DIRECTIVE, ID__ATTR },
        start.getCursor(),
        stop.getCursor(),
        start.getLine(), start.getCol());
    }
    else if (prefix == null)
    {
      compilationUnit.createProblem(
        ProblemSeverity.ERROR,
        IProblem.MissingRequiredAttribute,
        JET2Messages.JET2Compiler_MissingDirectiveAttribute,
        new Object []{ TAGLIB__DIRECTIVE, PREFIX__ATTR },
        start.getCursor(),
        stop.getCursor(),
        start.getLine(), start.getCol());
    }
    else if (!tagLibManager.canDefinePrefix(prefix, id))
    {
      compilationUnit.createProblem(
        ProblemSeverity.ERROR,
        IProblem.DuplicateXMLNamespacePrefix,
        JET2Messages.JET2Compiler_PrefixAlreadyAssigned,
        new Object []{ prefix, tagLibManager.getLibraryIdFromPrefix(prefix) },
        start.getCursor(),
        stop.getCursor(),
        start.getLine(), start.getCol());
    }
    else if (tagLibManager.isLibraryDefined(id))
    {
      compilationUnit.createProblem(
        ProblemSeverity.ERROR,
        IProblem.UnknownTagLibrary,
        JET2Messages.JET2Compiler_UnknownTagLibrary,
        new Object []{ id },
        start.getCursor(),
        stop.getCursor(),
        start.getLine(), start.getCol());
    }
    else
    {
      tagLibManager.add(prefix, id);
    }
  }

  /**
   * @see org.eclipse.jet.internal.parser.JETParseEventListener#handleExpression(org.eclipse.jet.internal.parser.JETMark, org.eclipse.jet.internal.parser.JETMark, java.util.Map)
   */
  public void handleExpression(JETMark start, JETMark stop, Map attributes)
  {
    JavaExpression expression = ast.newJavaExpression(
      start.getLine(),
      start.getCol(),
      start.getCursor() - 3,
      stop.getCursor() + 2,
      start.getCursor(),
      stop.getCursor(), reader.getChars(start, stop));

    if (elementStack.isEmpty())
    {
      compilationUnit.addBodyElement(expression);
    }
    else
    {
      XMLBodyElement topElement = elementStack.peek();

      topElement.addBodyElement(expression);
    }

  }

  /**
   * @see org.eclipse.jet.internal.parser.JETParseEventListener#handleCharData(char[])
   */
  public void handleCharData(char[] chars)
  {
    TextElement text = ast.newTextElement(chars);
    if (elementStack.isEmpty())
    {
      compilationUnit.addBodyElement(text);
    }
    else
    {
      XMLBodyElement topElement = elementStack.peek();

      topElement.addBodyElement(text);
    }

  }

  /**
   * @see org.eclipse.jet.internal.parser.JETParseEventListener#endPageProcessing()
   */
  public void endPageProcessing()
  {
    while (!elementStack.isEmpty())
    {
      XMLBodyElement element = elementStack.pop();
      compilationUnit.createProblem(
        ProblemSeverity.ERROR,
        IProblem.MissingXmlEndTag,
        JET2Messages.JET2Compiler_MissingEndTag,
        new Object []{ element.getName() },
        element.getStart(),
        element.getEnd(),
        element.getLine(), element.getColumn());
    }

  }

  /**
   * @see org.eclipse.jet.internal.parser.JETParseEventListener#handleScriptlet(org.eclipse.jet.internal.parser.JETMark, org.eclipse.jet.internal.parser.JETMark, java.util.Map)
   */
  public void handleScriptlet(JETMark start, JETMark stop, Map attributes)
  {
    JavaScriptlet scriplet = ast.newJavaScriptlet(
      start.getLine(),
      start.getCol(),
      start.getCursor() - 2,
      stop.getCursor() + 2,
      start.getCursor(),
      stop.getCursor(), reader.getChars(start, stop));

    if (elementStack.isEmpty())
    {
      compilationUnit.addBodyElement(scriplet);
    }
    else
    {
      XMLBodyElement topElement = elementStack.peek();

      topElement.addBodyElement(scriplet);
    }

  }

  /**
   * @see org.eclipse.jet.internal.parser.JETParseEventListener2#handleComment(org.eclipse.jet.internal.parser.JETMark, org.eclipse.jet.internal.parser.JETMark)
   */
  public void handleComment(JETMark start, JETMark stop)
  {
    Comment comment = ast.newComment(
      start.getLine(),
      start.getCol(),
      start.getCursor() - 4,
      stop.getCursor() + 4,
      start.getCursor(),
      stop.getCursor(), reader.getChars(start, stop));
    if (elementStack.isEmpty())
    {
      compilationUnit.addBodyElement(comment);
    }
    else
    {
      XMLBodyElement topElement = elementStack.peek();

      topElement.addBodyElement(comment);
    }
  }

  /**
   * @see org.eclipse.jet.internal.parser.JETParseEventListener2#handleDeclaration(org.eclipse.jet.internal.parser.JETMark, org.eclipse.jet.internal.parser.JETMark)
   */
  public void handleDeclaration(JETMark start, JETMark stop)
  {
    JavaDeclaration decl = ast.newJavaDeclaration(
      start.getLine(),
      start.getCol(),
      start.getCursor() - 3,
      stop.getCursor() + 2,
      start.getCursor(),
      stop.getCursor(), reader.getChars(start, stop));

    compilationUnit.addBodyElement(decl);
  }

  /**
   * @see org.eclipse.jet.internal.parser.JETParseEventListener2#handleXMLEndTag(java.lang.String, org.eclipse.jet.internal.parser.JETMark, org.eclipse.jet.internal.parser.JETMark)
   */
  public void handleXMLEndTag(String tagName, JETMark start, JETMark stop)
  {
    int tagIndex = elementStack.findElementIndex(tagName);
    XMLBodyElementEnd endTag = ast.newXMLBodyElementEnd(start.getLine(), start.getCol(), 
      start.getCursor(), stop.getCursor(), tagName);
    if (tagIndex == -1)
    {
      compilationUnit.createProblem(
        ProblemSeverity.ERROR,
        IProblem.MissingXmlStartTag,
        JET2Messages.JET2Compiler_MissingStartTag,
        new Object []{ tagName },
        start.getCursor(),
        stop.getCursor(),
        start.getLine(), start.getCol());
    }
    else
    {
      while (!elementStack.isAtTop(tagIndex))
      {
        XMLBodyElement top = elementStack.pop();
        compilationUnit.createProblem(
          ProblemSeverity.ERROR,
          IProblem.MissingXmlEndTag,
          JET2Messages.JET2Compiler_MissingEndTag,
          new Object []{ top.getName() },
          start.getCursor(),
          stop.getCursor(),
          start.getLine(), start.getCol());
      }
      XMLBodyElement topElement = elementStack.pop();
      endTag.setStartTag(topElement);
      topElement.setEndTag(endTag);
    }
    if (elementStack.isEmpty())
    {
      compilationUnit.addBodyElement(endTag);
    }
    else
    {
      XMLBodyElement topElement = (XMLBodyElement)elementStack.peek();

      topElement.addBodyElement(endTag);
    }

  }

  /**
   * @see org.eclipse.jet.internal.parser.JETParseEventListener2#handleXMLEmptyTag(java.lang.String, org.eclipse.jet.internal.parser.JETMark, org.eclipse.jet.internal.parser.JETMark, java.util.Map)
   */
  public void handleXMLEmptyTag(String tagName, JETMark start, JETMark stop, Map attributeMap)
  {
    TagDefinition td = tagLibManager.getTagDefinition(tagName);

    XMLEmptyElement decl = ast.newXMLEmptyElement(
      start.getLine(),
      start.getCol(),
      start.getCursor(),
      stop.getCursor(),
      tagName,
      attributeMap,
      td);

    if (elementStack.isEmpty())
    {
      compilationUnit.addBodyElement(decl);
    }
    else
    {
      XMLBodyElement topElement = (XMLBodyElement)elementStack.peek();

      topElement.addBodyElement(decl);
    }
  }

  /**
   * @see org.eclipse.jet.internal.parser.JETParseEventListener2#handleXMLStartTag(java.lang.String, org.eclipse.jet.internal.parser.JETMark, org.eclipse.jet.internal.parser.JETMark, java.util.Map)
   */
  public void handleXMLStartTag(String tagName, JETMark start, JETMark stop, Map attributeMap)
  {

    TagDefinition td = tagLibManager.getTagDefinition(tagName);

    if(!td.isContentAllowed() && tagName.indexOf(':') == -1)
    {
      // bug 147714: for DPTK compatibility, allow empty tags to appear as <tagname ...> instead of <tagname .../>
      // We only allow this for tags that have no namespace (as DPTK did not support namespaces).
      recordProblem(ProblemSeverity.WARNING, IProblem.TagInterpretedAsEmptyTag,
        JET2Messages.JET2Compiler_TagShouldBeEmptyFormat,
        new Object [] {"<" + tagName + "/>"},  //$NON-NLS-1$ //$NON-NLS-2$
        start.getCursor(), stop.getCursor(), start.getLine(), start.getCol());
      handleXMLEmptyTag(tagName, start, stop, attributeMap);
    }
    else
    {
      XMLBodyElement decl = ast.newXMLBodyElement(
        start.getLine(),
        start.getCol(),
        start.getCursor(),
        stop.getCursor(),
        tagName,
        attributeMap,
        td);
  
      if (elementStack.isEmpty())
      {
        compilationUnit.addBodyElement(decl);
      }
      else
      {
        XMLBodyElement topElement = elementStack.peek();
  
        topElement.addBodyElement(decl);
      }
  
      elementStack.push(decl);
    }
  }

  /**
   * @see org.eclipse.jet.internal.parser.JETParseEventListener2#isKnownTag(java.lang.String)
   */
  public boolean isKnownTag(String tagName)
  {
    return tagLibManager.isKnownTag(tagName);
  }

  /**
   * Return the compilation unit created as a result of handling the JET2 parser events.
   * @return compilation unit
   */
  public JETCompilationUnit getCompilationUnit()
  {
    return compilationUnit;
  }

  /**
   * @see org.eclipse.jet.internal.parser.JETParseEventListener2#recordProblem(org.eclipse.jet.compiler.Problem.ProblemSeverity, int, java.lang.String, java.lang.Object[], int, int, int, int)
   */
  public void recordProblem(ProblemSeverity severity, int problemId, String message, Object[] msgArgs, int start, int end, int line, int colOffset)
  {
    compilationUnit.createProblem(severity, problemId, message, msgArgs, start, end, line, colOffset);
  }

  public TagLibraryReference[] getTagLibraryReferences()
  {
    TagLibraryReference[] result = tagLibManager.getTagLibraryReferences();
    
    return result;
  }
  
  /**
   * @deprecated
   * @param reader
   */
  public void parse(JETReader reader)  {
    parser = configureParser(reader);
    this.beginPageProcessing();
    try
    {
      parser.parse();
    }
    catch (org.eclipse.jet.internal.core.parser.jasper.JETException e)
    {
      // create a minimal compilation unit with the exeception recorded as the error.
      recordProblem(ProblemSeverity.ERROR, IProblem.JETException, e.getLocalizedMessage(), null, 0, 0, 1, 1);
    }
    this.endPageProcessing();

  }

  /**
   * @param reader
   * @return 
   */
  private JETParser configureParser(JETReader reader)
  {
    JETParser.Directive directive = new JETParser.Directive();
    directive.getDirectives().add("jet"); //$NON-NLS-1$
    directive.getDirectives().add("taglib"); //$NON-NLS-1$
    directive.getDirectives().add("include"); //$NON-NLS-1$
    directive.getDirectives().add("start"); //$NON-NLS-1$
    directive.getDirectives().add("end"); //$NON-NLS-1$
    final JETCoreElement[] coreElements;
    if(supportEmbeddedExpressions) {
	   coreElements = new JETCoreElement []{
	      new ErrorRedirectingCoreElementDelegate(directive),
	      new ErrorRedirectingCoreElementDelegate(new JETParser.Expression()),
	      new ErrorRedirectingCoreElementDelegate(new EmbeddedExpressionElement()),
	      new ErrorRedirectingCoreElementDelegate(new CommentElementDelegate()),
	      new ErrorRedirectingCoreElementDelegate(new DeclarationElementDelegate()),
	      new ErrorRedirectingCoreElementDelegate(new JETParser.Scriptlet()),
	      new ErrorRedirectingCoreElementDelegate(new XMLElementDelegate()), };
    } else {
 	   coreElements = new JETCoreElement []{
 		      new ErrorRedirectingCoreElementDelegate(directive),
 		      new ErrorRedirectingCoreElementDelegate(new JETParser.Expression()),
 		      new ErrorRedirectingCoreElementDelegate(new CommentElementDelegate()),
 		      new ErrorRedirectingCoreElementDelegate(new DeclarationElementDelegate()),
 		      new ErrorRedirectingCoreElementDelegate(new JETParser.Scriptlet()),
 		      new ErrorRedirectingCoreElementDelegate(new XMLElementDelegate()), };
    	
    }
    
    return new JETParser(reader, this, coreElements, supportEmbeddedExpressions ? EmbeddedExpressionFactory.DEFAULT_EXPRESSION_LANGUAGE : null);
  }

  public boolean isKnownInvalidTagName(String tagName)
  {
    return tagLibManager.isKnownInvalidTagName(tagName);
  }

  public Object parse(String templatePath)
  {
    final ITemplateInput templateInput = templateResolver.getInput(templatePath);
    final URI baseLocation = templateInput.getBaseLocation();
    try
    {
      if(compilationUnit == null) {
        compilationUnit = new JETAST().newJETCompilationUnit(baseLocation, templatePath, templateInput.getEncoding());
        ast = compilationUnit.getAst();
      }
      reader = new JETReader(baseLocation == null ? null : baseLocation.toString(), templatePath, templateInput.getReader());
      intermalParse();
    }
    catch (org.eclipse.jet.internal.core.parser.jasper.JETException e)
    {
      // create a minimal compilation unit with the exeception recorded as the error.
      recordProblem(ProblemSeverity.ERROR, IProblem.JETException, e.getLocalizedMessage(), null, 0, 0, 1, 1);
    }
    catch (TemplateInputException e)
    {
      // create a minimal compilation unit with the exeception recorded as the error.
      recordProblem(ProblemSeverity.ERROR, IProblem.JETException, e.getLocalizedMessage(), null, 0, 0, 1, 1);
    }
    return compilationUnit;
  }

  public Object parse(char[] template)
  {
    if(compilationUnit == null) {
      compilationUnit = new JETAST().newJETCompilationUnit(null, "", null); //$NON-NLS-1$
      ast = compilationUnit.getAst();
    }
    try
    {
      reader = new JETReader(null, "", new CharArrayReader(template)); //$NON-NLS-1$
      intermalParse();
    }
    catch (JETException e)
    {
      // create a minimal compilation unit with the exeception recorded as the error.
      recordProblem(ProblemSeverity.ERROR, IProblem.JETException, e.getLocalizedMessage(), null, 0, 0, 1, 1);
    }
    return compilationUnit;
  }

  /**
   * @throws JETException
   */
  private void intermalParse() throws JETException
  {
    parser = configureParser(reader);
    this.beginPageProcessing();
    parser.parse();
    this.endPageProcessing();
    compilationUnit.setTagLibraryReferences(getTagLibraryReferences());
    compilationUnit.accept(new TagValidationVisitor(compilationUnit));
    compilationUnit.accept(new TextTrimmingVisitor());
  }
  
	public void handleEmbeddedExpression(String language, JETMark start,
			JETMark stop) throws JETException {
		EmbeddedExpression ee = ast.newEmbeddedExpression(start.getLine(),
				start.getCol(), start.getCursor(), stop.getCursor(), language,
				reader.getChars(start, stop));

		if (elementStack.isEmpty()) {
			compilationUnit.addBodyElement(ee);
		} else {
			XMLBodyElement topElement = elementStack.peek();

			topElement.addBodyElement(ee);
		}
	}
}
