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
 * $Id$
 * /
 *******************************************************************************/
package org.eclipse.jet.internal.compiler;

import java.util.Collections;
import java.util.Map;

import org.eclipse.jet.BodyContentWriter;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Template;
import org.eclipse.jet.JET2TemplateLoader;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.internal.compiler.templates.CodeGenTemplateLoader;
import org.eclipse.jet.transform.TransformContextExtender;

/**
 * Run JET templates for code generator
 */
public class TemplateRunner
{

  private JET2TemplateLoader templateLoader = null;

  public TemplateRunner() {
    // TODO Use a the JET Bundle Loader to get this. This requires some careful
    // work to make sure the corresponding JET bundle gets loaded and unloaded successfully.
    // @see IJETBundleManager#connect(String, IProgressMonitor)
    templateLoader = new CodeGenTemplateLoader();
  }
  
  public void generate(String templatePath, Map arguments, JET2Writer out) {
    JET2Context context = new JET2Context(null);
    TransformContextExtender.getInstance(context).setLoader(templateLoader);
    
    context.setVariables(arguments != null ? arguments : Collections.EMPTY_MAP);
    JET2Template template = templateLoader.getTemplate(templatePath);
    //add by yuxin start 2016-3-28
    if(template == null){
      return;
    }
    //add by yuxin end 2016-3-28
    template.generate(context, out);
  }
  
  public String generate(String templatePath, Map arguments) {
    JET2Writer out = new BodyContentWriter();
    generate(templatePath, arguments, out);
    return out.toString();
  }
}
