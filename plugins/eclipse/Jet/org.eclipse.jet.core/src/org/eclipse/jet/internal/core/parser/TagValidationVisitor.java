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
 * $Id: TagValidationVisitor.java,v 1.2 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet.internal.core.parser;


import java.util.Iterator;

import org.eclipse.jet.core.parser.IProblem;
import org.eclipse.jet.core.parser.ProblemSeverity;
import org.eclipse.jet.core.parser.ast.JETASTVisitor;
import org.eclipse.jet.core.parser.ast.JETCompilationUnit;
import org.eclipse.jet.core.parser.ast.XMLBodyElement;
import org.eclipse.jet.core.parser.ast.XMLElement;
import org.eclipse.jet.core.parser.ast.XMLEmptyElement;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.TagAttributeDefinition;
import org.eclipse.jet.taglib.TagDefinition;


/**
 * Validate tags and attributes found on tag declarations. 
 * <p> 
 * Types of attribute validation:
 * <bl>
 * <li>that required attributes are in the tag declaration.</li>
 * <li>that all attributes correspond to a declared attribute.</li>
 * <li>that warnings are generated for deprecated attribute access.</li>
 * </bl>
 * <p>
 * Types of tag validation:
 * <bl>
 * <li>whether tag or tag library is deprecated</li>
 * <li>whether the tag is permitted to be used as an empty tag</li>
 * <li>whether the tag is permitted to be used as a content tag</li>
 * </bl>
 * </p>
 */
public final class TagValidationVisitor extends JETASTVisitor
{

  private final JETCompilationUnit cu;

  /**
   * @param cu 
   * 
   */
  public TagValidationVisitor(JETCompilationUnit cu)
  {
    super();
    this.cu = cu;
  }

  public boolean visit(XMLBodyElement xmlBodyElement)
  {
    if (!xmlBodyElement.getTagDefinition().isContentAllowed())
    {
      cu.createProblem(
        ProblemSeverity.ERROR,
        IProblem.TagCannotHaveContent,
        JET2Messages.JET2Compiler_TagCannotHaveContent,
        new Object [0],
        xmlBodyElement.getStart(),
        xmlBodyElement.getEnd(),
        xmlBodyElement.getLine(), 
        xmlBodyElement.getColumn());
    }
    validateAttributes(xmlBodyElement);
    return true;
  }

  private void validateAttributes(XMLElement xmlElement)
  {
    TagDefinition tagDefinition = xmlElement.getTagDefinition();

    if (tagDefinition.isDeprecated() || tagDefinition.getTagLibrary().isDeprecated())
    {
      cu.createProblem(
        ProblemSeverity.WARNING,
        IProblem.DeprecatedTag,
        JET2Messages.JET2Compiler_DeprecatedTag,
        new Object [0],
        xmlElement.getStart(),
        xmlElement.getEnd(),
        xmlElement.getLine(), 
        xmlElement.getColumn());
    }

    // Validate that every supplied attribute is in the tag definition
    for (Iterator i = xmlElement.getAttributes().keySet().iterator(); i.hasNext();)
    {
      String attrName = (String)i.next();
      if (tagDefinition.getAttributeDefinition(attrName) == null)
      {
        cu.createProblem(
          ProblemSeverity.ERROR,
          IProblem.UnknownAttributeInTag,
          JET2Messages.JET2Compiler_UnknownAttribute,
          new Object []{ attrName },
          xmlElement.getStart(),
          xmlElement.getEnd(),
          xmlElement.getLine(), 
          xmlElement.getColumn());
      }
    }

    // Validate that every required attribute is in the supplied attributes
    for (Iterator i = tagDefinition.getAttributeDefinitions().iterator(); i.hasNext();)
    {
      TagAttributeDefinition tad = (TagAttributeDefinition)i.next();
      final String attributeName = tad.getName();
      if (tad.isRequired() && !xmlElement.getAttributes().containsKey(attributeName))
      {
        cu.createProblem(
          ProblemSeverity.ERROR,
          IProblem.MissingRequiredAttribute,
          JET2Messages.JET2Compiler_MissingRequiredAttribute,
          new Object []{ attributeName },
          xmlElement.getStart(),
          xmlElement.getEnd(),
          xmlElement.getLine(), 
          xmlElement.getColumn());
      }
      if (tad.isDeprecated() && xmlElement.getAttributes().containsKey(attributeName))
      {
        cu.createProblem(
          ProblemSeverity.WARNING,
          IProblem.DeprecatedAttribute,
          JET2Messages.JET2Compiler_DeprecatedAttribute,
          new Object []{ attributeName },
          xmlElement.getStart(),
          xmlElement.getEnd(),
          xmlElement.getLine(), 
          xmlElement.getColumn());
      }
    }
  }

  public boolean visit(XMLEmptyElement xmlEmptyElement)
  {
    if (!xmlEmptyElement.getTagDefinition().isEmptyTagAllowed())
    {
      cu.createProblem(
        ProblemSeverity.ERROR,
        IProblem.TagCannotBeEmpty,
        JET2Messages.JET2Compiler_TagRequiresContent,
        new Object [0],
        xmlEmptyElement.getStart(),
        xmlEmptyElement.getEnd(),
        xmlEmptyElement.getLine(), 
        xmlEmptyElement.getColumn());
    }
    validateAttributes(xmlEmptyElement);
    
    return true;
  }

}
