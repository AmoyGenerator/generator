/**
 * <copyright>
 *
 * Copyright (c) 2007 IBM Corporation and others.
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
 * $Id: JET2TemplateLoaderImpl.java,v 1.1 2007/05/16 16:44:13 pelder Exp $
 */
package org.eclipse.jet.internal.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Template;
import org.eclipse.jet.JET2TemplateLoader;
import org.eclipse.jet.JET2TemplateLoaderExtension;
import org.eclipse.jet.JET2Writer;
import org.osgi.framework.Bundle;

/**
 * An implementation of {@link JET2TemplateLoader} that loads template information
 * from a properties file.
 */
public class JET2TemplateLoaderImpl implements JET2TemplateLoader, JET2TemplateLoaderExtension
{

  private final Map map;
  private final Bundle bundle;
  private JET2TemplateLoader delegate;

  public JET2TemplateLoaderImpl(Bundle bundle, URL templateMap) throws IOException
  {
    this.bundle = bundle;
    this.map = getTemplateMap(bundle, templateMap);
  }

  private Map getTemplateMap(Bundle bundle, URL mapURL) throws IOException
  {
    InputStream inputStream = null;
    try {
      inputStream = mapURL.openStream();
      Properties map = new Properties();
      map.load(inputStream);
      return map;
    } finally {
      ensureClosed(inputStream);
    }
  }

  /**
   * @param inputStream
   */
  private void ensureClosed(InputStream inputStream)
  {
    if(inputStream != null) {
      try
      {
        inputStream.close();
      }
      catch (IOException e)
      {
        // nothing to do
      }
    }
  }


  private static final class JET1TemplateWrapper implements JET2Template
  {
    private static final String JET1_ARGUMENT_CONTEXT_VAR = "argument"; //$NON-NLS-1$
    private final Object template;
    private final Method generateMethod;

    public JET1TemplateWrapper(Class templateClass) throws SecurityException, NoSuchMethodException, InstantiationException, IllegalAccessException 
    {
      template = templateClass.newInstance();
      generateMethod = templateClass.getMethod("generate", new Class[] {Object.class}); //$NON-NLS-1$
    }

    public void generate(JET2Context context, JET2Writer out)
    {
      
      Object arg = context.hasVariable(JET1_ARGUMENT_CONTEXT_VAR) ? context.getVariable(JET1_ARGUMENT_CONTEXT_VAR) : null;
      try
      {
        Object result = generateMethod.invoke(template, new Object[] {arg});
        out.write(result != null ? result.toString() : ""); //$NON-NLS-1$
      }
      catch (IllegalArgumentException e)
      {
        context.logError(e.getCause());
      }
      catch (IllegalAccessException e)
      {
        context.logError(e.getCause());
      }
      catch (InvocationTargetException e)
      {
        context.logError(e.getCause());
      }
    }
    
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jet.JET2TemplateLoader#getTemplate(java.lang.String)
   */
  public JET2Template getTemplate(String templatePath)
  {
    String className = (String)map.get(templatePath);
    
    if(className != null) {
      try
      {
        Class templateClass = bundle.loadClass(className);
        
        if(JET2Template.class.isAssignableFrom(templateClass)) {
          return (JET2Template)templateClass.newInstance();
        } else {
          return new JET1TemplateWrapper(templateClass);
        }
            
      }
      catch (ClassNotFoundException e)
      {
        // do nothing
      }
      catch (InstantiationException e)
      {
        // do nothing
      }
      catch (IllegalAccessException e)
      {
        // do nothing
      }
      catch (SecurityException e)
      {
        // do nothing
      }
      catch (NoSuchMethodException e)
      {
        // do nothing
      }
    }    
    return delegate != null ? delegate.getTemplate(templatePath) : null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.JET2TemplateLoaderExtension#getDelegateLoader()
   */
  public JET2TemplateLoader getDelegateLoader()
  {
    return delegate;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.JET2TemplateLoaderExtension#setDelegateLoader(org.eclipse.jet.JET2TemplateLoader)
   */
  public void setDelegateLoader(JET2TemplateLoader loader)
  {
    this.delegate = loader;
  }

}
